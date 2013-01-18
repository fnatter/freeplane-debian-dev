package org.freeplane.plugin.bugreport;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.util.FreeplaneVersion;
import org.freeplane.core.util.HtmlUtils;
import org.freeplane.core.util.LogUtils;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.ui.ViewController;

public class ReportGenerator extends StreamHandler {
	private static final String BUGREPORT_USER_ID = "org.freeplane.plugin.bugreport.userid";
	private static final String REMOTE_LOG = "RemoteLog";
	private static final String NO_REPORTS_SENT_BEFORE = "no reports sent before";
	static final String LAST_BUG_REPORT_INFO = "last_bug_report_info";

	private class SubmitRunner implements Runnable {

		public SubmitRunner() {
		}

		public void run() {
			runSubmit();
		}
	}

	private class SubmitStarter implements Runnable {
		SubmitStarter() {
			if (EventQueue.isDispatchThread()) {
				return;
			}
			final Thread currentThread = Thread.currentThread();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						currentThread.join(1000);
					}
					catch (final InterruptedException e) {
					}
				}
			});
		}

		public void run() {
			startSubmit();
		}
	}

	private final static String BUG_TRACKER_REFERENCE_URL = "http://freeplane.sourceforge.net/info/bugtracker.ref.txt";
	private static String BUG_TRACKER_URL = null;
	static boolean disabled = false;
	private static int errorCounter = 0;
	private static String info;
	static final private String OPTION = "org.freeplane.plugin.bugreport";
	private static ByteArrayOutputStream out = null;
	private static String version;
	private static String revision;

	private static String toHexString(final byte[] v) {
		final String HEX_DIGITS = "0123456789abcdef";
		final StringBuffer sb = new StringBuffer(v.length * 2);
		for (int i = 0; i < v.length; i++) {
			final int b = v[i] & 0xFF;
			sb.append(HEX_DIGITS.charAt(b >>> 4)).append(HEX_DIGITS.charAt(b & 0xF));
		}
		return sb.toString();
	}

	private String hash = null;
	private boolean isRunning;
	private String log = null;
	private MessageDigest md = null;
	private boolean reportCollected = false;
	private IBugReportListener bugReportListener;

	public IBugReportListener getBugReportListener() {
		return bugReportListener;
	}

	public void setBugReportListener(final IBugReportListener bugReportListener) {
		this.bugReportListener = bugReportListener;
	}

	public ReportGenerator() {
		super();
		try {
			setEncoding("UTF-8");
		}
		catch (final SecurityException e) {
		}
		catch (final UnsupportedEncodingException e) {
		}
		setFormatter(new BugFormatter());
		setLevel(Level.SEVERE);
	}

	private String calculateHash(final String errorMessage) {
		final String[] lines = errorMessage.split("\n");
		final StringBuffer hashInput = new StringBuffer();
		for (int i = 0; i < lines.length; i++) {
			final String s = lines[i];
			if (s.startsWith("\tat org.freeplane.")
					 || s.startsWith("missing key ")
					) {
				hashInput.append(s);
			}
		}
		if (hashInput.length() == 0) {
			return null;
		}
		hashInput.append(version);
		hashInput.append(revision);
		try {
			return calculateHash(hashInput.toString().getBytes(getEncoding()));
		}
		catch (final UnsupportedEncodingException e) {
			return null;
		}
	}

	private String calculateHash(final byte[] byteArray) {
		try {
			if (md == null) {
				md = MessageDigest.getInstance("MD5");
			}
			final byte[] digest = md.digest(byteArray);
			return ReportGenerator.toHexString(digest);
		}
		catch (final Exception e) {
			LogUtils.warn(e);
			return null;
		}
	}

	private void createInfo() {
		if (info == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append("freeplane_version = ");
			version = FreeplaneVersion.getVersion().toString();
			sb.append(version);
			sb.append("; freeplane_xml_version = ");
			sb.append(FreeplaneVersion.XML_VERSION);
			
			revision = FreeplaneVersion.getVersion().getRevision();
			
			if(! revision.equals("")){
				sb.append("\nbzr revision = ");
				sb.append(revision);
			}
			sb.append("\njava_version = ");
			sb.append(System.getProperty("java.version"));
			sb.append("; os_name = ");
			sb.append(System.getProperty("os.name"));
			sb.append("; os_version = ");
			sb.append(System.getProperty("os.version"));
			sb.append('\n');
			info = sb.toString();
		}
	}

	private String getBugTrackerUrl() {
		if (BUG_TRACKER_URL != null) {
			return BUG_TRACKER_URL;
		}
		try {
			final URL url = new URL(BUG_TRACKER_REFERENCE_URL);
			final BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			BUG_TRACKER_URL = in.readLine();
			return BUG_TRACKER_URL;
		}
		catch (final Exception e) {
			disabled = true;
			return null;
		}
	}

	private static class LogOpener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			final String freeplaneLogDirectoryPath = LogUtils.getLogDirectory();
			final File file = new File(freeplaneLogDirectoryPath);
			if(file.isDirectory()){
				final ViewController viewController = Controller.getCurrentController().getViewController();
				try {
	                viewController.openDocument(file.toURL());
                }
                catch (Exception ex) {
                }
			}
        }
	}
	JButton logButton;
	@Override
	public synchronized void publish(final LogRecord record) {
		if (out == null) {
			out = new ByteArrayOutputStream();
			setOutputStream(out);
		}
		if (!isLoggable(record)) {
			return;
		}
		if (!(disabled || isRunning  || reportCollected)) {
			reportCollected = true;
			EventQueue.invokeLater(new SubmitStarter());
		}
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("serial")
			public void run() {
				errorCounter++;
				if(TextUtils.getRawText("internal_error_tooltip", null) != null){
					if(logButton == null){
						final ImageIcon errorIcon = new ImageIcon(ResourceController.getResourceController().getResource(
								"/images/icons/messagebox_warning.png"));
						logButton = new JButton(){
							@Override public Dimension getPreferredSize(){
								Dimension preferredSize = super.getPreferredSize();
								preferredSize.height = getIcon().getIconHeight();
								return preferredSize;
							}
						};
						logButton.addActionListener(new LogOpener());
						logButton.setIcon(errorIcon);
						String tooltip = TextUtils.getText("internal_error_tooltip");
						logButton.setToolTipText(tooltip);
						Controller.getCurrentController().getViewController().addStatusComponent("internal_error", logButton);
					}
					logButton.setText(TextUtils.format("errornumber", errorCounter));

				}
			}
		});
		super.publish(record);
	}

	private void runSubmit() {
		try {
			close();
			final String errorMessage = out.toString(getEncoding());
			if (errorMessage.indexOf(getClass().getPackage().getName()) != -1) {
				// avoid infinite loops
				System.err.println("don't send bug reports from bugreport plugin");
				return;
			}
			createInfo();
			hash = calculateHash(errorMessage);
			if (hash == null) {
				return;
			}
			final String reportHeader = createReportHeader();
			StringBuilder sb = new StringBuilder();
			sb.append(reportHeader).append('\n').append("previous report : ");
			String lastReportInfo = ResourceController.getResourceController().getProperty(LAST_BUG_REPORT_INFO, NO_REPORTS_SENT_BEFORE);
			sb.append(lastReportInfo).append('\n');
			final String userId = ResourceController.getResourceController().getProperty(BUGREPORT_USER_ID);
			if (userId.length() > 0){
				sb.append("user : ").append(userId).append('\n');
			}
			sb.append(info);
			sb.append(errorMessage);
			log = sb.toString(); 
			
			if (log.equals("")) {
				return;
			}
			final ReportRegistry register = ReportRegistry.getInstance();
			if (register.isReportRegistered(hash)) {
				return;
			}
			final String option = showBugReportDialog();
			if (BugReportDialogManager.ALLOWED.equals(option)) {
				register.registerReport(hash, reportHeader);
				final Map<String, String> report = new LinkedHashMap<String, String>();
				report.put("hash", hash);
				report.put("log", log);
				report.put("version", version);
				report.put("revision", revision);
				final String status = sendReport(report);
				if (bugReportListener == null || status == null) {
					return;
				}
				bugReportListener.onReportSent(report, status);
			}
		}
		catch (final UnsupportedEncodingException e) {
			LogUtils.severe(e);
		}
		finally {
			out = null;
			reportCollected = false;
			isRunning = false;
		}
	}


	private String createReportHeader() {
	    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		String time = dateFormatGmt.format(new Date());
		final String currentReportInfo = "at "  + time +  " CMT,  hash " + hash;
	    return currentReportInfo;
    }

	private String showBugReportDialog() {
		String option = ResourceController.getResourceController().getProperty(OPTION, BugReportDialogManager.ASK);
		if (option.equals(BugReportDialogManager.ASK)) {
			if(FreeplaneVersion.getVersion().isFinal())
				return BugReportDialogManager.DENIED;
			String question = TextUtils.getText("org.freeplane.plugin.bugreport.question");
			if (!question.startsWith("<html>")) {
				question = HtmlUtils.plainToHTML(question);
			}
			final Object[] options = new Object[] { TextUtils.getText("org.freeplane.plugin.bugreport.always_agree"),
			        TextUtils.getText("org.freeplane.plugin.bugreport.agree"),
			        TextUtils.getText("org.freeplane.plugin.bugreport.deny"),
			        TextUtils.getText("org.freeplane.plugin.bugreport.always_deny") };
			final String title = TextUtils.getText("org.freeplane.plugin.bugreport.dialog.title");
			final String reportName = TextUtils.getText("org.freeplane.plugin.bugreport.report");
			final int choice = BugReportDialogManager.showBugReportDialog(title, question,
			    JOptionPane.INFORMATION_MESSAGE, options, options[1], reportName, log);
			switch (choice) {
				case 0:
					option = BugReportDialogManager.ALLOWED;
					ResourceController.getResourceController().setProperty(OPTION, option);
					break;
				case 1:
					option = BugReportDialogManager.ALLOWED;
					break;
				case 2:
					option = BugReportDialogManager.DENIED;
					break;
				case 3:
					option = BugReportDialogManager.DENIED;
					ResourceController.getResourceController().setProperty(OPTION, option);
					break;
				default:
					option = BugReportDialogManager.DENIED;
					break;
			}
		}
		return option;
	}

	private String sendReport(final Map<String, String> reportFields) {
		try {
			// Construct data
			final StringBuilder data = new StringBuilder();
			for (final Entry<String, String> entry : reportFields.entrySet()) {
				if (data.length() != 0) {
					data.append('&');
				}
				data.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				data.append('=');
				data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			// Send data
			final URL url = new URL(getBugTrackerUrl());
			final URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data.toString());
			wr.flush();
			// Get the response
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final String line = rd.readLine();
			if (line != null) {
				System.out.println(line);
			}
			wr.close();
			rd.close();
			return line;
		}
		catch (final Exception e) {
		}
		return null;
	}

	private void startSubmit() {
		isRunning = true;
		final Thread submitterThread = new Thread(new SubmitRunner(), REMOTE_LOG);
		submitterThread.start();
	}
}

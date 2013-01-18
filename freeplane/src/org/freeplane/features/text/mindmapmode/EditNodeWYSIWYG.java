/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.text.mindmapmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.MenuBuilder;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.core.util.HtmlUtils;
import org.freeplane.core.util.LogUtils;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.spellchecker.mindmapmode.SpellCheckerController;

import com.lightdev.app.shtm.SHTMLEditorPane;
import com.lightdev.app.shtm.SHTMLPanel;

/**
 * @author Daniel Polansky
 */
public class EditNodeWYSIWYG extends EditNodeBase {
	private static class HTMLDialog extends EditDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private SHTMLPanel htmlEditorPanel;
		private JButton splitButton;

		HTMLDialog(final EditNodeBase base, final String title, String purpose, final RootPaneContainer frame) throws Exception {
			super(base, title, frame);
			createEditorPanel(purpose);
			getContentPane().add(htmlEditorPanel, BorderLayout.CENTER);
			UITools.addEscapeActionToDialog(getDialog(), new CancelAction());
			final JButton okButton = new JButton();
			final JButton cancelButton = new JButton();
			splitButton = new JButton();
			MenuBuilder.setLabelAndMnemonic(okButton, TextUtils.getRawText("ok"));
			MenuBuilder.setLabelAndMnemonic(cancelButton, TextUtils.getRawText("cancel"));
			MenuBuilder.setLabelAndMnemonic(splitButton, TextUtils.getRawText("split"));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					submit();
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					cancel();
				}
			});
			splitButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					split();
				}
			});
			UITools.addKeyActionToDialog(getDialog(), new SubmitAction(), "alt ENTER", "submit");
			final JPanel buttonPane = new JPanel();
			buttonPane.add(okButton);
			buttonPane.add(cancelButton);
			buttonPane.add(splitButton);
			buttonPane.setMaximumSize(new Dimension(1000, 20));
			if (ResourceController.getResourceController().getBooleanProperty("el__buttons_above")) {
				getContentPane().add(buttonPane, BorderLayout.NORTH);
			}
			else {
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see freeplane.view.mindmapview.EditNodeBase.Dialog#close()
		 */
		@Override
		protected void cancel() {
			super.cancel();
			final StyleSheet styleSheet = htmlEditorPanel.getDocument().getStyleSheet();
			styleSheet.removeStyle("p");
			styleSheet.removeStyle("BODY");
			getBase().getEditControl().cancel();
		}

		private SHTMLPanel createEditorPanel(String purpose) throws Exception {
			if (htmlEditorPanel == null) {
				htmlEditorPanel = MTextController.getController().createSHTMLPanel(purpose);
				final SHTMLEditorPane editorPane = (SHTMLEditorPane) htmlEditorPanel.getEditorPane();
				final SpellCheckerController spellCheckerController = SpellCheckerController.getController();
				spellCheckerController.enableAutoSpell(editorPane, true);
				spellCheckerController.addSpellCheckerMenu(editorPane.getPopup());
				spellCheckerController.enableShortKey(editorPane, true);
			}
			return htmlEditorPanel;
		}

		/**
		 * @return Returns the htmlEditorPanel.
		 */
		public SHTMLPanel getHtmlEditorPanel() {
			return htmlEditorPanel;
		}

		@Override
		public Component getMostRecentFocusOwner() {
			if (getDialog().isFocused()) {
				return getFocusOwner();
			}
			else {
				return htmlEditorPanel.getMostRecentFocusOwner();
			}
		}

		@Override
		protected boolean isChanged() {
			return htmlEditorPanel.needsSaving();
		}

		/*
		 * (non-Javadoc)
		 * @see freeplane.view.mindmapview.EditNodeBase.Dialog#split()
		 */
		@Override
		protected void split() {
			super.split();
			final StyleSheet styleSheet = htmlEditorPanel.getDocument().getStyleSheet();
			styleSheet.removeStyle("p");
			styleSheet.removeStyle("body");
			getBase().getEditControl().split(HtmlUtils.unescapeHTMLUnicodeEntity(htmlEditorPanel.getDocumentText()),
			    htmlEditorPanel.getCaretPosition());
		}

		/*
		 * (non-Javadoc)
		 * @see freeplane.view.mindmapview.EditNodeBase.Dialog#close()
		 */
		@Override
		protected void submit() {
			super.submit();
			htmlEditorPanel.getDocument().getStyleSheet().removeStyle("p");
			htmlEditorPanel.getDocument().getStyleSheet().removeStyle("body");
			if (htmlEditorPanel.needsSaving()) {
				getBase().getEditControl().ok(HtmlUtils.unescapeHTMLUnicodeEntity(htmlEditorPanel.getDocumentText()));
			}
			else {
				getBase().getEditControl().cancel();
			}
		}

		public void setSplitEnabled(boolean enableSplit) {
			splitButton.setEnabled(enableSplit);
	        splitButton.setVisible(enableSplit);
        }
	}

	private static final Dimension PREFERRED_SIZE = new Dimension(600, 400);

	private String title;
	
	private Font font;
	private Color textColor = Color.BLACK;
	private Dimension preferredSize = PREFERRED_SIZE;

	public String getTitle() {
    	return title;
    }

	public void setTitle(String purpose) {
    	this.title = purpose;
    }

	public Font getFont() {
    	return font;
    }

	public void setFont(Font font) {
    	this.font = font;
    }

	public Color getTextColor() {
    	return textColor;
    }

	public void setTextColor(Color textColor) {
    	this.textColor = textColor;
    }

	public Dimension getPreferredSize() {
    	return preferredSize;
    }

	public void setPreferredSize(Dimension preferredSize) {
    	this.preferredSize = preferredSize;
    }

	public EditNodeWYSIWYG(final NodeModel node, final String text, final IEditControl editControl, boolean enableSplit) {
		super(node, text, editControl);
	}

	public void show(final RootPaneContainer frame) {
		try {
			HTMLDialog htmlEditorWindow = createHtmlEditor(frame);
			htmlEditorWindow.setBase(this);
			final String titleText;
			titleText = TextUtils.getText(title);
			htmlEditorWindow.getDialog().setTitle(titleText);
			htmlEditorWindow.setSplitEnabled(getEditControl().canSplit());
			final SHTMLPanel htmlEditorPanel = (htmlEditorWindow).getHtmlEditorPanel();
			final StringBuilder ruleBuilder = new StringBuilder(100);
			ruleBuilder.append("body {");
			if(font != null){
				ruleBuilder.append("font-family: ").append(font.getFamily()).append(";");
				final int fontSize = Math.round(font.getSize() / UITools.FONT_SCALE_FACTOR);
				ruleBuilder.append("font-size: ").append(fontSize).append("pt;");
				if (font.isItalic()) {
					ruleBuilder.append("font-style: italic; ");
				}
				if (font.isBold()) {
					ruleBuilder.append("font-weight: bold; ");
				}
			}
			if(textColor != null)
				ruleBuilder.append("color: ").append(ColorUtils.colorToString(textColor)).append(";");
		    final Color bgColor = getBackground();
			ruleBuilder.append("background-color: ").append(ColorUtils.colorToString(bgColor)).append(";");
			ruleBuilder.append("}\n");
			ruleBuilder.append("p {margin-top:0;}\n");
			final HTMLDocument document = htmlEditorPanel.getDocument();
			final JEditorPane editorPane = htmlEditorPanel.getEditorPane();
			if(textColor != null){
				editorPane.setForeground(textColor);
				editorPane.setCaretColor(textColor);
			}
			final StyleSheet styleSheet = document.getStyleSheet();
			styleSheet.removeStyle("p");
			styleSheet.removeStyle("body");
			styleSheet.addRule(ruleBuilder.toString());
			final URL url = node.getMap().getURL();
			if (url != null) {
				document.setBase(url);
			}
			else {
				document.setBase(new URL("file: "));
			}
			htmlEditorPanel.setContentPanePreferredSize(preferredSize);
			htmlEditorWindow.getDialog().pack();
			if (ResourceController.getResourceController().getBooleanProperty("el__position_window_below_node")) {
				UITools.setDialogLocationUnder(htmlEditorWindow.getDialog(), node);
			}
			else {
				UITools.setDialogLocationRelativeTo(htmlEditorWindow.getDialog(), node);
			}
			String content = text;
			if (!HtmlUtils.isHtmlNode(content)) {
				content = HtmlUtils.plainToHTML(content);
			}
			htmlEditorPanel.setCurrentDocumentContent(content);
			final KeyEvent firstKeyEvent = MTextController.getController().getEventQueue().getFirstEvent();
			final JTextComponent currentPane = htmlEditorPanel.getEditorPane();
			if (currentPane == htmlEditorPanel.getMostRecentFocusOwner()) {
				redispatchKeyEvents(currentPane, firstKeyEvent);
				if (firstKeyEvent == null) {
					editorPane.setCaretPosition(htmlEditorPanel.getDocument().getLength());
				}
			}
			else{
				final EventBuffer keyEventDispatcher = MTextController.getController().getEventQueue();
				keyEventDispatcher.deactivate();
			}
			htmlEditorPanel.getMostRecentFocusOwner().requestFocus();
			htmlEditorWindow.show();
		}
		catch (final Exception ex) {
			LogUtils.severe("Loading of WYSIWYG HTML editor failed. Use the other editors instead.", ex);
		}
	}

	public HTMLDialog createHtmlEditor(final RootPaneContainer frame) throws Exception {
		final JRootPane rootPane = ((RootPaneContainer)frame).getRootPane();
		HTMLDialog htmlEditorWindow = (HTMLDialog) rootPane.getClientProperty(HTMLDialog.class);
		if (htmlEditorWindow == null) {
			htmlEditorWindow = new HTMLDialog(this, "", "", frame);
			rootPane.putClientProperty(HTMLDialog.class, htmlEditorWindow);
			// make sure that SHTML gets notified of relevant config changes!
		   	ResourceController.getResourceController().addPropertyChangeListener(
	    			new FreeplaneToSHTMLPropertyChangeAdapter(htmlEditorWindow.getHtmlEditorPanel()));
		}
	    return htmlEditorWindow;
    }
}

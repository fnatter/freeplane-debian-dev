package org.freeplane.features.clipboard;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

public class CopyNodeURIAction extends AFreeplaneAction {
	public CopyNodeURIAction() {
		super("CopyNodeURIAction");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(final ActionEvent e) {
		final Controller controller = Controller.getCurrentController();
		final NodeModel node = controller.getSelection().getSelected();
		File mindmapFile = node.getMap().getFile();
		if(mindmapFile == null) {
			UITools.errorMessage(TextUtils.getRawText("map_not_saved"));
			return;
		}
		final String idString = mindmapFile.toURI().toString() + '#' + node.createID();
		final ClipboardController clipboardController = (ClipboardController) Controller.getCurrentModeController().getExtension(
		    ClipboardController.class);
		clipboardController.setClipboardContents(new StringSelection(idString));
		controller.getViewController().out(idString);
	}
}

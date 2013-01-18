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
package org.freeplane.features.filter;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.freeplane.core.extension.IExtension;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.filter.condition.ASelectableCondition;
import org.freeplane.features.filter.condition.ICondition;
import org.freeplane.features.map.IMapSelection;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.MapController.Direction;
import org.freeplane.features.mode.Controller;

class FindAction extends AFreeplaneAction {
	static final String KEY = "FindAction";
	static private class FindNodeList implements IExtension {
		String rootID;
		final LinkedList<String> nodesUnfoldedByDisplay = new LinkedList<String>();
		ASelectableCondition condition;

		static FindNodeList create(final MapModel map) {
			FindNodeList list = FindNodeList.get(map);
			if (list == null) {
				list = new FindNodeList();
				map.addExtension(list);
			}
			return list;
		}

		private static FindNodeList get(final MapModel map) {
			if (map == null) {
				return null;
			}
			final FindNodeList list = (FindNodeList) map.getExtension(FindNodeList.class);
			return list;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FilterConditionEditor editor;

	public FindAction() {
		super(KEY);
	}

	public void actionPerformed(final ActionEvent e) {
		final IMapSelection selection = Controller.getCurrentController().getSelection();
		if (selection == null) {
			return;
		}
		final NodeModel start = selection.getSelected();
		if (editor == null) {
			editor = new FilterConditionEditor(FilterController.getCurrentFilterController());
			editor.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5, 0, 5, 0)));

		}
		else {
			editor.mapChanged(start.getMap());
		}
		editor.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(final AncestorEvent event) {
				final Component component = event.getComponent();
				((FilterConditionEditor) component).focusInputField(true);
				((JComponent) component).removeAncestorListener(this);
			}

			public void ancestorMoved(final AncestorEvent event) {
			}

			public void ancestorRemoved(final AncestorEvent event) {
			}
		});
		final int run = UITools.showConfirmDialog(start, editor, TextUtils.getText("FindAction.text"),
		    JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);	
		final Container parent = editor.getParent();
		if (parent != null) {
			parent.remove(editor);
		}
		if (run != JOptionPane.OK_OPTION) {
			return;
		}
		final ASelectableCondition condition = editor.getCondition();
		findFirst(condition);
	}

	void findFirst(final ASelectableCondition condition) {
	    final FindNodeList info = FindNodeList.create(Controller.getCurrentController().getMap());
		info.condition = condition;
		if (info.condition == null) {
			return;
		}
		info.rootID = Controller.getCurrentController().getSelection().getSelected().createID();
		findNext();
    }

	void findNext() {
		final MapModel map = Controller.getCurrentController().getMap();
		final FindNodeList info = FindNodeList.get(map);
		if (info == null || info.condition == null) {
			displayNoPreviousFindMessage();
			return;
		}
		final FilterController filterController = FilterController.getCurrentFilterController();
		final NodeModel start = Controller.getCurrentController().getSelection().getSelected();
		final NodeModel root = map.getNodeForID(info.rootID);
		if (root == null) {
			info.condition = null;
			displayNoPreviousFindMessage();
			return;
		}
		for (NodeModel n = start; !root.equals(n); n = n.getParentNode()) {
			if (n == null) {
				info.condition = null;
				displayNoPreviousFindMessage();
				return;
			}
		}
		final NodeModel next = filterController.findNext(start, null, Direction.FORWARD, info.condition);
		if (next == null) {
			displayNotFoundMessage(root, info.condition);
			return;
		}
		displayNode(info, next);
	}

	private void displayNoPreviousFindMessage() {
		UITools.informationMessage(Controller.getCurrentController().getViewController().getFrame(), TextUtils
		    .getText("no_previous_find"));
	}

	/**
	 * Display a node in the display (used by find and the goto action by arrow
	 * link actions).
	 */
	private void displayNode(final FindNodeList info, final NodeModel node) {
		final MapModel map = node.getMap();
		final LinkedList<String> nodesUnfoldedByDisplay = new LinkedList<String>();
		NodeModel nodeOnPath = null;
		for (nodeOnPath = node; nodeOnPath != null && !info.nodesUnfoldedByDisplay.contains(nodeOnPath.createID()); nodeOnPath = nodeOnPath
		    .getParentNode()) {
			if (Controller.getCurrentModeController().getMapController().isFolded(nodeOnPath)) {
				nodesUnfoldedByDisplay.add(nodeOnPath.createID());
			}
		}
		final ListIterator<String> oldPathIterator = info.nodesUnfoldedByDisplay
		    .listIterator(info.nodesUnfoldedByDisplay.size());
		while (oldPathIterator.hasPrevious()) {
			final String oldPathNodeID = oldPathIterator.previous();
			final NodeModel oldPathNode = map.getNodeForID(oldPathNodeID);
			if (oldPathNode != null && oldPathNode.equals(nodeOnPath)) {
				break;
			}
			oldPathIterator.remove();
			if (oldPathNode != null) {
				Controller.getCurrentModeController().getMapController().setFolded(oldPathNode, true);
			}
		}
		info.nodesUnfoldedByDisplay.addAll(nodesUnfoldedByDisplay);
		Controller.getCurrentModeController().getMapController().select(node);
	}

	private void displayNotFoundMessage(final NodeModel start, final ICondition condition) {
		final String message = TextUtils.format("no_more_found_from", condition.toString(), getFindFromText(start));
		UITools.informationMessage(Controller.getCurrentController().getViewController().getFrame(), message);
	}

	public String getFindFromText(final NodeModel node) {
		final String plainNodeText = node.toString().replaceAll("\n", " ");
		return plainNodeText.length() <= 30 ? plainNodeText : plainNodeText.substring(0, 30) + "...";
	}
}

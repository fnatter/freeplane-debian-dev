/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2009 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
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
package org.freeplane.plugin.script;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.freeplane.core.ui.components.JRestrictedSizeScrollPane;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.TextUtils;

/**
 * @author Dimitry Polivaev
 * Mar 5, 2009
 */
public class ScriptComboBoxEditor implements ComboBoxEditor {

	final private JButton showEditorBtn;
	final private List<ActionListener> actionListeners;
	private String script;
	private Dimension minimumSize;
	private Rectangle bounds;

	public ScriptComboBoxEditor() {
		showEditorBtn = new JButton();
		final String title = TextUtils.getText("EditScript");
		UITools.addTitledBorder(showEditorBtn, title, 10);
		showEditorBtn.setHorizontalAlignment(SwingConstants.LEFT);
		showEditorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editScript(false);
			}
		});
		actionListeners = new LinkedList<ActionListener>();
		minimumSize = new Dimension(100, 60);
	}

	public Dimension getMinimumSize() {
    	return minimumSize;
    }

	public void setMinimumSize(Dimension minimumSize) {
    	this.minimumSize = minimumSize;
    }

	protected void editScript(boolean selectAll) {
		JEditorPane textEditor = new JEditorPane();
		textEditor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		final JRestrictedSizeScrollPane scrollPane = new JRestrictedSizeScrollPane(textEditor);
		scrollPane.setMinimumSize(minimumSize);
		textEditor.setContentType("text/groovy");
		textEditor.setText(script);
		if(selectAll){
			textEditor.selectAll();
		}
		String title = TextUtils.getText("plugins/ScriptEditor/window.title");
		final JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		final JDialog dialog = optionPane.createDialog(showEditorBtn, title);
		dialog.setResizable(true);
		if(bounds != null)
			dialog.setBounds(bounds);
		dialog.setVisible(true);
		bounds = dialog.getBounds();
		final Integer result = ((Integer)optionPane.getValue());
		if(result == null || result != JOptionPane.OK_OPTION)
			return;
		script = textEditor.getText();
		setButtonText();
	    final ActionEvent actionEvent = new ActionEvent(this, 0, null);
	    for (final ActionListener l : actionListeners) {
	    	l.actionPerformed(actionEvent);
	    }
    }

	protected void setButtonText() {
	    final String text = script.substring(0, Math.min(40, script.length())).trim().replaceAll("\\s+", " ");
		showEditorBtn.setText(text);
    }

	public Component getEditorComponent() {
		return showEditorBtn;
	}
	public void setItem(Object anObject) {
		if(anObject == null)
			script = "";
		else
			this.script = (String)anObject;
		if("".equals(script))
			showEditorBtn.setText(" ");
		else
			setButtonText();
    }

	public Object getItem() {
	    return script;
    }
	public void selectAll() {
		editScript(true);
    }
	public void addActionListener(final ActionListener l) {
		actionListeners.add(l);
	}

	public void removeActionListener(final ActionListener l) {
		actionListeners.remove(l);
	}
	
	public void setPreferredSize(Dimension preferredSize) {
	    showEditorBtn.setPreferredSize(preferredSize);
    }

	public Dimension getPreferredSize() {
	    return showEditorBtn.getPreferredSize();
    }

}

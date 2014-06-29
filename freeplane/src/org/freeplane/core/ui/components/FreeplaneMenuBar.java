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
package org.freeplane.core.ui.components;

import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/**
 * This is the menu bar for Freeplane. Actions are defined in MenuListener.
 * Moreover, the StructuredMenuHolder of all menus are hold here.
 */
public class FreeplaneMenuBar extends JMenuBar {
	public static final String EDIT_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/edit";
	public static final String EXTRAS_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/extras";
	public static final String FILE_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/file";
	public static final String FORMAT_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/format";
	public static final String HELP_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/help";
	public static final String INSERT_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/insert";
	public static final String MAP_POPUP_MENU = "/map_popup";
	public static final String MENU_BAR_PREFIX = "/menu_bar";
	public static final String MODES_MENU = "main_menu_modes";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_MENU = FreeplaneMenuBar.MENU_BAR_PREFIX + "/view";

	public FreeplaneMenuBar() {
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0), "none");
	}

	static final int KEY_MODIFIERS = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.SHIFT_MASK | KeyEvent.ALT_GRAPH_DOWN_MASK
	        | KeyEvent.ALT_GRAPH_MASK;

	public static KeyStroke derive(final KeyStroke ks, final Character keyChar) {
		if (ks == null) {
			return ks;
		}
		final int modifiers = ks.getModifiers();
		if (ks.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
			if (0 != (modifiers & KEY_MODIFIERS)) {
				switch (keyChar) {
					case '<':
						return KeyStroke
						    .getKeyStroke(KeyEvent.VK_LESS, modifiers & ~KEY_MODIFIERS, ks.isOnKeyRelease());
					case '>':
						return KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, modifiers & ~KEY_MODIFIERS, ks
						    .isOnKeyRelease());
					case '+':
						return KeyStroke
						    .getKeyStroke(KeyEvent.VK_PLUS, modifiers & ~KEY_MODIFIERS, ks.isOnKeyRelease());
					case '-':
						return KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, modifiers & ~KEY_MODIFIERS, ks
						    .isOnKeyRelease());
					case '=':
						return KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, modifiers & ~KEY_MODIFIERS, ks
						    .isOnKeyRelease());
					case '.':
						return KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, modifiers & ~KEY_MODIFIERS, ks
						    .isOnKeyRelease());
				}
			}
			if (keyChar != '\0' && keyChar != KeyEvent.CHAR_UNDEFINED) {
				return KeyStroke.getKeyStroke(keyChar, modifiers);
			}
		}
		return ks;
	}

	@Override
	public boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
		// ignore key events without modifiers if text component is a source
		if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && e.getKeyChar() != '\0'
		        && 0 == (e.getModifiers() & ~KEY_MODIFIERS) && e.getSource() instanceof JTextComponent) {
			return false;
		}
		if (super.processKeyBinding(ks, e, condition, pressed)) {
			return true;
		}
		final KeyStroke derivedKS = FreeplaneMenuBar.derive(ks, e.getKeyChar());
		if (derivedKS == ks) {
			return false;
		}
		return super.processKeyBinding(derivedKS, e, condition, pressed);
	}
}

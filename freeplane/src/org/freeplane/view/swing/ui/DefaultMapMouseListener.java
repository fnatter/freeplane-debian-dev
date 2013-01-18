/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
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
package org.freeplane.view.swing.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.freeplane.core.ui.ControllerPopupMenuListener;
import org.freeplane.core.ui.IMouseListener;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.map.IMapSelection;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.view.swing.map.MapView;

/**
 * @author Dimitry Polivaev
 * 06.01.2009
 */
/**
 * The MouseListener which belongs to MapView
 */
public class DefaultMapMouseListener implements IMouseListener {

	public DefaultMapMouseListener() {
	}

	protected void handlePopup(final MouseEvent e) {
		if (e.isPopupTrigger()) {
			Component popup = null;
			final Component popupForModel;
			final MapView mapView = (MapView) Controller.getCurrentController().getViewController().getMapView();
			final ModeController modeController = Controller.getCurrentController().getModeController();
			if(mapView != null){
				final java.lang.Object obj = mapView.detectCollision(e.getPoint());
				popupForModel= LinkController.getController(modeController).getPopupForModel(obj);
			}
			else{
				popupForModel = null;
			}
			if (popupForModel != null) {
				final ControllerPopupMenuListener popupListener = new ControllerPopupMenuListener();
				popupForModel.addHierarchyListener(popupListener);
				popup = popupForModel;
			}
			else {
				popup = modeController.getUserInputListenerFactory().getMapPopup();
			}
            Component component = e.getComponent();
			if(popup instanceof JPopupMenu) {
                ((JPopupMenu)popup).show(component, e.getX(), e.getY());
            }
			else {
			    Point locationOnScreen = component.getLocationOnScreen();
			    final Component window;
			    if(popup instanceof Window){
			        window= popup;
			    }
			    else{
                    final Frame frame = UITools.getFrame();
                    final JDialog d = new JDialog(frame, popup.getName());
                    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    d.setModal(false);
                    d.add(popup);
                    d.pack();
                    d.addWindowFocusListener(new WindowFocusListener() {
                        public void windowLostFocus(WindowEvent e) {
                        }
                        
                        public void windowGainedFocus(WindowEvent e) {
                            frame.addWindowFocusListener(new WindowFocusListener() {
                                public void windowLostFocus(WindowEvent e) {
                                }
                                
                                public void windowGainedFocus(WindowEvent e) {
                                    d.setVisible(false);
                                    frame.removeWindowFocusListener(this);
                                }
                            });
                            d.removeWindowFocusListener(this);
                        }
                    });
			        window = d;
			    }
			    window.setLocation(locationOnScreen.x+e.getX(), locationOnScreen.y + e.getY());
			    window.setVisible(true);
			}
			
		}
	}

	public void mouseClicked(final MouseEvent e) {
		final Object source = e.getSource();
		if(! (source instanceof MapView))
			return;
		final MapView map = (MapView) source;
		final Controller controller = map.getModeController().getController();
		final IMapSelection selection = controller.getSelection();
		if(selection != null){
			final NodeModel selected = selection.getSelected();
			if(selected != null)
				controller.getViewController().getComponent(selected).requestFocusInWindow();
		}
	}

	public void mouseEntered(final MouseEvent e) {
	}

	public void mouseExited(final MouseEvent e) {
	}

	public void mouseMoved(final MouseEvent e) {
	}

	public void mousePressed(final MouseEvent e) {
		if (e.isPopupTrigger()) {
			handlePopup(e);
		}
		else if (e.getButton() == MouseEvent.BUTTON1){
			final MapView mapView = getMapView(e.getComponent());
			if(mapView != null){
				mapView.setMoveCursor(true);
				originX = e.getX();
				originY = e.getY();
			}
		}
		e.consume();
	}

	public void mouseReleased(final MouseEvent e) {
		final MapView mapView = getMapView(e.getComponent());
		if(mapView != null)
			mapView.setMoveCursor(false);
		originX = -1;
		originY = -1;
		handlePopup(e);
		e.consume();
	}
	// // 	final private Controller controller;
	protected int originX = -1;
	protected int originY = -1;

	/**
	 *
	 */
	public void mouseDragged(final MouseEvent e) {
		final Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		final JComponent component = (JComponent) e.getComponent();
		final MapView mapView = getMapView(component);
		if(mapView == null)
			return;
		final boolean isEventPointVisible = component.getVisibleRect().contains(r);
		if (!isEventPointVisible) {
			component.scrollRectToVisible(r);
		}
		if (originX >= 0 && isEventPointVisible) {
			mapView.scrollBy(originX - e.getX(), originY - e.getY());
		}
	}

	private MapView getMapView(final Component component) {
		if(component instanceof MapView)
			return (MapView) component;
		return (MapView) SwingUtilities.getAncestorOfClass(MapView.class, component);
	}
}

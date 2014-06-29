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
package org.freeplane.features.map;

import java.io.IOException;
import org.freeplane.core.io.IAttributeWriter;
import org.freeplane.core.io.IElementWriter;
import org.freeplane.core.io.ITreeWriter;
import org.freeplane.core.io.xml.TreeXmlWriter;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.features.map.MapWriter.Hint;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.n3.nanoxml.XMLElement;

class NodeWriter implements IElementWriter, IAttributeWriter {
	private boolean mayWriteChildren;
	final private MapController mapController;
	final private boolean shouldWriteChildren;
	private final boolean writeFolded;
	final private boolean writeInvisible;
	private XMLElement xmlNode;
	final private String nodeTag;

	public NodeWriter(final MapController mapController, final String nodeTag, final boolean writeChildren,
	                  final boolean writeInvisible) {
		this.mapController = mapController;
		this.shouldWriteChildren = writeChildren;
		this.mayWriteChildren = true;
		this.writeInvisible = writeInvisible;
		this.nodeTag = nodeTag;
		final String saveFolding = ResourceController.getResourceController().getProperty(
		    NodeBuilder.RESOURCES_SAVE_FOLDING);
		writeFolded = saveFolding.equals(NodeBuilder.RESOURCES_ALWAYS_SAVE_FOLDING)
		        || saveFolding.equals(NodeBuilder.RESOURCES_SAVE_FOLDING_IF_MAP_IS_CHANGED);
	}

	private void saveChildren(final ITreeWriter writer, final NodeModel node) throws IOException {
		for (final NodeModel child: mapController.childrenUnfolded(node)) {
			if (writeInvisible || child.isVisible()) {
				writer.addElement(child, nodeTag);
			}
			else {
				saveChildren(writer, child);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * freeplane.io.IAttributeWriter#saveAttributes(freeplane.io.ITreeWriter,
	 * java.lang.Object, java.lang.String)
	 */
	public void writeAttributes(final ITreeWriter writer, final Object content, final String tag) {
		if (tag.equals(nodeTag)) {
			final NodeModel node = (NodeModel) content;
			writeAttributesGenerateContent(writer, node);
			return;
		}
	}

	private void writeAttributesGenerateContent(final ITreeWriter writer, final NodeModel node) {
		/** fc, 12.6.2005: XML must not contain any zero characters. */
		xmlNode = new XMLElement();
		EncryptionModel encryptionModel = EncryptionModel.getModel(node);
		mayWriteChildren = true;
		final Object mode = writer.getHint(Hint.MODE);
		if (encryptionModel != null && !(encryptionModel.isAccessible() && Mode.EXPORT.equals(mode))) {
        	final String enctyptedContent = encryptionModel.getEncryptedContent(mapController);
        	if(enctyptedContent != null){
        		writer.addAttribute(NodeBuilder.XML_NODE_ENCRYPTED_CONTENT, enctyptedContent);
        		mayWriteChildren = false;
        	}
        }
		if (mayWriteChildren && (writeFolded || !writer.getHint(Hint.MODE).equals(Mode.FILE))) {
			if(mapController.isFolded(node)){
				writer.addAttribute("FOLDED", "true");
			}
			else if(node.isRoot() && ! Mode.STYLE.equals(mode)){
				writer.addAttribute("FOLDED", "false");
			}
		}
		final NodeModel parentNode = node.getParentNode();
		if (parentNode != null && parentNode.isRoot()) {
			writer.addAttribute("POSITION", node.isLeft() ? "left" : "right");
		}
		final boolean saveID = !mode.equals(Mode.STYLE);
		if (saveID) {
			final String id = node.createID();
			writer.addAttribute("ID", id);
		}
		if (!mode.equals(Mode.STYLE)
		        && node.getHistoryInformation() != null
		        && ResourceController.getResourceController().getBooleanProperty(
		            NodeBuilder.RESOURCES_SAVE_MODIFICATION_TIMES)) {
			writer.addAttribute(NodeBuilder.XML_NODE_HISTORY_CREATED_AT, TreeXmlWriter.dateToString(node
			    .getHistoryInformation().getCreatedAt()));
			writer.addAttribute(NodeBuilder.XML_NODE_HISTORY_LAST_MODIFIED_AT, TreeXmlWriter.dateToString(node
			    .getHistoryInformation().getLastModifiedAt()));
		}
		writer.addExtensionAttributes(node, node.getExtensions().values());
	}

	public void writeContent(final ITreeWriter writer, final Object content, final String tag) throws IOException {
		final NodeModel node = (NodeModel) content;
		writer.addExtensionNodes(node, node.getExtensions().values());
		for (int i = 0; i < xmlNode.getChildrenCount(); i++) {
			writer.addElement(null, xmlNode.getChildAtIndex(i));
		}
		if (mayWriteChildren && shouldWriteChildren && mapController.childrenUnfolded(node).size()>0) {
			saveChildren(writer, node);
		}
		return;
	}

	String getNodeTag() {
		return nodeTag;
	}
}

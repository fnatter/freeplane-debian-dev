/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is created by Dimitry Polivaev in 2008.
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
package org.freeplane.features.nodestyle;

import java.awt.Color;
import java.io.IOException;

import org.freeplane.core.extension.IExtension;
import org.freeplane.core.io.IAttributeHandler;
import org.freeplane.core.io.IAttributeWriter;
import org.freeplane.core.io.IElementDOMHandler;
import org.freeplane.core.io.IElementWriter;
import org.freeplane.core.io.IExtensionAttributeWriter;
import org.freeplane.core.io.IExtensionElementWriter;
import org.freeplane.core.io.ITreeWriter;
import org.freeplane.core.io.ReadManager;
import org.freeplane.core.io.WriteManager;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.core.util.FreeplaneVersion;
import org.freeplane.features.map.MapWriter;
import org.freeplane.features.map.NodeBuilder;
import org.freeplane.features.map.NodeModel;
import org.freeplane.n3.nanoxml.XMLElement;

class NodeStyleBuilder implements IElementDOMHandler, IExtensionElementWriter, IExtensionAttributeWriter,
        IAttributeWriter, IElementWriter {
	static class FontProperties {
		String fontName;
		Integer fontSize;
		Boolean isBold;
		Boolean isItalic;
	}

	private final NodeStyleController nsc;

	public NodeStyleBuilder(final NodeStyleController nsc) {
		this.nsc = nsc;
	}

	public Object createElement(final Object parent, final String tag, final XMLElement attributes) {
		if (tag.equals("font")) {
			return new FontProperties();
		}
		return null;
	}

	public void endElement(final Object parent, final String tag, final Object userObject, final XMLElement dom) {
		if (parent instanceof NodeModel) {
			final NodeModel node = (NodeModel) parent;
			if (tag.equals("font")) {
				final FontProperties fp = (FontProperties) userObject;
				NodeStyleModel nodeStyleModel = NodeStyleModel.getModel(node);
				if (nodeStyleModel == null) {
					nodeStyleModel = new NodeStyleModel();
					node.addExtension(nodeStyleModel);
				}
				nodeStyleModel.setFontFamilyName(fp.fontName);
				nodeStyleModel.setFontSize(fp.fontSize);
				nodeStyleModel.setItalic(fp.isItalic);
				nodeStyleModel.setBold(fp.isBold);
				return;
			}
			return;
		}
	}

	private void registerAttributeHandlers(final ReadManager reader) {
		final IAttributeHandler colorHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				if (value.length() == 7) {
					final NodeModel node = (NodeModel) userObject;
					NodeStyleModel.setColor(node, ColorUtils.stringToColor(value));
				}
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "COLOR", colorHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "COLOR", colorHandler);
		final IAttributeHandler bgHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				if (value.length() == 7) {
					final NodeModel node = (NodeModel) userObject;
					NodeStyleModel.setBackgroundColor(node, ColorUtils.stringToColor(value));
				}
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "BACKGROUND_COLOR", bgHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "BACKGROUND_COLOR", bgHandler);
		final IAttributeHandler styleHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final NodeModel node = (NodeModel) userObject;
				NodeStyleModel.setShape(node, value);
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "STYLE", styleHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "STYLE", styleHandler);
		reader.addAttributeHandler("font", "SIZE", new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final FontProperties fp = (FontProperties) userObject;
				fp.fontSize = Integer.parseInt(value.toString());
			}
		});
		reader.addAttributeHandler("font", "NAME", new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final FontProperties fp = (FontProperties) userObject;
				fp.fontName = value;
			}
		});
		reader.addAttributeHandler("font", "BOLD", new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final FontProperties fp = (FontProperties) userObject;
				fp.isBold = value.equals("true");
			}
		});
		reader.addAttributeHandler("font", "ITALIC", new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final FontProperties fp = (FontProperties) userObject;
				fp.isItalic = value.equals("true");
			}
		});
		final IAttributeHandler nodenumberingHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final NodeModel node = (NodeModel) userObject;
				NodeStyleModel.setNodeNumbering(node, value.equals("true"));
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "NUMBERED", nodenumberingHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "NUMBERED", nodenumberingHandler);
		
		final IAttributeHandler formatHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final NodeModel node = (NodeModel) userObject;
				NodeStyleModel.setNodeFormat(node, value);
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "FORMAT", formatHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "FORMAT", formatHandler);
		if (FreeplaneVersion.getVersion().isOlderThan(new FreeplaneVersion(1, 3, 0))) {
			// compatibility for a view 1.2.X preview versions - remove after release 1.3
			reader.addAttributeHandler(NodeBuilder.XML_NODE, "TEMPLATE", formatHandler);
			reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "TEMPLATE", formatHandler);
		}
		
		final IAttributeHandler nodeMaxNodeWidthHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final NodeModel node = (NodeModel) userObject;
				NodeSizeModel.setNodeMaxNodeWidth(node, Integer.valueOf(value));
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "MAX_WIDTH", nodeMaxNodeWidthHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "MAX_WIDTH", nodeMaxNodeWidthHandler);
		
		final IAttributeHandler nodeMinWidthHandler = new IAttributeHandler() {
			public void setAttribute(final Object userObject, final String value) {
				final NodeModel node = (NodeModel) userObject;
				NodeSizeModel.setNodeMinWidth(node, Integer.valueOf(value));
			}
		};
		reader.addAttributeHandler(NodeBuilder.XML_NODE, "MIN_WIDTH", nodeMinWidthHandler);
		reader.addAttributeHandler(NodeBuilder.XML_STYLENODE, "MIN_WIDTH", nodeMinWidthHandler);
		
	}

	/**
	 */
	public void registerBy(final ReadManager reader, final WriteManager writer) {
		reader.addElementHandler("font", this);
		registerAttributeHandlers(reader);
		writer.addAttributeWriter(NodeBuilder.XML_NODE, this);
		writer.addAttributeWriter(NodeBuilder.XML_STYLENODE, this);
		writer.addElementWriter(NodeBuilder.XML_NODE, this);
		writer.addElementWriter(NodeBuilder.XML_STYLENODE, this);
		writer.addExtensionElementWriter(NodeStyleModel.class, this);
		writer.addExtensionAttributeWriter(NodeStyleModel.class, this);
		writer.addExtensionAttributeWriter(NodeSizeModel.class, this);
	}

	public void setAttributes(final String tag, final Object node, final XMLElement attributes) {
	}

	public void writeAttributes(final ITreeWriter writer, final Object userObject, final String tag) {
		final boolean forceFormatting = Boolean.TRUE.equals(writer.getHint(MapWriter.WriterHint.FORCE_FORMATTING));
		if (!forceFormatting) {
			return;
		}
		final NodeModel node = (NodeModel) userObject;
		writeAttributes(writer, node, (NodeStyleModel)null, true);
		writeAttributes(writer, node, (NodeSizeModel)null, true);
	}

	public void writeAttributes(final ITreeWriter writer, final Object userObject, final IExtension extension) {
		final boolean forceFormatting = Boolean.TRUE.equals(writer.getHint(MapWriter.WriterHint.FORCE_FORMATTING));
		if (forceFormatting) {
			return;
		}
		if(extension instanceof NodeStyleModel){
			final NodeStyleModel style = (NodeStyleModel) extension;
			writeAttributes(writer, null, style, false);
			return;
		}
		if(extension instanceof NodeSizeModel){
			final NodeSizeModel size = (NodeSizeModel) extension;
			writeAttributes(writer, null, size, false);
			return;
		}
		
	}

	private void writeAttributes(final ITreeWriter writer, final NodeModel node, final NodeStyleModel style,
	                             final boolean forceFormatting) {
		final Color color = forceFormatting ? nsc.getColor(node) : style.getColor();
		if (color != null) {
			writer.addAttribute("COLOR", ColorUtils.colorToString(color));
		}
		final Color backgroundColor = forceFormatting ? nsc.getBackgroundColor(node) : style.getBackgroundColor();
		if (backgroundColor != null) {
			writer.addAttribute("BACKGROUND_COLOR", ColorUtils.colorToString(backgroundColor));
		}
		final String shape = forceFormatting ? nsc.getShape(node) : style.getShape();
		if (shape != null) {
			writer.addAttribute("STYLE", shape);
		}
		final Boolean numbered = forceFormatting ? nsc.getNodeNumbering(node) : style.getNodeNumbering();
		if (numbered != null && numbered) {
			writer.addAttribute("NUMBERED", numbered.toString());
		}
		final String format = forceFormatting ? nsc.getNodeFormat(node) : style.getNodeFormat();
		if (format != null) {
			writer.addAttribute("FORMAT", format);
		}
	}

	private void writeAttributes(final ITreeWriter writer, final NodeModel node, final NodeSizeModel size,
	                             final boolean forceFormatting) {
		final int maxTextWidth = forceFormatting ? nsc.getMaxWidth(node) : size.getMaxNodeWidth();
		if (maxTextWidth != NodeSizeModel.NOT_SET) {
			writer.addAttribute("MAX_WIDTH", Integer.toString(maxTextWidth));
		}
		
		final int minTextWidth = forceFormatting ? nsc.getMinWidth(node) : size.getMinNodeWidth();
		if (minTextWidth != NodeSizeModel.NOT_SET) {
			writer.addAttribute("MIN_WIDTH", Integer.toString(minTextWidth));
		}
	}
	public void writeContent(final ITreeWriter writer, final Object userObject, final String tag) throws IOException {
		final boolean forceFormatting = Boolean.TRUE.equals(writer.getHint(MapWriter.WriterHint.FORCE_FORMATTING));
		if (!forceFormatting) {
			return;
		}
		final NodeModel node = (NodeModel) userObject;
		writeContent(writer, node, null, true);
	}

	public void writeContent(final ITreeWriter writer, final Object userObject, final IExtension extension)
	        throws IOException {
		final boolean forceFormatting = Boolean.TRUE.equals(writer.getHint(MapWriter.WriterHint.FORCE_FORMATTING));
		if (forceFormatting) {
			return;
		}
		final NodeStyleModel style = (NodeStyleModel) extension;
		writeContent(writer, null, style, false);
	}

	private void writeContent(final ITreeWriter writer, final NodeModel node, final NodeStyleModel style,
	                          final boolean forceFormatting) throws IOException {
		if (forceFormatting || style != null) {
			final XMLElement fontElement = new XMLElement();
			fontElement.setName("font");
			boolean isRelevant = forceFormatting;
			final String fontFamilyName = forceFormatting ? nsc.getFontFamilyName(node) : style.getFontFamilyName();
			if (fontFamilyName != null) {
				fontElement.setAttribute("NAME", fontFamilyName);
				isRelevant = true;
			}
			final Integer fontSize = forceFormatting ? Integer.valueOf(nsc.getFontSize(node)) : style.getFontSize();
			if (fontSize != null) {
				fontElement.setAttribute("SIZE", Integer.toString(fontSize));
				isRelevant = true;
			}
			final Boolean bold = forceFormatting ? Boolean.valueOf(nsc.isBold(node)) : style.isBold();
			if (bold != null) {
				fontElement.setAttribute("BOLD", bold ? "true" : "false");
				isRelevant = true;
			}
			final Boolean italic = forceFormatting ? Boolean.valueOf(nsc.isItalic(node)) : style.isItalic();
			if (italic != null) {
				fontElement.setAttribute("ITALIC", italic ? "true" : "false");
				isRelevant = true;
			}
			if (isRelevant) {
				writer.addElement(style, fontElement);
			}
		}
	}
}

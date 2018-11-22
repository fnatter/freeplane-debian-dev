package org.freeplane.plugin.script;

import java.net.URL;
import java.util.Vector;

import org.freeplane.core.util.HtmlUtils;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.plugin.script.dependencies.RelatedElements;

public class NodeScript {
	final NodeModel node;
	final String script;

	public NodeScript(NodeModel node, String script) {
		this.node = node;
		this.script = script;
	}
	String format(NodeScript nodeScriptToHighlight) {
		return (nodeScriptToHighlight.equals(this) ? "* " : "") + node.createID() + " "
				+ limitLength(toPlainText(node.getText()), 30) //
				+ " -> " + limitLength(script, 60);
	}

	private String toPlainText(String string) {
		return HtmlUtils.htmlToPlain(string).replaceAll("\\s+", " ");
	}

	private String limitLength(final String string, int maxLenght) {
		if (string == null || maxLenght >= string.length())
			return string;
		maxLenght = maxLenght > 3 ? maxLenght - 3 : maxLenght;
		return string.substring(0, maxLenght) + "...";
	}


	MapModel getMap() {
		return node.getMap();
	}

	public URL getBaseUrl() {
		return getMap().getURL();
	}

	public RelatedElements containingElements(){
		RelatedElements elements = new RelatedElements(node);
		Object userObject = node.getUserObject();
		if(scriptIsContainedIn(userObject))
			elements.relateNode(node);
		NodeAttributeTableModel attributeTableModel = node.getExtension(NodeAttributeTableModel.class);
		if(attributeTableModel != null) {
			final Vector<Attribute> attributes = attributeTableModel.getAttributes();
			attributes.stream().filter(a -> scriptIsContainedIn(a.getValue()))
					.forEach(a -> elements.relateAttribute(node, a));
		}
		return elements;
	}

	public boolean scriptIsContainedIn(Object userObject) {
		return FormulaUtils.containsFormula(userObject) && script.equals(FormulaUtils.scriptOf((String)userObject));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeScript other = (NodeScript) obj;
		if (node != other.node)
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		}
		return script.equals(other.script);
	}

	@Override
	public String toString() {
		return node + "[" + script + "]";
	}

}

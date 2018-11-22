package org.freeplane.plugin.script.dependencies;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;

public class RelatedElements {
	private final Map<Object, NodeModel> relatedElements;
	private final NodeModel relatedNode;
	private Set<MapModel> relatedMaps;

	public RelatedElements(final NodeModel relatedNode) {
		this.relatedNode = relatedNode;
		relatedElements = new HashMap<Object, NodeModel>();
	}

	public void relateAttribute(final NodeModel relatedNode, final Attribute attribute) {
		if (this.relatedNode.getMap() == relatedNode.getMap())
			relatedElements.put(attribute, relatedNode);
	}

	public void relateNode(final NodeModel accessedNode) {
		if (relatedNode.getMap() == accessedNode.getMap()) {
			relatedElements.put(accessedNode, accessedNode);
		}
	}

	public void relateMap(final MapModel accessedMap) {
		if (relatedNode.getMap() != accessedMap) {
			add(accessedMap);
		}
	}

	private void add(final MapModel accessedMap) {
		if(relatedMaps == null)
			relatedMaps = new HashSet<>();
		relatedMaps.add(accessedMap);
	}

	public boolean isEmpty() {
		return relatedElements.isEmpty();
	}

	public Collection<NodeModel> getRelatedNodes() {
		return relatedElements.values();
	}

	public Collection<Object> getElements() {
		return relatedElements.keySet();
	}

	public Set<Entry<Object, NodeModel>> entrySet() {
		return relatedElements.entrySet();
	}
}

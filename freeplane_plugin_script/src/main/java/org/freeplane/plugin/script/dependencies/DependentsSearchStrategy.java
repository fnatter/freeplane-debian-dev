package org.freeplane.plugin.script.dependencies;

import org.freeplane.core.util.Pair;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.plugin.script.FormulaDependencies;
import org.freeplane.plugin.script.FormulaUtils;

import java.util.Collection;
import java.util.Vector;

class DependentsSearchStrategy implements DependencySearchStrategy {
	@Override
	public RelatedElements find(final NodeModel node) {
		return find(node, node);
	}

	@Override
	public RelatedElements find(NodeModel node, Attribute attribute) {
		return find(node, (Object) attribute);
	}

	private RelatedElements find(final NodeModel node, Object element) {
		final Iterable<NodeModel> possibleDependencies = FormulaDependencies.getPossibleDependencies(node);
		final RelatedElements relatedElements = new RelatedElements(node);
		for (final NodeModel candidate : possibleDependencies) {
			final Object userObject = candidate.getUserObject();
			final Collection<Object> candidatePrecedents = FormulaUtils.getRelatedElements(candidate, userObject).getElements();
			if (candidatePrecedents.contains(node))
				relatedElements.relateNode(candidate);
			NodeAttributeTableModel attributeTableModel = candidate.getExtension(NodeAttributeTableModel.class);
			if(attributeTableModel != null) {
				final Vector<Attribute> attributes = attributeTableModel.getAttributes();
				attributes.stream().filter(a -> FormulaUtils.getRelatedElements(candidate, a.getValue()).getElements().contains(element))
						.forEach(a -> relatedElements.relateAttribute(candidate, a));
			}
		}
		return relatedElements;
	}

	@Override
	public Pair<NodeModel, NodeModel> inConnectionOrder(Pair<NodeModel, NodeModel> nodePair) {
		return nodePair;
	}
}

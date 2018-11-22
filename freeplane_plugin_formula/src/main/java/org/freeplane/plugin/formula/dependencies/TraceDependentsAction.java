package org.freeplane.plugin.formula.dependencies;

import org.freeplane.core.extension.Configurable;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.mode.Controller;
import org.freeplane.plugin.formula.FormulaPluginUtils;

import java.awt.event.ActionEvent;

class TraceDependentsAction extends AFreeplaneAction {
	private static final long serialVersionUID = 1L;
	private final LinkController linkController;

	public TraceDependentsAction(LinkController linkController) {
		super(FormulaPluginUtils.getFormulaKey("TraceDependentsAction"));
		this.linkController = linkController;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Configurable mapViewConfiguration = Controller.getCurrentController().getMapViewManager().getMapViewConfiguration();
		FormulaDependencyTracer tracer = mapViewConfiguration.computeIfAbsent(FormulaDependencyTracer.class, () -> new FormulaDependencyTracer(mapViewConfiguration, linkController));
		tracer.findDependents();
	}
}

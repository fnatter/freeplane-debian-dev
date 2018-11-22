package org.freeplane.plugin.script;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.freeplane.core.extension.Configurable;
import org.freeplane.core.util.HtmlUtils;
import org.freeplane.core.util.LogUtils;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.plugin.script.dependencies.RelatedElements;

public class FormulaUtils {

	/** evaluate text as a script if it starts with '='.
	 * @return the evaluation result for script and the original text otherwise
	 * @throws ExecuteScriptException */
	public static Object evalIfScript(final NodeModel nodeModel, final String text){
		if (textContainsFormula(text)) {
			final String script = scriptOf(text);
			return eval(nodeModel, script);
		}
		else {
			return text;
		}
	}

	public static Object safeEvalIfScript(final NodeModel nodeModel, final String text) {
        try {
            return evalIfScript(nodeModel, text);
        } catch (final Exception e) {
            LogUtils.info("could not interpret as a formula (ignored): " + text + " due to " + e.getMessage());
            return text;
        }
    }

	public static boolean textContainsFormula(final String text) {
		// ignore == and => since these are often used in text
		return startsWithEqualSign(text) && secondCharIsntSpecial(text.charAt(1));
	}

	private static boolean startsWithEqualSign(final String text) {
		return text != null && text.length() > 2 && text.charAt(0) == '=';
	}

	private static boolean secondCharIsntSpecial(final char secondChar) {
		return secondChar != '=' && secondChar != '>';
	}

	public static boolean containsFormula(final Object object) {
		return (object instanceof String) && containsFormula((String)object);
	}

	public static boolean containsFormula(final String text) {
	    if(HtmlUtils.isHtmlNode(text))
	    	return htmlContainsFormula(text);
	    else
	    	return textContainsFormula(text);
    }

	private static final Pattern FIRST_CHARACTER_IN_HTML = Pattern.compile("(?m)>\\s*[^<\\s]");

	private static boolean htmlContainsFormula(final String text) {
	    final Matcher matcher = FIRST_CHARACTER_IN_HTML.matcher(text);
		return matcher.find() && text.charAt(matcher.end()-1) == '=';
    }

	/** evaluate text as a script.
	 * @return the evaluation result.
	 * @throws ExecuteScriptException */
	private static Object eval(final NodeModel nodeModel, final String script) {
		final NodeScript nodeScript = new NodeScript(nodeModel, script);
		final ScriptContext scriptContext = new ScriptContext(nodeScript);
		final ScriptingPermissions restrictedPermissions = ScriptingPermissions.getFormulaPermissions();
		if (FormulaCache.ENABLE_CACHING) {
			final FormulaCache formulaCache = FormulaCache.of(nodeModel.getMap());
			Object value = formulaCache.getOrThrowCachedResult(nodeScript);
			if (value == null) {
				try {
					value = evaluateLoggingExceptions(nodeScript, scriptContext, restrictedPermissions);
					formulaCache.put(nodeScript, new CachedResult(value, scriptContext.getRelatedElements()));
				}
				catch (final ExecuteScriptException e) {
					formulaCache.put(nodeScript, new CachedResult(e, scriptContext.getRelatedElements()));
					throw e;
				}
			}
			return value;
		}
		else {
			return evaluateLoggingExceptions(nodeScript, scriptContext, restrictedPermissions);
		}
	}

	private static Object evaluateLoggingExceptions(final NodeScript nodeScript, final ScriptContext scriptContext,
	                           final ScriptingPermissions restrictedPermissions) {
		try {
			return evaluateCheckingForCyclesAndNonNullResult(nodeScript, scriptContext, restrictedPermissions);
		}
		catch (final ExecuteScriptException e) {
			final NodeModel node = nodeScript.node;
			final URL url = node.getMap().getURL();
			String nodeLocation = url != null ? url.toString() : "Unsaved map ";
			String message = "Error on evaluating formula in map " + nodeLocation + ", node " +  node.getID() + ",\n"
					+ "Script '" + nodeScript.script + "'";
			LogUtils.warn(message, e);
			throw e;
		}
	}

	private static Object evaluateCheckingForCyclesAndNonNullResult(final NodeScript nodeScript,
																	final ScriptContext scriptContext,
																	final ScriptingPermissions restrictedPermissions) {
		if (!FormulaThreadLocalStack.INSTANCE.push(nodeScript)) {
			showCyclicDependency(nodeScript);
			final String message = TextUtils.format("formula.error.circularReference",
				HtmlUtils.htmlToPlain(nodeScript.script));
			Controller.getCurrentController().getViewController().out(message);
			throw new ExecuteScriptException(new CyclicScriptReferenceException(message));
		}
		try {
			final Object value = ScriptingEngine.executeScript(nodeScript.node, nodeScript.script, scriptContext,
				restrictedPermissions);
			if (value == null)
				throw new ExecuteScriptException("Null pointer returned by formula");
			return value;
		}
		finally {
			FormulaThreadLocalStack.INSTANCE.pop();
		}
	}

	private static void showCyclicDependency(final NodeScript nodeScript) {
		final Controller controller = Controller.getCurrentController();
		if (controller.getMap() != nodeScript.node.getMap())
			return;
		final List<NodeScript> cycle = FormulaThreadLocalStack.INSTANCE.findCycle(nodeScript);
		final Configurable configurable = controller.getMapViewManager().getMapViewConfiguration();
		final DependencyHighlighter dependencyHighlighter = new DependencyHighlighter(LinkController.getController(),
			configurable);
		if (! cycle.isEmpty())
			dependencyHighlighter.showCyclicDependency(nodeScript);
	}

	public static RelatedElements getRelatedElements(final NodeModel node, final Object object) {
		if (FormulaUtils.containsFormula(object)) {
			final RelatedElements accessedValues = FormulaCache.of(node.getMap()).getAccessedValues(node,
				scriptOf((String) object));
			if (accessedValues != null)
				return accessedValues;
		}
		return new RelatedElements(node);
	}

	public static String scriptOf(final String object) {
		return object.substring(1);
	}

	public static void clearCache(final MapModel map) {
		FormulaDependencies.clearCache(map);
	}

	public static void evaluateAllFormulas(MapModel map) {
		clearCache(map);
		evaluateOutdatedFormulas(map);
	}

	public static void evaluateOutdatedFormulas(MapModel map) {
		evaluateAllRecursively(map.getRootNode());
	}

	static private void evaluateAllRecursively(NodeModel node) {
		evaluateObject(node, node.getUserObject());
		NodeAttributeTableModel attributeTableModel = node.getExtension(NodeAttributeTableModel.class);
		if(attributeTableModel != null)
			attributeTableModel.getAttributes().stream().forEach(a -> evaluateObject(node, a.getValue()));
		node.getChildren().stream().forEach(FormulaUtils::evaluateAllRecursively);
	}

	static private void evaluateObject(NodeModel node, Object userObject) {
		try {
			if (FormulaUtils.containsFormula(userObject)){
				FormulaUtils.evalIfScript(node, (String) userObject);
			}
		} catch (Exception e) {
		}
	}
}

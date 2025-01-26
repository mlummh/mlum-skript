package ch.njol.skript.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Feature;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.script.Script;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Name("All Scripts")
@Description("Returns all of the scripts, or just the enabled or disabled ones.")
@Examples({
	"command /scripts:",
	"\ttrigger:",
	"\t\tsend \"All Scripts: %scripts%\" to player",
	"\t\tsend \"Loaded Scripts: %enabled scripts%\" to player",
	"\t\tsend \"Unloaded Scripts: %disabled scripts%\" to player"
})
@Since("2.5")
public class ExprScriptsOld extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprScriptsOld.class, String.class, ExpressionType.SIMPLE,
				"[all [of the]|the] scripts [1:without ([subdirectory] paths|parents)]",
				"[all [of the]|the] (enabled|loaded) scripts [1:without ([subdirectory] paths|parents)]",
				"[all [of the]|the] (disabled|unloaded) scripts [1:without ([subdirectory] paths|parents)]");
	}

	public static final Path SCRIPTS_PATH = Skript.getInstance().getScriptsFolder().getAbsoluteFile().toPath();

	private boolean includeEnabled;
	private boolean includeDisabled;
	private boolean noPaths;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (this.getParser().hasExperiment(Feature.SCRIPT_REFLECTION))
			return false;
		includeEnabled = matchedPattern <= 1;
		includeDisabled = matchedPattern != 1;
		noPaths = parseResult.mark == 1;
		return true;
	}

	@Override
	protected String[] get(Event event) {
		List<Path> scripts = new ArrayList<>();
		if (includeEnabled) {
			for (Script script : ScriptLoader.getLoadedScripts())
				scripts.add(script.getConfig().getPath());
		}
		if (includeDisabled)
			scripts.addAll(ScriptLoader.getDisabledScripts()
					.stream()
					.map(File::toPath)
					.collect(Collectors.toList()));
		return formatPaths(scripts);
	}

	@SuppressWarnings("null")
	private String[] formatPaths(List<Path> paths) {
		return paths.stream()
			.map(path -> {
				if (noPaths)
					return path.getFileName().toString();
				return SCRIPTS_PATH.relativize(path.toAbsolutePath()).toString();
			})
			.toArray(String[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String text;
		if (!includeEnabled) {
			text = "all disabled scripts";
		} else if (!includeDisabled) {
			text = "all enabled scripts";
		} else {
			text = "all scripts";
		}
		if (noPaths)
			text = text + " without paths";
		return text;
	}

}

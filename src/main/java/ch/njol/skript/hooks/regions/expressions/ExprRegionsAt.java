package ch.njol.skript.hooks.regions.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.hooks.regions.RegionsPlugin;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author Peter Güttinger
 */
@Name("Regions At")
@Description({
	"All <a href='./classes.html#region'>regions</a> at a particular <a href='./classes/#location'>location</a>.",
	"This expression requires a supported regions plugin to be installed."
})
@Examples({
	"On click on a sign:",
	"\tline 1 of the clicked block is \"[region info]\"",
	"\tset {_regions::*} to regions at the clicked block",
	"\tif {_regions::*} is empty:",
	"\t\tmessage \"No regions exist at this sign.\"",
	"\telse:",
	"\t\tmessage \"Regions containing this sign: &lt;gold&gt;%{_regions::*}%<r>.\""
})
@Since("2.1")
@RequiredPlugins("Supported regions plugin")
public class ExprRegionsAt extends SimpleExpression<Region> {
	static {
		Skript.registerExpression(ExprRegionsAt.class, Region.class, ExpressionType.PROPERTY,
				"[the] region(1¦s|) %direction% %locations%");
	}
	
	@SuppressWarnings("null")
	private Expression<Location> locs;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		if (matchedPattern == 1)
			Skript.warning("Most regions plugins can have multiple intersecting regions at a the same location, thus it is recommended to use \"regions at ...\" instead of \"region at...\" for clarity.");
		locs = Direction.combine((Expression<? extends Direction>) exprs[0], (Expression<? extends Location>) exprs[1]);
		return true;
	}
	
	@SuppressWarnings("null")
	@Override
	@Nullable
	protected Region[] get(final Event e) {
		final Location[] ls = locs.getArray(e);
		if (ls.length == 0)
			return new Region[0];
		final ArrayList<Region> r = new ArrayList<>();
		for (final Location l : ls)
			r.addAll(RegionsPlugin.getRegionsAt(l));
		return r.toArray(new Region[r.size()]);
	}
	
	@Override
	public boolean isSingle() {
		return false;
	}
	
	@Override
	public Class<? extends Region> getReturnType() {
		return Region.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the regions at " + locs.toString(e, debug);
	}
	
}

package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Readied Arrow/Bow")
@Description("The bow or arrow in a <a href='events.html#ready_arrow'>Ready Arrow event</a>.")
@Examples({
	"on player ready arrow:",
		"\tselected bow's name is \"Spectral Bow\"",
		"\tif selected arrow is not a spectral arrow:",
			"\t\tcancel event"
})
@Since("2.8.0")
@Events("ready arrow")
public class ExprReadiedArrow extends SimpleExpression<ItemStack> {

	static {
		if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerReadyArrowEvent"))
			Skript.registerExpression(ExprReadiedArrow.class, ItemStack.class, ExpressionType.SIMPLE, "[the] (readied|selected|drawn) (:arrow|bow)");
	}

	private boolean isArrow;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		isArrow = parseResult.hasTag("arrow");
		if (!getParser().isCurrentEvent(PlayerReadyArrowEvent.class)) {
			Skript.error("'the readied " + (isArrow ? "arrow" : "bow") + "' can only be used in a ready arrow event");
			return false;
		}
		return true;
	}

	@Override
	@Nullable
	protected ItemStack[] get(Event event) {
		if (!(event instanceof PlayerReadyArrowEvent))
			return null;
		if (isArrow)
			return new ItemStack[]{((PlayerReadyArrowEvent) event).getArrow()};
		return new ItemStack[]{((PlayerReadyArrowEvent) event).getBow()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the readied " + (isArrow ? "arrow" : "bow");
	}

}

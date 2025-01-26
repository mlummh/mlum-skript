package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.jetbrains.annotations.Nullable;

@Name("Egg Will Hatch")
@Description("Whether the egg will hatch in a Player Egg Throw event.")
@Examples({
	"on player egg throw:",
	"\tif an entity won't hatch:",
	"\t\tsend \"Better luck next time!\" to the player"
})
@Events("Egg Throw")
@Since("2.7")
public class CondWillHatch extends Condition {

	static {
		Skript.registerCondition(CondWillHatch.class,
				"[the] egg (:will|will not|won't) hatch"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(PlayerEggThrowEvent.class)) {
			Skript.error("You can't use the 'egg will hatch' condition outside of a Player Egg Throw event.");
			return false;
		}
		setNegated(!parseResult.hasTag("will"));
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof PlayerEggThrowEvent))
			return false;
		return ((PlayerEggThrowEvent) event).isHatching() ^ isNegated();
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the egg " + (isNegated() ? "will" : "will not") + " hatch";
	}

}

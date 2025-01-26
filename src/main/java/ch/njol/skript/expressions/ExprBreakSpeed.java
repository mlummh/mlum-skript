package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Name("Block Break Speed")
@Description(
	"Gets the speed at which the given player would break this block, taking into account tools, potion effects, " +
	"whether or not the player is in water, enchantments, etc. The returned value is the amount of progress made in " +
	"breaking the block each tick. When the total breaking progress reaches 1.0, the block is broken. Note that the " +
	"break speed can change in the course of breaking a block, e.g. if a potion effect is applied or expires, or the " +
	"player jumps/enters water.")
@Examples({
	"on left click using diamond pickaxe:",
		"\tevent-block is set",
		"\tsend \"Break Speed: %break speed for player%\" to player"
})
@Since("2.7")
@RequiredPlugins("1.17+")
public class ExprBreakSpeed extends SimpleExpression<Float> {

	static {
		if (Skript.methodExists(Block.class, "getBreakSpeed", Player.class)) {
			Skript.registerExpression(ExprBreakSpeed.class, Float.class, ExpressionType.COMBINED,
				"[the] break speed[s] [of %blocks%] [for %players%]",
				"%block%'[s] break speed[s] [for %players%]"
			);
		}
	}

	private Expression<Block> blocks;
	private Expression<Player> players;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		blocks = (Expression<Block>) exprs[0];
		players = (Expression<Player>) exprs[1];
		return true;
	}

	@Override
	@Nullable
	protected Float[] get(Event event) {
		ArrayList<Float> speeds = new ArrayList<>();
		for (Block block : this.blocks.getArray(event)) {
			for (Player player : this.players.getArray(event)) {
				speeds.add(block.getBreakSpeed(player));
			}
		}

		return speeds.toArray(new Float[0]);
	}

	@Override
	public boolean isSingle() {
		return blocks.isSingle() && players.isSingle();
	}

	@Override
	public Class<? extends Float> getReturnType() {
		return Float.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "break speed of " + blocks.toString(event, debug) + " for " + players.toString(event, debug);
	}
}

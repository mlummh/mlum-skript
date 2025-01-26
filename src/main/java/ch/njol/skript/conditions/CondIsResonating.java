package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

@Name("Bell Is Resonating")
@Description({
	"Checks to see if a bell is currently resonating.",
	"A bell will start resonating five game ticks after being rung, and will continue to resonate for 40 game ticks."
})
@Examples("target block is resonating")
@RequiredPlugins("Spigot 1.19.4+")
@Since("2.9.0")
public class CondIsResonating extends PropertyCondition<Block> {

	static {
		if (Skript.classExists("org.bukkit.block.Bell") && Skript.methodExists(Bell.class, "isResonating"))
			register(CondIsResonating.class, "resonating", "blocks");
	}

	@Override
	public boolean check(Block value) {
		BlockState state = value.getState(false);
		return state instanceof Bell && ((Bell) state).isResonating();
	}

	@Override
	protected String getPropertyName() {
		return "resonating";
	}

}

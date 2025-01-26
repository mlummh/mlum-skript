package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;

@Name("Is Swimming")
@Description("Checks whether a living entity is swimming.")
@Examples("player is swimming")
@RequiredPlugins("1.13 or newer")
@Since("2.3")
public class CondIsSwimming extends PropertyCondition<LivingEntity> {
	
	static {
		if (Skript.methodExists(LivingEntity.class, "isSwimming"))
			register(CondIsSwimming.class, "swimming", "livingentities");
	}
	
	@Override
	public boolean check(final LivingEntity e) {
		return e.isSwimming();
	}
	
	@Override
	protected String getPropertyName() {
		return "swimming";
	}
	
}

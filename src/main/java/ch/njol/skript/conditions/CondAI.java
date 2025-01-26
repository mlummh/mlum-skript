package ch.njol.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;

@Name("Has AI")
@Description("Checks whether an entity has AI.")
@Examples("target entity has ai")
@Since("2.5")
public class CondAI extends PropertyCondition<LivingEntity> {
	
	static {
		register(CondAI.class, PropertyType.HAVE, "(ai|artificial intelligence)", "livingentities");
	}
	
	@Override
	public boolean check(LivingEntity entity) {
		return entity.hasAI();
	}
	
	@Override
	protected String getPropertyName() {
		return "artificial intelligence";
	}
	
}

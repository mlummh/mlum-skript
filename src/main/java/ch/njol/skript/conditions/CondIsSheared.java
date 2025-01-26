package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import io.papermc.paper.entity.Shearable;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent;

@Name("Entity Is Sheared")
@Description("Checks whether entities are sheared. This condition only works on cows, sheep and snowmen for versions below 1.19.4.")
@Examples({
	"if targeted entity of player is sheared:",
		"\tsend \"This entity has nothing left to shear!\" to player"
})
@Since("2.8.0")
@RequiredPlugins("MC 1.13+ (cows, sheep & snowmen), Paper 1.19.4+ (all shearable entities)")
public class CondIsSheared extends PropertyCondition<LivingEntity> {

	private static final boolean INTERFACE_METHOD = Skript.classExists("io.papermc.paper.entity.Shearable");

	static {
		register(CondIsSheared.class, "(sheared|shorn)", "livingentities");
	}

	@Override
	public boolean check(LivingEntity entity) {
		if (entity instanceof Cow) {
			return entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.SHEARED;
		} else if (INTERFACE_METHOD) {
			if (!(entity instanceof Shearable)) {
				return false;
			}
			return !((Shearable) entity).readyToBeSheared();
		} else if (entity instanceof Sheep) {
			return ((Sheep) entity).isSheared();
		} else if (entity instanceof Snowman) {
			return ((Snowman) entity).isDerp();
		}
		return false;
	}

	@Override
	protected String getPropertyName() {
		return "sheared";
	}

}

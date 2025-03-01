package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.conditions.base.PropertyCondition.PropertyType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Is Wearing")
@Description("Checks whether an entity is wearing some items (usually armor).")
@Examples({
	"player is wearing an iron chestplate and iron leggings",
	"player is wearing all diamond armour",
	"target is wearing wolf armor"
})
@Since("1.0")
public class CondIsWearing extends Condition {

	private static final boolean HAS_CAN_USE_SLOT_METHOD = Skript.methodExists(LivingEntity.class, "canUseEquipmentSlot", EquipmentSlot.class);
	private static final boolean HAS_BODY_SLOT = Skript.fieldExists(EquipmentSlot.class, "BODY");
	
	static {
		PropertyCondition.register(CondIsWearing.class, "wearing %itemtypes%", "livingentities");
	}
	
	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<LivingEntity> entities;
	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<ItemType> types;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(Expression<?>[] vars, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		entities = (Expression<LivingEntity>) vars[0];
		types = (Expression<ItemType>) vars[1];
		setNegated(matchedPattern == 1);
		return true;
	}
	
	@Override
	public boolean check(Event event) {
		ItemType[] cachedTypes = types.getAll(event);

		return entities.check(event, entity -> {
			EntityEquipment equipment = entity.getEquipment();
			if (equipment == null)
				return false; // spigot nullability, no identifier as to why this occurs

			ItemStack[] contents = Arrays.stream(EquipmentSlot.values())
				.filter(slot -> {
					// this method was added in 1.20.6
					if (HAS_CAN_USE_SLOT_METHOD)
						return entity.canUseEquipmentSlot(slot);

					// body slot was added in 1.20.5
					if (HAS_BODY_SLOT && slot == EquipmentSlot.BODY)
						// this may change in the future, but for now this is the only way to figure out
						// if the entity can use the body slot
						return entity instanceof Horse
							|| entity instanceof Wolf
							|| entity instanceof Llama;

					return true;
				})
				.map(equipment::getItem)
				.toArray(ItemStack[]::new);

			return SimpleExpression.check(cachedTypes, type -> {
				for (ItemStack content : contents) {
					if (type.isOfType(content) ^ type.isAll())
						return !type.isAll();
				}
				return type.isAll();
			}, false, false);
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return PropertyCondition.toString(this, PropertyType.BE, event, debug, entities,
				"wearing " + types.toString(event, debug));
	}
	
}

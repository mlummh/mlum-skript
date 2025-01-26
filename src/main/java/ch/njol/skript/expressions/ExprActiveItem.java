package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Active Item")
@Description(
	"Returns the item the entities are currently using (ie: the food they're eating, " +
	"the bow they're drawing back, etc.). This cannot be changed. " +
	"If an entity is not using any item, this will return null."
)
@Examples({
	"on damage of player:",
		"\tif victim's active tool is a bow:",
			"\t\tinterrupt player's active item use"
})
@Since("2.8.0")
@RequiredPlugins("Paper")
public class ExprActiveItem extends SimplePropertyExpression<LivingEntity, ItemStack> {

	static {
		if (Skript.methodExists(LivingEntity.class, "getActiveItem"))
			register(ExprActiveItem.class, ItemStack.class, "(raised|active) (tool|item|weapon)", "livingentities");
	}

	@Override
	@Nullable
	public ItemStack convert(LivingEntity livingEntity) {
		ItemStack item = livingEntity.getActiveItem();
		return item.getType() == Material.AIR ? null : item;
	}

	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@Override
	protected String getPropertyName() {
		return "active item";
	}

}

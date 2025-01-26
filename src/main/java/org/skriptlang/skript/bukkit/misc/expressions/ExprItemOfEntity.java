package org.skriptlang.skript.bukkit.misc.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.slot.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.Nullable;

@Name("Item of an Entity")
@Description({
	"An item associated with an entity. For dropped item entities, it gets the item that was dropped.",
	"For item frames, the item inside the frame is returned.",
	"For throwable projectiles (snowballs, enderpearls etc.) or item displays, it gets the displayed item.",
	"Other entities do not have items associated with them."
})
@Examples({
	"item of event-entity",
	"",
	"set the item inside of event-entity to a diamond sword named \"Example\""
})
@Since("2.2-dev35, 2.2-dev36 (improved), 2.5.2 (throwable projectiles), 2.10 (item displays)")
public class ExprItemOfEntity extends SimplePropertyExpression<Entity, Slot> {


	static {
		register(ExprItemOfEntity.class, Slot.class, "item [inside]", "entities");
	}

	@Override
	public @Nullable Slot convert(Entity entity) {
		if (entity instanceof ItemFrame itemFrame) {
			return new ItemFrameSlot(itemFrame);
		} else if (entity instanceof Item item) {
			return new DroppedItemSlot(item);
		} else if (entity instanceof ThrowableProjectile throwableProjectile) {
			return new ThrowableProjectileSlot(throwableProjectile);
		} else if (entity instanceof ItemDisplay itemDisplay) {
			return new DisplayEntitySlot(itemDisplay);
		}
		return null; // Other entities don't have associated items
	}

	@Override
	public Class<? extends Slot> getReturnType() {
		return Slot.class;
	}

	@Override
	protected String getPropertyName() {
		return "item inside";
	}

}

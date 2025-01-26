package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Arrow Knockback Strength")
@Description("An arrow's knockback strength.")
@Examples({"on shoot:",
	"\tevent-projectile is an arrow",
	"\tset arrow knockback strength of event-projectile to 10"})
@Since("2.5.1")
public class ExprArrowKnockbackStrength extends SimplePropertyExpression<Projectile, Long> {
	
	final static boolean abstractArrowExists = Skript.classExists("org.bukkit.entity.AbstractArrow");
	
	static {
		register(ExprArrowKnockbackStrength.class, Long.class, "arrow knockback strength", "projectiles");
	}
	
	@Nullable
	@Override
	public Long convert(Projectile arrow) {
		if (abstractArrowExists)
			return arrow instanceof AbstractArrow ? (long) ((AbstractArrow) arrow).getKnockbackStrength() : null;
		return arrow instanceof Arrow ? (long) ((Arrow) arrow).getKnockbackStrength() : null;
	}
	
	@Nullable
	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		switch (mode) {
			case SET:
			case ADD:
			case RESET:
			case REMOVE:
				return CollectionUtils.array(Number.class);
			default:
				return null;
		}
	}
	
	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		int strength = delta != null ? ((Number) delta[0]).intValue() : 0;
		switch (mode) {
			case REMOVE:
				if (abstractArrowExists) {
					for (Projectile entity : getExpr().getArray(e)) {
						if (entity instanceof AbstractArrow) {
							AbstractArrow abstractArrow = (AbstractArrow) entity;
							int dmg = abstractArrow.getKnockbackStrength() - strength;
							if (dmg < 0) dmg = 0;
							abstractArrow.setKnockbackStrength(dmg);
						}
					}
				} else {
					for (Projectile entity : getExpr().getArray(e)) {
						if (entity instanceof Arrow) {
							Arrow arrow = (Arrow) entity;
							int dmg = arrow.getKnockbackStrength() - strength;
							if (dmg < 0) return;
							arrow.setKnockbackStrength(dmg);
						}
					}
				}
				break;
			case ADD:
				if (abstractArrowExists)
					for (Projectile entity : getExpr().getArray(e)) {
						if (entity instanceof AbstractArrow) {
							AbstractArrow abstractArrow = (AbstractArrow) entity;
							int dmg = abstractArrow.getKnockbackStrength() + strength;
							if (dmg < 0) return;
							abstractArrow.setKnockbackStrength(dmg);
						}
					}
				else
					for (Projectile entity : getExpr().getArray(e)) {
						if (entity instanceof Arrow) {
							Arrow arrow = (Arrow) entity;
							int dmg = arrow.getKnockbackStrength() + strength;
							if (dmg < 0) return;
							arrow.setKnockbackStrength(dmg);
						}
					}
				break;
			case RESET:
			case SET:
				for (Projectile entity : getExpr().getArray(e)) {
					if (abstractArrowExists) {
						if (entity instanceof AbstractArrow) ((AbstractArrow) entity).setKnockbackStrength(strength);
					} else if (entity instanceof Arrow) {
						((Arrow) entity).setKnockbackStrength(strength);
					}
				}
				break;
			default:
				assert false;
		}
	}
	
	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "projectile knockback strength";
	}
	
}

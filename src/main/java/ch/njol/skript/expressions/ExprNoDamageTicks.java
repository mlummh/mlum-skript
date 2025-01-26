package ch.njol.skript.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("No Damage Ticks")
@Description("The number of ticks that an entity is invulnerable to damage for.")
@Examples({"on damage:",
		"	set victim's invulnerability ticks to 20 #Victim will not take damage for the next second"})
@Since("2.5")
public class ExprNoDamageTicks extends SimplePropertyExpression<LivingEntity, Long> {
	
	static {
		register(ExprNoDamageTicks.class, Long.class, "(invulnerability|invincibility|no damage) tick[s]", "livingentities");
	}

	@Override
	public Long convert(LivingEntity e) {
		return (long) e.getNoDamageTicks();
	}
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.REMOVE_ALL)
			return null;
		return CollectionUtils.array(Number.class);
	}
	
	@Override
	public void change(final Event e, final @Nullable Object[] delta, final ChangeMode mode) {
		int d = delta == null ? 0 : ((Number) delta[0]).intValue();
		for (LivingEntity le : getExpr().getArray(e)) {
			switch (mode) {
				case ADD:
					int r1 = le.getNoDamageTicks() + d;
					if (r1 < 0) r1 = 0;
					le.setNoDamageTicks(r1);
					break;
				case SET:
					le.setNoDamageTicks(d);
					break;
				case DELETE:
				case RESET:
					le.setNoDamageTicks(0);
					break;
				case REMOVE:
					int r2 = le.getNoDamageTicks() - d;
					if (r2 < 0) r2 = 0;
					le.setNoDamageTicks(r2);
					break;
				case REMOVE_ALL:
					assert false;		
			}
		}
	}
	
	@Override
	protected String getPropertyName() {
		return "no damage ticks";
	}
	
	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}
	
}

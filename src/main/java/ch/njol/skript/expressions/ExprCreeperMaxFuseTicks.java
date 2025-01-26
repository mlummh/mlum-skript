package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Creeper Max Fuse Ticks")
@Description("The max fuse ticks that a creeper has.")
@Examples("set target entity's max fuse ticks to 20 #1 second")
@Since("2.5")
public class ExprCreeperMaxFuseTicks extends SimplePropertyExpression<LivingEntity, Long> {
	
	static {
		if(Skript.methodExists(LivingEntity.class, "getMaxFuseTicks"))
			register(ExprCreeperMaxFuseTicks.class, Long.class, "[creeper] max[imum] fuse tick[s]", "livingentities");
	}

	@Override
	public Long convert(LivingEntity e) {
		return e instanceof Creeper ? (long) ((Creeper) e).getMaxFuseTicks() : 0;
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
			if (le instanceof Creeper) {
				Creeper c = (Creeper) le;
				switch (mode) {
					case ADD:
						int r1 = c.getMaxFuseTicks() + d;
						if (r1 < 0) r1 = 0;
						c.setMaxFuseTicks(r1);
						break;
					case SET:
						c.setMaxFuseTicks(d);
						break;
					case DELETE:
						c.setMaxFuseTicks(0);
						break;
					case RESET:
						c.setMaxFuseTicks(30); //Seems to be the same for powered creepers?
						break;
					case REMOVE:
						int r2 = c.getMaxFuseTicks() - d;
						if (r2 < 0) r2 = 0;
						c.setMaxFuseTicks(r2);
						break;
					case REMOVE_ALL:
						assert false;		
				}
			}
		}
	}

	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}

	@Override
	protected String getPropertyName() {
		return "creeper max fuse ticks";
	}
	
}

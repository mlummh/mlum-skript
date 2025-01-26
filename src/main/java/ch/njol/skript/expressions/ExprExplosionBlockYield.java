package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.Nullable;

@Name("Explosion Block Yield")
@Description({"The percentage of exploded blocks dropped in an explosion event.",
				"When changing the yield, a value greater than 1 will function the same as using 1.",
				"Attempting to change the yield to a value less than 0 will have no effect."})
@Examples({"on explode:",
			"set the explosion's block yield to 10%"})
@Events("explosion")
@Since("2.5")
public class ExprExplosionBlockYield extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprExplosionBlockYield.class, Number.class, ExpressionType.PROPERTY,
				"[the] [explosion['s]] block (yield|amount)",
				"[the] percentage of blocks dropped"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(EntityExplodeEvent.class)) {
			Skript.error("The 'explosion block yield' is only usable in an explosion event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	@Nullable
	protected Number[] get(Event e) {
		if (!(e instanceof EntityExplodeEvent))
			return null;

		return new Number[]{((EntityExplodeEvent) e).getYield()};
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(final ChangeMode mode) {
		switch (mode) {
			case SET:
			case ADD:
			case REMOVE:
			case DELETE:
				return CollectionUtils.array(Number.class);
			default:
				return null;
		}
	}

	@Override
	public void change(final Event event, final @Nullable Object[] delta, final ChangeMode mode) {
		float n = delta == null ? 0 : ((Number) delta[0]).floatValue();
		if (n < 0 || !(event instanceof EntityExplodeEvent)) // Yield can't be negative
			return;
		EntityExplodeEvent e = (EntityExplodeEvent) event;
		// Yield can be a value from 0 to 1
		switch (mode) {
			case SET:
				e.setYield(n);
				break;
			case ADD:
				float add = e.getYield() + n;
				if (add < 0)
					return;
				e.setYield(add);
				break;
			case REMOVE:
				float subtract = e.getYield() - n;
				if (subtract < 0)
					return;
				e.setYield(subtract);
				break;
			case DELETE:
				e.setYield(0);
				break;
			default:
				assert false;
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "the explosion's block yield";
	}

}

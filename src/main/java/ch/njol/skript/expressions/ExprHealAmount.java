
package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.Nullable;

@Name("Heal Amount")
@Description("The amount of health healed in a <a href='/events.html#heal'>heal event</a>.")
@Examples({
	"on player healing:",
		"\tincrease the heal amount by 2",
		"\tremove 0.5 from the healing amount"
})
@Events("heal")
@Since("2.5.1")
public class ExprHealAmount extends SimpleExpression<Double> {

	static {
		Skript.registerExpression(ExprHealAmount.class, Double.class, ExpressionType.SIMPLE, "[the] heal[ing] amount");
	}

	private Kleenean delay;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(EntityRegainHealthEvent.class)) {
			Skript.error("The expression 'heal amount' may only be used in a healing event");
			return false;
		}
		delay = isDelayed;
		return true;
	}

	@Nullable
	@Override
	protected Double[] get(Event event) {
		if (!(event instanceof EntityRegainHealthEvent))
			return null;
		return new Double[]{((EntityRegainHealthEvent) event).getAmount()};
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (delay != Kleenean.FALSE) {
			Skript.error("The heal amount cannot be changed after the event has already passed");
			return null;
		}
		if (mode == Changer.ChangeMode.REMOVE_ALL || mode == Changer.ChangeMode.RESET)
			return null;
		return CollectionUtils.array(Number.class);
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (!(event instanceof EntityRegainHealthEvent))
			return;

		EntityRegainHealthEvent healthEvent = (EntityRegainHealthEvent) event;
		double value = delta == null ? 0 : ((Number) delta[0]).doubleValue();
		switch (mode) {
			case SET:
			case DELETE:
				healthEvent.setAmount(value);
				break;
			case ADD:
				healthEvent.setAmount(healthEvent.getAmount() + value);
				break;
			case REMOVE:
				healthEvent.setAmount(healthEvent.getAmount() - value);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean setTime(int time) {
		return super.setTime(time, EntityRegainHealthEvent.class);
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Double> getReturnType() {
		return Double.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "heal amount";
	}

}

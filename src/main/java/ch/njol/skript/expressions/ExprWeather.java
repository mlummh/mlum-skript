package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.WeatherType;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter Güttinger
 */
@Name("Weather")
@Description("The weather in the given or the current world.")
@Examples({"set weather to clear",
		"weather in \"world\" is rainy"})
@Since("1.0")
@Events("weather change")
public class ExprWeather extends PropertyExpression<World, WeatherType> {
	static {
		Skript.registerExpression(ExprWeather.class, WeatherType.class, ExpressionType.PROPERTY, "[the] weather [(in|of) %worlds%]", "%worlds%'[s] weather");
	}

	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		setExpr((Expression<World>) exprs[0]);
		return true;
	}

	@Override
	protected WeatherType[] get(Event event, World[] source) {
		return get(source, world -> {
			if (getTime() >= 0 && event instanceof WeatherEvent weatherEvent
				&& world.equals(weatherEvent.getWorld()) && !Delay.isDelayed(event))
				return WeatherType.fromEvent(weatherEvent);
			else
				return WeatherType.fromWorld(world);
		});
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the weather in " + getExpr().toString(e, debug);
	}
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.DELETE || mode == ChangeMode.SET)
			return CollectionUtils.array(WeatherType.class);
		return null;
	}
	
	@Override
	public void change(final Event e, final @Nullable Object[] delta, final ChangeMode mode) {
		final WeatherType t = delta == null ? WeatherType.CLEAR : (WeatherType) delta[0];
		for (final World w : getExpr().getArray(e)) {
			assert w != null : getExpr();
			if (getTime() >= 0 && e instanceof WeatherEvent && w.equals(((WeatherEvent) e).getWorld()) && !Delay.isDelayed(e)) {
				if (e instanceof WeatherChangeEvent) {
					if (((WeatherChangeEvent) e).toWeatherState() && t == WeatherType.CLEAR)
						((WeatherChangeEvent) e).setCancelled(true);
					if (((WeatherChangeEvent) e).getWorld().isThundering() != (t == WeatherType.THUNDER))
						((WeatherChangeEvent) e).getWorld().setThundering(t == WeatherType.THUNDER);
				} else if (e instanceof ThunderChangeEvent) {
					if (((ThunderChangeEvent) e).toThunderState() && t != WeatherType.THUNDER)
						((ThunderChangeEvent) e).setCancelled(true);
					if (((ThunderChangeEvent) e).getWorld().hasStorm() == (t == WeatherType.CLEAR))
						((ThunderChangeEvent) e).getWorld().setStorm(t != WeatherType.CLEAR);
				}
			} else {
				t.setWeather(w);
			}
		}
	}
	
	@Override
	public Class<WeatherType> getReturnType() {
		return WeatherType.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean setTime(final int time) {
		return super.setTime(time, getExpr(), WeatherChangeEvent.class, ThunderChangeEvent.class);
	}
	
}

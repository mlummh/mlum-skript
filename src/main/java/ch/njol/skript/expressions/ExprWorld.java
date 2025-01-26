package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter Güttinger
 */
@Name("World")
@Description("The world the event occurred in.")
@Examples({"world is \"world_nether\"",
		"teleport the player to the world's spawn",
		"set the weather in the player's world to rain",
		"set {_world} to world of event-chunk"})
@Since("1.0")
public class ExprWorld extends PropertyExpression<Object, World> {

	static {
		Skript.registerExpression(ExprWorld.class, World.class, ExpressionType.PROPERTY, "[the] world [of %locations/entities/chunk%]", "%locations/entities/chunk%'[s] world");
	}
	
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		Expression<?> expr = exprs[0];
		if (expr == null) {
			expr = new EventValueExpression<>(World.class);
			if (!((EventValueExpression<?>) expr).init())
				return false;
		}
		setExpr(expr);
		return true;
	}
	
	@Override
	protected World[] get(final Event e, final Object[] source) {
		if (source instanceof World[]) // event value (see init)
			return (World[]) source;
		return get(source, obj -> {
			if (obj instanceof Entity) {
				if (getTime() > 0 && e instanceof PlayerTeleportEvent && obj.equals(((PlayerTeleportEvent) e).getPlayer()) && !Delay.isDelayed(e))
					return ((PlayerTeleportEvent) e).getTo().getWorld();
				else
					return ((Entity) obj).getWorld();
			} else if (obj instanceof Location) {
				return ((Location) obj).getWorld();
			} else if (obj instanceof Chunk) {
				return ((Chunk) obj).getWorld();
			}
			assert false : obj;
			return null;
		});
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return CollectionUtils.array(World.class);
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (delta == null)
			return;

		for (Object o : getExpr().getArray(e)) {
			if (o instanceof Location) {
				((Location) o).setWorld((World) delta[0]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean setTime(final int time) {
		return super.setTime(time, getExpr(), PlayerTeleportEvent.class);
	}

	@Override
	public Class<World> getReturnType() {
		return World.class;
	}

	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the world" + (getExpr().isDefault() ? "" : " of " + getExpr().toString(e, debug));
	}

}

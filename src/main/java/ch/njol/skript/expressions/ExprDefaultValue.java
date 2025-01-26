package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

import java.lang.reflect.Array;

@Name("Default Value")
@Description("A shorthand expression for giving things a default value. If the first thing isn't set, the second thing will be returned.")
@Examples({"broadcast {score::%player's uuid%} otherwise \"%player% has no score!\""})
@Since("2.2-dev36")
@SuppressWarnings("null")
public class ExprDefaultValue<T> extends SimpleExpression<T> {

	static {
		Skript.registerExpression(ExprDefaultValue.class, Object.class, ExpressionType.COMBINED,
				"%objects% (otherwise|?) %objects%");
	}

	private final ExprDefaultValue<?> source;
	private final Class<? extends T>[] types;
	private final Class<T> superType;
	@Nullable
	private Expression<Object> first;
	@Nullable
	private Expression<Object> second;

	@SuppressWarnings("unchecked")
	public ExprDefaultValue() {
		this(null, (Class<? extends T>) Object.class);
	}

	@SuppressWarnings("unchecked")
	private ExprDefaultValue(ExprDefaultValue<?> source, Class<? extends T>... types) {
		this.source = source;
		if (source != null) {
			this.first = source.first;
			this.second = source.second;
		}
		this.types = types;
		this.superType = (Class<T>) Utils.getSuperType(types);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		first = LiteralUtils.defendExpression(exprs[0]);
		second = LiteralUtils.defendExpression(exprs[1]);
		return LiteralUtils.canInitSafely(first, second);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T[] get(Event e) {
		Object[] first = this.first.getArray(e);
		Object values[] = first.length != 0 ? first : second.getArray(e);
		try {
			return Converters.convert(values, types, superType);
		} catch (ClassCastException e1) {
			return (T[]) Array.newInstance(superType, 0);
		}
	}

	@Override
	public <R> Expression<? extends R> getConvertedExpression(Class<R>... to) {
		return new ExprDefaultValue<>(this, to);
	}

	@Override
	public Expression<?> getSource() {
		return source == null ? this : source;
	}

	@Override
	public Class<? extends T> getReturnType() {
		return superType;
	}

	@Override
	public boolean isSingle() {
		return first.isSingle() && second.isSingle();
	}

	@Override
	public String toString(Event e, boolean debug) {
		return first.toString(e, debug) + " or else " + second.toString(e, debug);
	}

}

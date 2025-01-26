package ch.njol.skript.lang;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.condition.Conditional;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.util.Priority;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * A condition which must be fulfilled for the trigger to continue. If the condition is in a section the behaviour depends on the section.
 *
 * @see Skript#registerCondition(Class, String...)
 */
public abstract class Condition extends Statement implements Conditional<Event> {

	public enum ConditionType {
		/**
		 * Conditions that contain other expressions, e.g. "%properties% is/are within %expressions%"
		 * 
		 * @see #PROPERTY
		 */
		COMBINED(SyntaxInfo.COMBINED),

		/**
		 * Property conditions, e.g. "%properties% is/are data value[s]"
		 */
		PROPERTY(PropertyCondition.DEFAULT_PRIORITY),

		/**
		 * Conditions whose pattern matches (almost) everything or should be last checked.
		 */
		PATTERN_MATCHES_EVERYTHING(SyntaxInfo.PATTERN_MATCHES_EVERYTHING);

		@ApiStatus.Experimental
		private final Priority priority;

		@ApiStatus.Experimental
		ConditionType(Priority priority) {
			this.priority = priority;
		}

		/**
		 * @return The Priority equivalent of this ConditionType.
		 */
		@ApiStatus.Experimental
		public Priority priority() {
			return this.priority;
		}

	}

	private boolean negated;

	protected Condition() {}

	/**
	 * Checks whether this condition is satisfied with the given event. This should not alter the event or the world in any way, as conditions are only checked until one returns
	 * false. All subsequent conditions of the same trigger will then be omitted.<br/>
	 * <br/>
	 * You might want to use {@link SimpleExpression#check(Event, Predicate)}
	 *
	 * @param event the event to check
	 * @return <code>true</code> if the condition is satisfied, <code>false</code> otherwise or if the condition doesn't apply to this event.
	 */
	public abstract boolean check(Event event);

	@Override
	public Kleenean evaluate(Event event) {
		return Kleenean.get(check(event));
	}

	@Override
	public final boolean run(Event event) {
		return check(event);
	}

	/**
	 * Sets the negation state of this condition. This will change the behaviour of {@link Expression#check(Event, Predicate, boolean)}.
	 */
	protected final void setNegated(boolean invert) {
		negated = invert;
	}

	/**
	 * @return whether this condition is negated or not.
	 */
	public final boolean isNegated() {
		return negated;
	}

	@Override
	public @NotNull String getSyntaxTypeName() {
		return "condition";
	}

	/**
	 * Parse a raw string input as a condition.
	 * 
	 * @param input The string input to parse as a condition.
	 * @param defaultError The error if the condition fails.
	 * @return Condition if parsed correctly, otherwise null.
	 */
	public static @Nullable Condition parse(String input, @Nullable String defaultError) {
		input = input.trim();
		while (input.startsWith("(") && SkriptParser.next(input, 0, ParseContext.DEFAULT) == input.length())
			input = input.substring(1, input.length() - 1);
		//noinspection unchecked,rawtypes
		return (Condition) SkriptParser.parse(input, (Iterator) Skript.getConditions().iterator(), defaultError);
	}

}

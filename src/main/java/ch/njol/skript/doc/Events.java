package ch.njol.skript.doc;

import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptEventInfo;

import java.lang.annotation.*;

/**
 * @author Peter Güttinger
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Events {
	/**
	 * A list of {@link SkriptEventInfo#getName() name(s)} of {@link SkriptEvent events} this expression is useful for.
	 */
	String[] value();
}

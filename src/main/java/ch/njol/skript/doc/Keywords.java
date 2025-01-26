package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides a list of keywords that will allow for easier documentation searching.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Keywords {
	
	public String[] value();
}

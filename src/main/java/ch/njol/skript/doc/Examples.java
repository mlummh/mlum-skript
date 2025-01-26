package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides a list of examples to be used in documentation for annotated
 * element.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Examples {
	
	public String[] value();
}

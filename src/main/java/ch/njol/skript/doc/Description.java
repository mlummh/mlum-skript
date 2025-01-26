package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides a description for annotated element in documentation.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Description {
	
	public String[] value();
}

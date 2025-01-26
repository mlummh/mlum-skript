package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides the name of annotated element to be used in documentation.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Name {
	
	public String value();
}

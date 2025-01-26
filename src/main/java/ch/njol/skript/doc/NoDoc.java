package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Annotated element will not appear in documentation, nor will missing
 * documentation about it cause warnings.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoDoc {
	
}

package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Sets documentation id for the annotated element. This is optional;
 * if no id is specified, Skript will create one.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DocumentationId {

	public String value();

}

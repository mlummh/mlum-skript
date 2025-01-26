package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides a list of plugins other than Skript that the annotated
 * element requires to be used. Non-Spigot server software can be considered
 * to be plugins.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPlugins {

	String[] value();

}

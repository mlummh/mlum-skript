package ch.njol.skript.doc;

import java.lang.annotation.*;

/**
 * Provides the version of plugin when was the annotated element added.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Since {
	
	public String[] value();
}

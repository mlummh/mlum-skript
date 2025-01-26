package ch.njol.yggdrasil;

import java.lang.annotation.*;

/**
 * Can be used to set a class's or field's id used by Yggdrasil.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface YggdrasilID {
	
	String value();
	
}

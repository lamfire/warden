package com.lamfire.warden.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface ACTION {
	public abstract String path();
    public abstract boolean singleton() default false;
    public abstract boolean enableBoundParameters() default false;
}

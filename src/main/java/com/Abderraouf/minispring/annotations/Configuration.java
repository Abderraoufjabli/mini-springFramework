package com.Abderraouf.minispring.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Component
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface Configuration {
    String value() default "";
}

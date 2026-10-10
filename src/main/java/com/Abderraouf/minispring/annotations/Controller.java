package com.Abderraouf.minispring.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({
        java.lang.annotation.ElementType.TYPE,
        java.lang.annotation.ElementType.ANNOTATION_TYPE})
public @interface Controller {
    String value() default "";
}

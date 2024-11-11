package com.vaynerakawalo.springobservability.logging.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Egress {

    String method() default "unknown";

    String service() default "unknown";
}

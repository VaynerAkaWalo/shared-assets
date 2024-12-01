package com.vaynerakawalo.springobservability.logging.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Egress {

    String NOT_SET = "unknown";

    String method() default NOT_SET;

    String service() default NOT_SET;
}

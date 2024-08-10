package com.vaynerakawalo.springobservability.logging;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.util.Assert;

import java.util.Optional;

public interface ThreadContextProperty {
    String EMPTY = "";
    String TRACE = "TRACE";
    String START_TIME = "START_TIME";


    static String getValue(String key){
        Assert.notNull(key, "Thread context property key cannot be null");
        return Optional.ofNullable(ThreadContext.get(key))
                .orElse(EMPTY);
    }
}

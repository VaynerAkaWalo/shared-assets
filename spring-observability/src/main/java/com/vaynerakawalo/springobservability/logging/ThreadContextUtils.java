package com.vaynerakawalo.springobservability.logging;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import org.apache.logging.log4j.ThreadContext;

public class ThreadContextUtils {

    public static void put(ThreadContextProperty property, String value) {
        ThreadContext.put(property.name(), value);
    }

    public static void put(ThreadContextProperty property, int value) {
        ThreadContext.put(property.name(), String.valueOf(value));
    }

    public static void put(ThreadContextProperty property, long value) {
        ThreadContext.put(property.name(), String.valueOf(value));
    }

    public static String get(ThreadContextProperty property) {
        return ThreadContext.get(property.name());
    }
}

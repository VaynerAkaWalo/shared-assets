package com.vaynerakawalo.springobservability.logging.model;

import lombok.Getter;
import org.apache.logging.log4j.ThreadContext;

public enum ThreadContextProperty {
    TYPE("type", true),
    START_TIME("start_time", false),
    TOTAL_DURATION("total_duration", false),
    OUTCOME("outcome", true),
    STATUS_CODE("status_code", false),
    CAUSE("cause", false),
    ERROR("error", false),
    TRACE("trace_id", false);

    private static final String UNKNOWN = "unknown";
    private static final String EMPTY = "";

    @Getter
    private final String displayName;
    private final boolean unknownIfNull;

    ThreadContextProperty(String name, boolean skipIfNull) {
        this.displayName = name;
        this.unknownIfNull = skipIfNull;
    }

    public String getString() {
        var value = ThreadContext.get(displayName);
        if (value != null) {
            return value;
        }

        return unknownIfNull ? UNKNOWN : EMPTY;
    }
}

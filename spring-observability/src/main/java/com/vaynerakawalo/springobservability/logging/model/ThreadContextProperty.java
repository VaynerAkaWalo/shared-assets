package com.vaynerakawalo.springobservability.logging.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

public enum ThreadContextProperty {
    TYPE("type", true),
    START_TIME("start_time", false),
    TOTAL_DURATION("total_duration", false),
    OUTCOME("outcome", true),
    STATUS_CODE("status_code", false),
    CAUSE("cause", false),
    ERROR("error", false),
    URL("url", false),
    TARGET_URL("target_url", false),
    TRACE("trace_id", false),
    TARGET_SERVICE("service", false),
    METHOD("method", false);

    private static final String UNKNOWN = "unknown";
    private static final String EMPTY = "";

    private final String displayName;
    private final boolean unknownIfNull;

    ThreadContextProperty(String name, boolean skipIfNull) {
        this.displayName = name;
        this.unknownIfNull = skipIfNull;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getValue() {
        var value = ThreadContext.get(name());
        if (value != null) {
            return value;
        }

        return unknownIfNull ? UNKNOWN : EMPTY;
    }

    public Long getNumericValue() {
        if (!StringUtils.isNumeric(getValue())) {
            return null;
        }
        return switch (this) {
            case START_TIME, TOTAL_DURATION, STATUS_CODE -> Long.parseLong(getValue());
            default -> 0L;
        };
    }

    public void putString(String value) {
        ThreadContext.put(name(), value);
    }

    public void putInteger(Integer value) {
        ThreadContext.put(name(), String.valueOf(value));
    }

    public void putLong(Long value) {
        ThreadContext.put(name(), String.valueOf(value));
    }
}

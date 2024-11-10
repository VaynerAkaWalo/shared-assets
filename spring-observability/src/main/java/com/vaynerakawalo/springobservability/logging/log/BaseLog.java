package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.MapMessage;

import java.util.HashMap;
import java.util.Map;

public class BaseLog {
    protected static final String CONTEXT = "context";
    protected static final String METRICS = "metrics";
    protected static final String RESULT = "result";
    protected static final String TRACING = "tracing";

    private final Map<String, Object> logMap;

    public BaseLog() {
        this.logMap = new HashMap<>();

        putProperty(ThreadContextProperty.TYPE);
        nestPropertyIn(METRICS, ThreadContextProperty.TOTAL_DURATION);
        nestPropertyIn(RESULT, ThreadContextProperty.OUTCOME);
        nestPropertyIn(RESULT, ThreadContextProperty.CAUSE);
        nestPropertyIn(RESULT, ThreadContextProperty.ERROR);
        nestPropertyIn(RESULT, ThreadContextProperty.STATUS_CODE);
        nestPropertyIn(TRACING, ThreadContextProperty.TRACE);
    }

    protected void putProperty(ThreadContextProperty property) {
        if (StringUtils.isEmpty(property.getString())) {
            return;
        }

        logMap.put(property.name(), property.getString());
    }

    protected void nestPropertyIn(String parent, ThreadContextProperty child) {
        if (StringUtils.isEmpty(child.getString())) {
            return;
        }

        var parentMap = getPropertyMap(parent);
        parentMap.put(child.name(), child.getString());
    }

    private Map<String, Object> getPropertyMap(String mapName) {
        logMap.computeIfAbsent(mapName, k -> new HashMap<>());

        return (Map<String, Object>) logMap.get(mapName);
    }

    public MapMessage mapMessage() {
        return new MapMessage(logMap);
    }
}

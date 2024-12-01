package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MapMessage;

import java.util.HashMap;
import java.util.Map;

public class BasicOperationLog {
    protected static final String CONTEXT = "context";
    protected static final String METRICS = "metrics";
    protected static final String RESULT = "result";
    protected static final String TRACING = "tracing";

    private final Map<String, Object> logMap;
    private final Logger log = LogManager.getLogger("OperationLogger");

    public BasicOperationLog() {
        this.logMap = new HashMap<>();
        logMap.put(ThreadContextProperty.TYPE.getDisplayName(), getType());
        nestPropertyIn(RESULT, ThreadContextProperty.OUTCOME);
        nestPropertyIn(RESULT, ThreadContextProperty.CAUSE);
        nestPropertyIn(RESULT, ThreadContextProperty.ERROR);
        nestNumericPropertyIn(METRICS, ThreadContextProperty.TOTAL_DURATION);
        nestPropertyIn(TRACING, ThreadContextProperty.TRACE);
    }

    protected void putProperty(ThreadContextProperty property) {
        if (StringUtils.isEmpty(property.getValue())) {
            return;
        }

        logMap.put(property.getDisplayName(), property.getValue());
    }

    protected void nestPropertyIn(String parent, ThreadContextProperty child) {
        if (StringUtils.isEmpty(child.getValue())) {
            return;
        }

        var parentMap = getPropertyMap(parent);
        parentMap.put(child.getDisplayName(), child.getValue());
    }

    protected void nestNumericPropertyIn(String parent, ThreadContextProperty child) {
        var value = child.getNumericValue();

        if (value == null) {
            return;
        }

        var parentMap = getPropertyMap(parent);
        parentMap.put(child.getDisplayName(), value);
    }

    private Map<String, Object> getPropertyMap(String mapName) {
        logMap.computeIfAbsent(mapName, k -> new HashMap<>());

        return (Map<String, Object>) logMap.get(mapName);
    }

    public void log() {
        log.info(new MapMessage<>(logMap));
    }

    protected Type getType() {
        return Type.UNKNOWN;
    }
}

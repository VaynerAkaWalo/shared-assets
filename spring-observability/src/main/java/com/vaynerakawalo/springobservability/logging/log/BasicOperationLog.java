package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.MapMessage;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class BasicOperationLog {
    protected static final String CONTEXT = "context";
    protected static final String METRICS = "metrics";
    protected static final String RESULT = "result";
    protected static final String TRACING = "tracing";

    private final Map<String, Object> logMap;

    public BasicOperationLog() {
        this.logMap = new HashMap<>();
        putProperty(ThreadContextProperty.TYPE);
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
}

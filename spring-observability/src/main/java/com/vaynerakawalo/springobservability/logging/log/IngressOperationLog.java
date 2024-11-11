package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;

public class IngressOperationLog extends BasicOperationLog {

    public IngressOperationLog() {
        super();
        putProperty(ThreadContextProperty.URL);
        nestNumericPropertyIn(METRICS, ThreadContextProperty.TOTAL_DURATION);
        nestNumericPropertyIn(RESULT, ThreadContextProperty.STATUS_CODE);
    }
}

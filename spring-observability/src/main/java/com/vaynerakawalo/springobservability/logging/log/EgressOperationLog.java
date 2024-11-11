package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;

public class EgressOperationLog extends BasicOperationLog {
    private static final String TARGET = "target";

    public EgressOperationLog() {
        super();
        nestNumericPropertyIn(RESULT, ThreadContextProperty.STATUS_CODE);
        nestPropertyIn(TARGET, ThreadContextProperty.TARGET_SERVICE);
        nestPropertyIn(TARGET, ThreadContextProperty.URL);
    }
}

package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;

public class EgressOperationLog extends BasicOperationLog {
    private static final String TARGET = "target";

    public EgressOperationLog() {
        super();
        nestNumericPropertyIn(RESULT, ThreadContextProperty.STATUS_CODE);
        nestPropertyIn(TARGET, ThreadContextProperty.TARGET_SERVICE);
        nestPropertyIn(TARGET, ThreadContextProperty.TARGET_URL);
    }

    @Override
    protected Type getType() {
        return Type.EGRESS;
    }
}

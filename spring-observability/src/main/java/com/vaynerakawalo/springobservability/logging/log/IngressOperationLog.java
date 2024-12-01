package com.vaynerakawalo.springobservability.logging.log;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;

public class IngressOperationLog extends BasicOperationLog {

    public IngressOperationLog() {
        super();
        putProperty(ThreadContextProperty.URL);
        nestNumericPropertyIn(METRICS, ThreadContextProperty.TOTAL_DURATION);
        nestNumericPropertyIn(RESULT, ThreadContextProperty.STATUS_CODE);
    }

    @Override
    protected Type getType() {
        return Type.INGRESS;
    }
}

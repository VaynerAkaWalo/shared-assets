package com.vaynerakawalo.springobservability.logging;

import com.vaynerakawalo.springobservability.logging.log.BaseLog;
import com.vaynerakawalo.springobservability.logging.model.Outcome;
import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public class ThreadContextPopulationInterceptor implements HandlerInterceptor {
    private static final String TRACE_HEADER = "X-TRACE-ID";
    private final Clock clock;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var trace = setTraceIfAbsent(request, response);

        ThreadContextProperty.TYPE.putString(Type.INGRESS.name());
        ThreadContextProperty.TRACE.putString(trace);
        ThreadContextProperty.START_TIME.putLong(clock.millis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            ThreadContextProperty.STATUS_CODE.putInteger(response.getStatus());
            var totalDuration = Duration.between(getStartTime(), clock.instant());
            ThreadContextProperty.TOTAL_DURATION.putLong(totalDuration.toMillis());
            if (ex != null) {
                ThreadContextProperty.OUTCOME.putString(Outcome.ERROR.name());
                ThreadContextProperty.ERROR.putString(ex.getClass().getSimpleName());
                var rootCause = ExceptionUtils.getRootCause(ex);
                ThreadContextProperty.CAUSE.putString(rootCause.getClass().getSimpleName());
            } else {
                ThreadContextProperty.OUTCOME.putString(Outcome.SUCCESS.name());
            }
        } finally {
            new BaseLog().log();
            ThreadContext.clearAll();
        }
    }

    private String setTraceIfAbsent(HttpServletRequest request, HttpServletResponse response) {
        String trace = request.getHeader(TRACE_HEADER);
        if (StringUtils.isEmpty(trace)) {
            trace = generateUUID();
        }

        response.setHeader(TRACE_HEADER, trace);
        return trace;
    }

    private Instant getStartTime() {
        return Instant.ofEpochMilli(ThreadContextProperty.START_TIME.getNumericValue());
    }

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

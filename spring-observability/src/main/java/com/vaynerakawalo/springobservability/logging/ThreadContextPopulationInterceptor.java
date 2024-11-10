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

        ThreadContextUtils.put(ThreadContextProperty.TYPE, Type.INGRESS.name());
        ThreadContextUtils.put(ThreadContextProperty.TRACE, trace);
        ThreadContextUtils.put(ThreadContextProperty.START_TIME, clock.millis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            ThreadContextUtils.put(ThreadContextProperty.STATUS_CODE, response.getStatus());
            var totalDuration = Duration.between(getStartTime(), clock.instant());
            ThreadContextUtils.put(ThreadContextProperty.TOTAL_DURATION, totalDuration.toMillis());
            if (ex != null) {
                ThreadContextUtils.put(ThreadContextProperty.OUTCOME, Outcome.ERROR.name());
                ThreadContextUtils.put(ThreadContextProperty.ERROR, ex.getClass().getSimpleName());
                var rootCause = ExceptionUtils.getRootCause(ex);
                ThreadContextUtils.put(ThreadContextProperty.CAUSE, rootCause.getClass().getSimpleName());
            } else {
                ThreadContextUtils.put(ThreadContextProperty.OUTCOME, Outcome.SUCCESS.name());
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
        return Instant.ofEpochMilli(Long.parseLong(ThreadContextUtils.get(ThreadContextProperty.START_TIME)));
    }

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

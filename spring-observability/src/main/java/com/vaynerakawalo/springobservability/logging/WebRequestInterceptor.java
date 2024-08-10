package com.vaynerakawalo.springobservability.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
public class WebRequestInterceptor implements HandlerInterceptor {
    private static final String TRACE_HEADER = "X-TRACE-ID";
    private final WebRequestLogger logger;
    private final Clock clock;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var trace = setTraceIfAbsent(request, response);

        ThreadContext.put(ThreadContextProperty.TRACE, trace);
        ThreadContext.put(ThreadContextProperty.START_TIME, String.valueOf(clock.millis()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws JsonProcessingException {
        try {
            logger.logCompletedRequest(request, response, ex);
        }
        finally {
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

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

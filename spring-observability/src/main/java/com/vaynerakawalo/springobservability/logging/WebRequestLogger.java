package com.vaynerakawalo.springobservability.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.MapMessage;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class WebRequestLogger {
    private static final String TRACE_KEY = "trace";
    private static final String REQUEST_PATH = "path";
    private static final String REQUEST_TIME = "requestTime";
    private static final String RESPONSE_CODE = "statusCode";
    private static final String ERROR = "error";
    private static final String QUERY_PARAMS = "query_params";

    private final Clock clock;

    public void logCompletedRequest(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        logRequest(prepareCompletedRequestLog(request, response, ex));
    }

    protected Map<String, Object> prepareCompletedRequestLog(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return Map.of(
                REQUEST_PATH, getRequestPath(request),
                REQUEST_TIME, getRequestTime(),
                RESPONSE_CODE, response.getStatus(),
                ERROR, getError(ex),
                TRACE_KEY, ThreadContextProperty.getValue(ThreadContextProperty.TRACE),
                QUERY_PARAMS, request.getParameterMap()
        );
    }

    private void logRequest(Map<String, Object> toBeLogged) {
        log.info(new MapMessage<>(toBeLogged));
    }

    private String getRequestPath(HttpServletRequest request) {
        return "%s %s".formatted(request.getMethod(), request.getServletPath());
    }

    private long getRequestTime() {
        return clock.millis() - Long.parseLong(ThreadContextProperty.getValue(ThreadContextProperty.START_TIME));
    }

    private String getError(Exception ex) {
        return Objects.nonNull(ex) ? ex.getMessage() : ThreadContextProperty.EMPTY;
    }
}

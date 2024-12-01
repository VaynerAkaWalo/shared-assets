package com.vaynerakawalo.springobservability.logging.interceptor;

import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class EgressCallInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ThreadContextProperty.TARGET_URL.putString(request.getURI().getPath());
        return execution.execute(request, body);
    }
}

package com.vaynerakawalo.springobservability;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaynerakawalo.springobservability.logging.WebRequestInterceptor;
import com.vaynerakawalo.springobservability.logging.WebRequestLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Configuration
public class ObservabilityAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webRequestInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    @ConditionalOnMissingBean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    WebRequestLogger webRequestLogger(ObjectMapper objectMapper, Clock clock) {
        return new WebRequestLogger(objectMapper, clock);
    }

    @Bean
    @ConditionalOnMissingBean
    WebRequestInterceptor webRequestInterceptor() {
        return new WebRequestInterceptor(webRequestLogger(objectMapper(), clock()), clock());
    }
}

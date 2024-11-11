package com.vaynerakawalo.springobservability;

import com.vaynerakawalo.springobservability.logging.ThreadContextPopulationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
    ThreadContextPopulationInterceptor webRequestInterceptor() {
        return new ThreadContextPopulationInterceptor(clock());
    }
}

package com.vaynerakawalo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloWorldAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    HelloWorld helloWorld() {
        return new HelloWorld();
    }
}

package com.vaynerakawalo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloWorld {

    @PostConstruct
    void helloWorld() {
        log.info("Library successfully loaded - Hello World");
    }
}

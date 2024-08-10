package com.vaynerakawalo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorld {

    @PostConstruct
    void helloWorld() {
        log.info("Library successfully loaded - Hello World");
    }
}

package com.vaynerakawalo.springobservability.logging.model;

import lombok.Getter;

@Getter
public enum Outcome {
    SUCCESS("Success"),
    ERROR("Error");

    private final String displayName;

    Outcome(String name) {
        this.displayName = name;
    }
}

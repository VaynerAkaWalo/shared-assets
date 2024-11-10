package com.vaynerakawalo.springobservability.logging.model;

public enum Outcome {
    SUCCESS("Success"),
    ERROR("Error");

    private final String name;

    Outcome(String name) {
        this.name = name;
    }
}

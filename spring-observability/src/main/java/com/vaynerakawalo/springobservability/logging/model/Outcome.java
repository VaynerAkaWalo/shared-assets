package com.vaynerakawalo.springobservability.logging.model;


public enum Outcome {
    SUCCESS("Success"),
    ERROR("Error");

    private final String displayName;

    Outcome(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}

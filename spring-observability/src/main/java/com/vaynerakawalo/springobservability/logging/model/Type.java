package com.vaynerakawalo.springobservability.logging.model;


public enum Type {
    UNKNOWN("Unknown"),
    INGRESS("Ingress"),
    EGRESS("Egress");

    private final String displayName;
    Type(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}

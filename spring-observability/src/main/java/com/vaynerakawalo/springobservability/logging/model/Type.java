package com.vaynerakawalo.springobservability.logging.model;


public enum Type {
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

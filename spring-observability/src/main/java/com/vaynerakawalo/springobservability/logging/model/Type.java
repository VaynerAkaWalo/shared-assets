package com.vaynerakawalo.springobservability.logging.model;

public enum Type {
    INGRESS("Ingress"),
    EGRESS("Egress");

    private final String name;

    Type(String name) {
        this.name = name;
    }
}

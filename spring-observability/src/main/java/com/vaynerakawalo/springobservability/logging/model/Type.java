package com.vaynerakawalo.springobservability.logging.model;

import lombok.Getter;

@Getter
public enum Type {
    INGRESS("Ingress"),
    EGRESS("Egress");

    private final String displayName;
    Type(String name) {
        this.displayName = name;
    }
}

package com.kelchm.scheduledo.models.task;

/**
 * Created by kelchm on 12/4/13.
 */
public enum Priorities {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private String priorityName;

    Priorities(String name) {
        priorityName = name;
    }

    @Override
    public String toString() {
        return priorityName;
    }
}
package com.example.campusrideshare.domain;

public enum RideStatus {
    OPEN, FULL, CANCELLED, COMPLETED;

    @Override
    public String toString() {
        return switch(this.ordinal()) {
            case 0 -> "Open";
            case 1 -> "Full";
            case 2 -> "Cancelled";
            case 3 -> "Completed";
            default -> "Other";
        };
    }

    public static RideStatus fromLabel(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }
        String v = value.trim();
        for (RideStatus s : values()) {
            if (s.name().equalsIgnoreCase(v) || s.toString().equalsIgnoreCase(v)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown ride status: " + value);
    }
}

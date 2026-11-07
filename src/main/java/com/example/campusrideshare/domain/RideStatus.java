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
}

package com.example.campusrideshare.domain;

public enum Department {
    IT, ENG, SCI, MATH, BUS, EDU, ART, SOC, OTHER, UNDECIDED;

    @Override
    public String toString() {
        return switch(this.ordinal()) {
            case 0 -> "Information Technology";
            case 1 -> "Engineering";
            case 2 -> "Science";
            case 3 -> "Mathematics";
            case 4 -> "Business";
            case 5 -> "Education";
            case 6 -> "Arts";
            case 7 -> "Social Sciences";
            case 8 -> "Other";
            default -> "Undecided";
        };
    }
}

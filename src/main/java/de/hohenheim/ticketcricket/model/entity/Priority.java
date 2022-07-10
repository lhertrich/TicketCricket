package de.hohenheim.ticketcricket.model.entity;


public enum Priority {
    SEHR_WICHTIG("Sehr Wichtig"), WICHTIG("Wichtig"), UNWICHTIG("Unwichtig");

    private final String displayValue;

    private Priority(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

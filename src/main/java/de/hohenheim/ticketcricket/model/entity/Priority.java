package de.hohenheim.ticketcricket.model.entity;


public enum Priority {
    HOCH("Hoch"), MITTEL("Mittel"), NIEDRIG("Niedrig");

    private final String displayValue;

    private Priority(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

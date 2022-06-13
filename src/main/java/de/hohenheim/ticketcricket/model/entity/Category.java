package de.hohenheim.ticketcricket.model.entity;

public enum Category {
    INAKTIVITÄT("Inaktivität"), TECHNISCHE_PROBLEME("Technisches Problem"), SONSTIGES("Sonstiges");

    private final String displayValue;

    private Category(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

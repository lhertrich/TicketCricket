package de.hohenheim.ticketcricket.model.entity;

public enum Status {
    OFFEN("Offen"), IN_BEARBEITUNG("In Bearbeitung"), ERLEDIGT("Erledigt");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }


}

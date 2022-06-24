package de.hohenheim.ticketcricket.model.entity;

public enum NotificationType {
    STATUS_ANFRAGE("Status angefragt"), STATUS_ÄNDERUNG("Status geändert"), NACHRICHT("Neue Nachricht");

    private final String displayValue;

    private NotificationType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

package de.hohenheim.ticketcricket.model.entity;

public enum NotificationType {
    STATUS_ANFRAGE("Status angefragt"), TICKET_ZUGEWIESEN("Ticket zugewiesen"), NACHRICHT("Neue Nachricht"), STATUS_ÄNDERUNG("Status geändert"), KATEGORIE_ÄNDERUNG("Kategorie geändert"), PRIORITÄT_ÄNDERUNG("Priorität geändert");

    private final String displayValue;

    private NotificationType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

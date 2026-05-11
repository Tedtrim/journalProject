package data;

import java.time.LocalDate;
import java.util.UUID;

public class JournalEntry {

    private LocalDate date;
    private String text;
    private UUID id;

    public JournalEntry(LocalDate date, String text) {
        this.date = date;
        this.text = text;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

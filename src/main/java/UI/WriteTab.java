package UI;

import data.JournalEntry;
import data.JournalRepository;
import data.JournalStore;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class WriteTab {

    private JournalStore store;
    private JournalRepository repo;


    public WriteTab (JournalStore store, JournalRepository repo) {
        this.repo = repo;
        this.store = store;
    }

    public VBox getLayout() {

        Label entryLabel = new Label("Enter Journal Entry");
        TextArea entryArea = new TextArea();
        entryArea.setWrapText(true);
        Label nameLabel = new Label("");

        Button button = new Button("Save Entry");
        button.setOnAction(e -> {
            String text = entryArea.getText();

            if (text.isEmpty()) {
                nameLabel.setText("Please submit entry");
                return;
            }
            LocalDate today = LocalDate.now();
            JournalEntry entry = new JournalEntry(today, text);

            store.save(entry);
            try {
                repo.save(store.getEntryMap());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            nameLabel.setText("Entry saved for " + today);
            entryArea.clear();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                entryLabel,
                entryArea,
                button,
                nameLabel
        );

        return layout;
    }

}

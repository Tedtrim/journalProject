package UI;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import data.JournalEntry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class JournalUI extends Application {

    @Override
    public void start(Stage stage) {
        Label entryLabel = new Label("Enter Journal Entry");
        TextArea entryArea = new TextArea();
        entryArea.setWrapText(true);
        Label nameLabel = new Label("");
        Map<LocalDate, JournalEntry> entries = new HashMap<>();

        Button button = new Button("Save Entry");
        button.setOnAction(e -> {
            String text = entryArea.getText();

            if (text.isEmpty()) {
                nameLabel.setText("Please submit entry");
                return;
            }
            LocalDate today = LocalDate.now();
            JournalEntry entry = new JournalEntry(today, text);

            entries.put(today, entry);
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

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

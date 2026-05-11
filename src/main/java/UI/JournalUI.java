package UI;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import data.JournalEntry;
import data.JournalRepository;
import data.JournalStore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class JournalUI extends Application {

    public JournalStore store = new JournalStore();
    public JournalRepository repo = new JournalRepository("journal.json");


    @Override
    public void start(Stage stage) {

        store.setEntryMap(repo.load());
        WriteTab writeTab = new WriteTab(store, repo);
        ReadTab readTab = new ReadTab(store, repo);

        VBox writeLayout = writeTab.getLayout();
        HBox readLayout = readTab.getLayout();

        Tab submitEntries = new Tab("Submit Entries");
        submitEntries.setContent(writeLayout);

        Tab readPastEntries = new Tab("Read Previous Entries");
        readPastEntries.setContent(readLayout);

        submitEntries.setClosable(false);
        readPastEntries.setClosable(false);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(submitEntries, readPastEntries);

        Scene scene = new Scene(tabPane, 700, 500);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

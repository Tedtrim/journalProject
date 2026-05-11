package UI;

import data.JournalEntry;
import data.JournalRepository;
import data.JournalStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import java.time.LocalDate;

public class ReadTab {

    private JournalStore store;
    private JournalRepository repo;

    public ReadTab(JournalStore store, JournalRepository repo) {
        this.store = store;
        this.repo = repo;
    }

    public HBox getLayout() {

        ObservableList<LocalDate> dates = FXCollections.observableArrayList(store.getEntryMap().keySet());
        ListView<LocalDate> entryList = new ListView(dates);
        TextArea readEntry = new TextArea();


        entryList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    StringBuilder sb = new StringBuilder();
                    for(JournalEntry entry : store.getEntryMap().get(newValue)) {
                        sb.append(entry.getText());
                        sb.append("\n\n");
                    }
                    readEntry.setText(sb.toString());
                }
        );



        HBox layout = new HBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                entryList,
                readEntry
        );



        return layout;
    }
}

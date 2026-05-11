package data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.UUID;

public class JournalStore {

    private Map<LocalDate, LinkedList<JournalEntry>> entryMap = new HashMap<>();

    public void setEntryMap(Map<LocalDate, LinkedList<JournalEntry>> entryMap) {
        this.entryMap = entryMap;
    }

    public Map<LocalDate, LinkedList<JournalEntry>> getEntryMap() {
        return entryMap;
    }

    public void save(JournalEntry entry) {
        if (!entryMap.containsKey(entry.getDate())) {
            LinkedList<JournalEntry> entryList = new LinkedList<>();
            entryList.add(entry);
            entryMap.put(entry.getDate(),entryList);
        }
        else {
             entryMap.get(entry.getDate()).add(entry);
        }
    }

    public void delete(LocalDate key, UUID id) {
        for(JournalEntry entry : entryMap.get(key)) {
            if (entry.getId().equals(id)) {
                entryMap.get(key).remove(entry);
                return;
            }
        }
    }

    public JournalEntry get(LocalDate key, UUID id) {
        for (JournalEntry entry : entryMap.get(key)) {
            if (entry.getId().equals(id)) {
                return entry;
            }
        }
        throw new IllegalArgumentException("Entry not found");
    }

    public void edit(LocalDate key, String newEntry, UUID id) {
       for (JournalEntry entry : entryMap.get(key)) {
           if (entry.getId().equals(id)) {
               entry.setText(newEntry);
               return;
           }
       }
    }

}

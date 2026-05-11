package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JournalRepository  {

    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        }
    }

    private final String filePath;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public JournalRepository(String filePath) {
        this.filePath = filePath;
    }

    public Map<LocalDate, LinkedList<JournalEntry>> load() {
        try {
            String json = Files.readString(Path.of(filePath));
            Type type = new TypeToken<Map<LocalDate, LinkedList<JournalEntry>>>(){}.getType();
            return gson.fromJson(json, type);
        }
        catch (IOException e) {
            return new HashMap<>();
        }
    }

    public void save(Map<LocalDate, LinkedList<JournalEntry>> entryMap) throws IOException {
        String json = gson.toJson(entryMap);
        Files.writeString(Path.of(filePath), json);
    }
}

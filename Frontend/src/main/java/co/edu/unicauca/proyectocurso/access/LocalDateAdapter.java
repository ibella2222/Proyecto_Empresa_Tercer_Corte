package co.edu.unicauca.proyectocurso.access; // O el paquete que prefieras

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador para que Gson pueda serializar y deserializar objetos LocalDate.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value)); // Convierte LocalDate a String "YYYY-MM-DD"
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String dateStr = in.nextString();
            return LocalDate.parse(dateStr, formatter); // Convierte String "YYYY-MM-DD" a LocalDate
        }
    }
}
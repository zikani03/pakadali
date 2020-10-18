package me.zikani.labs.pakadali.api.whatsapp;

import com.opencsv.CSVWriter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exports messages to CSV using OpenCSV library
 */
public class CsvMessagesExporter implements MessagesExporter {
    public static final String CSV_HEADERS_ONLY = "sender,message,timestamp,mentions\n";
    private static final String[] CSV_HEADERS = new String[] {
        "sender", "message", "timestamp", "mentions"
    };
    @Override
    public String mediaType() {
        return "text/csv;charset=utf-8";
    }

    @Override
    public String export(List<Message> messages) {
        if (messages == null) return CSV_HEADERS_ONLY;
        if (messages.isEmpty()) return CSV_HEADERS_ONLY;

        StringWriter sw = new StringWriter();
        try(CSVWriter writer = new CSVWriter(sw)) {
            writer.writeNext(CSV_HEADERS);
            writer.writeAll(messages.stream().map(m -> new String[]{
                m.getPhone(),
                m.getMessage(),
                String.valueOf(m.getTimestamp()),
                String.join(",", m.getMentions())
            }).collect(Collectors.toList()));
        } catch(IOException e) {
            LoggerFactory.getLogger(getClass()).error("Failed to write CSV, returning empty document", e);
            return CSV_HEADERS_ONLY;
        }
        return sw.toString();
    }
}

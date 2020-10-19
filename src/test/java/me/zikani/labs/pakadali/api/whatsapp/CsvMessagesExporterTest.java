package me.zikani.labs.pakadali.api.whatsapp;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static me.zikani.labs.pakadali.api.whatsapp.CsvMessagesExporter.CSV_HEADERS_ONLY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvMessagesExporterTest {

    private final CsvMessagesExporter exporter = new CsvMessagesExporter();

    @Test
    public void testExportEmptyCSVDocument() {
        assertEquals(CSV_HEADERS_ONLY, exporter.export(null));
        assertEquals(CSV_HEADERS_ONLY, exporter.export(Collections.emptyList()));
        assertEquals(CSV_HEADERS_ONLY, exporter.export(new ArrayList<>()));
    }

    @Test
    public void testExportOneMessage() {
        String exported = """
        "sender","message","timestamp","mentions"
        "+265999123456","Hello","1577614620000",""
        """;

        assertEquals(exported, exporter.export(singletonList(
            new Message("+265999123456", new String[0], 1577614620000L, "Hello"))));
    }
}

package me.zikani.labs.pakadali.api.whatsapp;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JsonMessagesExporterTest {

    @Test
    public void testEmptyMessages() {
        JsonMessagesExporter exporter = new JsonMessagesExporter();
        assertEquals("[]", exporter.export(null));
        assertEquals("[]", exporter.export(Collections.emptyList()));
        assertEquals("[]", exporter.export(new ArrayList<>()));
    }


}

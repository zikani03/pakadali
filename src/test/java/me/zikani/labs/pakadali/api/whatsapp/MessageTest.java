package me.zikani.labs.pakadali.api.whatsapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @Test
    public void testToString() {
        Message message = new Message("+265999123456", new String[0], 1577614620000L, "Hello");
        String expected ="1577614620000 +265999123456 Hello";
        assertEquals(expected, message.toString());
    }
}

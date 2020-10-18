package me.zikani.labs.pakadali.api.whatsapp;

import java.util.List;

/**
 * Export a list of messages to a String
 */
public interface MessagesExporter {
    /**
     * @return The media type that this exporter exports to
     */
    String mediaType();

    /**
     * Exports the messages to a String that represents the messages in the
     * given {@link #mediaType()}
     * @param messages list of messages
     * @return String representation of the list of messages
     */
    String export(List<Message> messages);
}

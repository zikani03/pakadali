package me.zikani.labs.pakadali.api.whatsapp;

import com.google.gson.Gson;

import java.util.List;

/**
 * Exports messages to JSON using Gson
 */
public class JsonMessagesExporter implements MessagesExporter {
    private final Gson gson = new Gson();

    @Override
    public String mediaType() {
        return "application/json;charset=utf-8";
    }

    @Override
    public String export(List<Message> messages) {
        if (messages == null) return "[]";
        if (messages.isEmpty()) return "[]";

        return gson.toJson(messages);
    }
}

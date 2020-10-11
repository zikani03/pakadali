package me.zikani.labs.pakadali.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String phone;
    private String[] mentions;
    private long timestamp;
    private String message;

    @Override
    public String toString() {
        return timestamp + " " + phone + " " + message;
    }
}

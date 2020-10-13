package me.zikani.labs.pakadali.api.utils;

import java.security.MessageDigest;

public final class HashUtil {
    HashUtil() {}

    public static String sha256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            byte[] byteData = md.digest();
            String hashed = Hex.encode(byteData);
            byteData = null;
            return hashed;
        } catch (Exception e) {
            throw new RuntimeException(e); // Shaaaa, that's just wrong!
        }
    }
}

package me.zikani.labs.pakadali;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.zikani.labs.pakadali.placeholder.ImagePlaceholderRoute;
import me.zikani.labs.pakadali.qr.QRCodeRoute;
import me.zikani.labs.pakadali.whatsapp.ChatToJsonRoute;
import spark.Spark;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

public class PakadaliApplication {
    public static final String MEDIA_TYPE_IMAGE_PNG = "image/png";

    public static void main(String[] args) {
        Spark.port(parseInt(getProperty("spark.port", "4567")));
        // Cache sharedCache;
        // Placeholder route
        Spark.staticFileLocation("public");
        Spark.get("/img/:wxh", new ImagePlaceholderRoute());

        // QR Code Routes
        Cache<String, byte[]> qrCodeCache = Caffeine.newBuilder().maximumSize(1000).build();
        Spark.get("/qr/generate/:size", new QRCodeRoute(qrCodeCache));
        Spark.post("/qr/generate/:size", new QRCodeRoute(qrCodeCache));

        Spark.post("/wa/chat2json", new ChatToJsonRoute());

        Spark.notFound((request, response) -> {
            return "Not found";
        });
    }
}


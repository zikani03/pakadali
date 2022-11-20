package me.zikani.labs.pakadali;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.zikani.labs.pakadali.api.whatsapp.CsvMessagesExporter;
import me.zikani.labs.pakadali.api.whatsapp.JsonMessagesExporter;
import me.zikani.labs.pakadali.api.whatsapp.VCardExporter;
import me.zikani.labs.pakadali.web.placeholder.ImagePlaceholderRoute;
import me.zikani.labs.pakadali.web.qr.QRCodeRoute;
import me.zikani.labs.pakadali.web.whatsapp.ChatExportRoute;
import spark.Spark;

import java.time.Duration;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

public class PakadaliApplication {
    public static final String MEDIA_TYPE_IMAGE_PNG = "image/png";

    public static void main(String[] args) {
        Spark.port(parseInt(getProperty("spark.port", "4567")));
        Cache<String, byte[]> sharedCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(parseInt(getProperty("pakadali.cacheExpirySeconds", "1800"))))
            .maximumSize(parseInt(getProperty("pakadali.cacheMaxSize", "420")))
            .build();
        // Placeholder route
        Spark.staticFileLocation("public");
        Spark.get("/img/:wxh", new ImagePlaceholderRoute(sharedCache));

        // QR Code Routes
        Spark.get("/qr/generate/:size", new QRCodeRoute(sharedCache));
        Spark.post("/qr/generate/:size", new QRCodeRoute(sharedCache));

        Spark.post("/wa/chat2json", new ChatExportRoute(new JsonMessagesExporter()));
        Spark.post("/wa/chat2csv", new ChatExportRoute(new CsvMessagesExporter()));
        Spark.post("/wa/vcard", new ChatExportRoute(new VCardExporter()));

        Spark.notFound((request, response) -> {
            return "Not found";
        });
    }
}


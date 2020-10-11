package me.zikani.labs.pakadali.placeholder;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static java.lang.Integer.parseInt;
import static me.zikani.labs.pakadali.PakadaliApplication.MEDIA_TYPE_IMAGE_PNG;

public class ImagePlaceholderRoute implements Route {
    private static final int LIGHT_GRAY_RGB = Color.lightGray.getRGB();
    private static final int MAX_WIDTH_HEIGHT = 9999;

    private final Cache<String, byte[]> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String widthAndHeight = request.params("wxh");
        byte[] image = cache.getIfPresent(widthAndHeight);

        if (image == null) {
            LoggerFactory.getLogger(getClass()).debug("Not found in cache, generating new image");
            int[] dimensions = parseWidthAndHeight(widthAndHeight);
            if (dimensions == null || dimensions.length < 2) {
                return Spark.halt(400);
            }
            int width = dimensions[0];
            int height = dimensions[1];
            if ((width < 0 || width > MAX_WIDTH_HEIGHT) || (height < 0 || height > MAX_WIDTH_HEIGHT)) {
                return Spark.halt(400);
            }

            try {
                image = generateImage(width, height).toByteArray();
            } catch(Exception e) {
                LoggerFactory.getLogger(getClass()).error("Failed to generate image", e);
                return Spark.halt(501);
            }

            cache.put(widthAndHeight, image);
        }
        response.type(MEDIA_TYPE_IMAGE_PNG);
        response.raw().getOutputStream().write(image);
        return response;
    }

    public ByteArrayOutputStream generateImage(int width, int height) throws Exception {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bufferedImage.setRGB(i, j, LIGHT_GRAY_RGB);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", os);
            return os;
        }
    }

    private static int[] parseWidthAndHeight(String spec) {
        if (spec == null) return null;

        if (!spec.toLowerCase().contains("x")) {
            return null;
        }
        String[] wh = spec.toLowerCase().split("x", 2);
        if (wh.length < 2) return null;
        return new int[]{ parseInt(wh[0]), parseInt(wh[1]) };
    }
}

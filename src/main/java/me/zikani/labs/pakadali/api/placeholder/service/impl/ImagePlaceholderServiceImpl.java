package me.zikani.labs.pakadali.api.placeholder.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import me.zikani.labs.pakadali.api.placeholder.service.PNGImagePlaceHolderService;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static java.lang.Integer.parseInt;


public class ImagePlaceholderServiceImpl implements PNGImagePlaceHolderService {
    private static final int LIGHT_GRAY_RGB = Color.lightGray.getRGB();

    @Override
    public byte[] getImagePlaceHolder(int width, int height, Cache<String, byte[]> cache) throws Exception {
        String widthAndHeight = width +"x"+height;
        byte[] image = cache.getIfPresent(widthAndHeight);

        if (image == null) {
            LoggerFactory.getLogger(getClass()).debug("Not found in cache, generating new image");
            image = generateImage(width, height).toByteArray();
            cache.put(widthAndHeight, image);
        }
        return image;
    }

    private ByteArrayOutputStream generateImage(int width, int height) throws Exception {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bufferedImage.setRGB(i, j, LIGHT_GRAY_RGB);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", os);
            return os;
        }
    }
}

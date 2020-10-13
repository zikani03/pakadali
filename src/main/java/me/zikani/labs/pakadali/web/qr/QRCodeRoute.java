package me.zikani.labs.pakadali.web.qr;

import com.github.benmanes.caffeine.cache.Cache;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static me.zikani.labs.pakadali.PakadaliApplication.MEDIA_TYPE_IMAGE_PNG;
import static me.zikani.labs.pakadali.api.utils.HashUtil.sha256;

public class QRCodeRoute implements Route {
    private static final int QR_MAX_SIZE = 9999;
    private final Cache<String, byte[]> cache;

    public QRCodeRoute(Cache<String, byte[]> qrCodeCache) {
        this.cache = requireNonNull(qrCodeCache, "qrCodeCache");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final int widthAndHeight = Integer.parseInt(request.params("size"));
        final String content = request.queryMap("content").value();
        final String hashed = sha256(widthAndHeight + "/" + content);
        final String qrCacheID = String.format("qr__%s", hashed);
        byte[] image = cache.getIfPresent(qrCacheID);

        if (image == null) {
            if (widthAndHeight < 0 || widthAndHeight > QR_MAX_SIZE) {
                return Spark.halt(400);
            }

            try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                QRCode.from(content)
                    .withSize(widthAndHeight, widthAndHeight)
                    .to(ImageType.PNG)
                    .writeTo(bos);
                image = bos.toByteArray();
                cache.put(qrCacheID + "", image);
            } catch(IOException e) {
                LoggerFactory.getLogger(getClass()).error(e.getMessage());
                return Spark.halt(500);
            }
        }

        response.type(MEDIA_TYPE_IMAGE_PNG);
        response.raw().getOutputStream().write(image);
        return response;
    }
}

package me.zikani.labs.pakadali.web.qr;

import com.github.benmanes.caffeine.cache.Cache;
import me.zikani.labs.pakadali.api.qr.service.impl.QRCodeServiceImpl;
import me.zikani.labs.pakadali.api.qr.service.QRCodeService;
import me.zikani.labs.pakadali.api.qr.validator.impl.QRCodeValidatorImpl;
import me.zikani.labs.pakadali.api.qr.validator.QRCodeValidator;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import static java.lang.System.getProperty;
import static java.util.Objects.requireNonNull;
import static me.zikani.labs.pakadali.PakadaliApplication.MEDIA_TYPE_IMAGE_PNG;

public class QRCodeRoute implements Route {
    private final Cache<String, byte[]> cache;

    public QRCodeRoute(Cache<String, byte[]> qrCodeCache) {
        this.cache = requireNonNull(qrCodeCache, "qrCodeCache");
    }

    @Override
    public Object handle(Request request, Response response) {
        final String content = request.queryMap("content").value();
        QRCodeValidator qrCodeValidator = new QRCodeValidatorImpl();
        boolean validationResult = qrCodeValidator.isQRCodeSizeValid(request.params("size"));
        if(!validationResult) {
            return Spark.halt(400);
        }
        QRCodeService qrCodeService = new QRCodeServiceImpl();
        final int widthAndHeight = Integer.parseInt(request.params("size"));
        try {
            byte[] image = qrCodeService.getQRCode(widthAndHeight,content,cache);
            response.type(MEDIA_TYPE_IMAGE_PNG);
            response.header("Cache-Control", String.format("max-age=%s", getProperty("pakadali.cacheExpirySeconds", "1800")));
            response.raw().getOutputStream().write(image);
            return response;
        }
        catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error(e.getMessage());
            return Spark.halt(500);
        }
    }
}

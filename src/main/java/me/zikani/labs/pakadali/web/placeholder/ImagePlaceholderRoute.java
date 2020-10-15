package me.zikani.labs.pakadali.web.placeholder;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.zikani.labs.pakadali.api.placeholder.service.impl.ImagePlaceholderServiceImpl;
import me.zikani.labs.pakadali.api.placeholder.service.PNGImagePlaceHolderService;
import me.zikani.labs.pakadali.api.placeholder.validator.impl.PlaceholderValidator;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import static java.lang.Integer.parseInt;
import static me.zikani.labs.pakadali.PakadaliApplication.MEDIA_TYPE_IMAGE_PNG;

public class ImagePlaceholderRoute implements Route {
    private final Cache<String, byte[]> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        int[] dimension = PlaceholderValidator.parseImageWidthAndHeight(request.params("wxh"));
        if(dimension == null)
        {
            return Spark.halt(400);
        }
        try
        {
            PNGImagePlaceHolderService imagePlaceHolderService = new ImagePlaceholderServiceImpl();
            byte[] image =imagePlaceHolderService.getImagePlaceHolder(dimension[0],dimension[1], cache);
            response.type(MEDIA_TYPE_IMAGE_PNG);
            response.raw().getOutputStream().write(image);
            return response;
        }
        catch (Exception e)
        {
            LoggerFactory.getLogger(getClass()).error(e.getMessage());
            return Spark.halt(500);
        }
    }
}

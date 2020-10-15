package me.zikani.labs.pakadali.web.whatsapp;

import com.google.gson.Gson;
import me.zikani.labs.pakadali.api.whatsapp.ChatReader;
import me.zikani.labs.pakadali.api.whatsapp.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.util.List;

public class ChatToJsonRoute implements Route {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatToJsonRoute.class);;
    private final ChatReader chatReader = new ChatReader();
    private final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        Part file = request.raw().getPart("file"); //file is name of the upload form
        if (file == null)
            return Spark.halt(400);
        if (file.getInputStream() == null)
            return Spark.halt(400);

        LOGGER.info("Reading chat messages from {} Uploaded from {}", file.getSubmittedFileName(), request.ip());
        List<Message> messages = chatReader.readMessages(file.getInputStream());
        response.type("application/json;charset=utf-8");
        return gson.toJson(messages);
    }
}

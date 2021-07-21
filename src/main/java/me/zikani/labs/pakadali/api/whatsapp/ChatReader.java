package me.zikani.labs.pakadali.api.whatsapp;

import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatReader {
    // Pattern to match the date times in the messages
    static final Pattern messageLinePattern = Pattern.compile("^(?<timestamp>\\d{1,2}\\/\\d{1,2}\\/\\d{2,4},\\s\\d{1,2}:\\d{2}\\s(AM|PM|am|pm)?\\s?\\-)\\s(?<sender>(\\w*[^:]\\s*?)+\\b:)\\s(?<message>.*$)");
    static final Pattern datePattern = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{2,4},\\s\\d{1,2}:\\d{2}\\s(AM|PM|am|pm)?");
    static final Pattern mediaOmittedPattern = Pattern.compile("\\<Media omitted\\>|Waiting for this message");
    static final Pattern atPhoneNumber = Pattern.compile("(@\\d{8,13})");
    static final Pattern justNumbers = Pattern.compile("\\d*\\s*\\d");

    private static final List<SimpleDateFormat> DEFAULT_DATE_FORMAT_LIST = List.of(
        new SimpleDateFormat("MM/dd/YY, HH:mm a -"),
        new SimpleDateFormat("MM/dd/YY, HH:mm -")
    );
    // The data format that was successful, is for the first message we successfully parse
    private SimpleDateFormat currentFormat = null;


    public List<Message> readMessages(InputStream inputStream) throws Exception {
        List<Message> messages = new ArrayList<>();
        try (Scanner sc = new Scanner(inputStream)) {

            StringBuilder sb = new StringBuilder();
            String currentLine = null;
            /** This loop does the following things
             * 0. Reads each line in the input
             * 1. Normalize messages so that each appears as one line
             * 2. Skips <media omitted> lines in the input
             */
            while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                if (mediaOmittedPattern.matcher(currentLine).find()) {
                    continue; // Skip media lines
                }
                // Create a new string builder if there's a new date pattern in the
                // input for the next message
                if (datePattern.matcher(currentLine).find()) {
                    parseMessage(sb.toString()).ifPresent(messages::add);
                    sb = new StringBuilder();
                }

                currentLine = currentLine.replace("PM - +", "PM - ");
                currentLine = currentLine.replace("AM - +", "AM - ");
                sb.append(currentLine).append(" ");
            }
            parseMessage(sb.toString()).ifPresent(messages::add);
            //String output = messages.stream().map(Message::toString).collect(Collectors.joining("\n"));
            //Files.write(Paths.get("messages-normalized.text"), output.getBytes());
            return messages;
        }
    }

    public Optional<Message> parseMessage(String messageLine) {
        var matcher = messageLinePattern.matcher(messageLine);
        if (matcher.matches()) {
            // TODO: String sender = sha256(matcher.group("sender").replace(" ", ""));
            String sender = matcher.group("sender").trim();
            if (justNumbers.matcher(sender).find()) {
                sender = sender.replace(" ", "");
            }
            sender = sender.replace(":", "");
            long timestamp = -1;
            if (currentFormat == null) {
                for (SimpleDateFormat sdf : DEFAULT_DATE_FORMAT_LIST) {
                    timestamp = parseTimestamp(matcher, sdf);
                    if (timestamp != -1) {
                        currentFormat = sdf;
                        break;
                    }
                }
            } else {
                timestamp = parseTimestamp(matcher, currentFormat);
            }

            return Optional.of(new Message(
                sender,
                new String[0], // TODO: Parse mentions...
                timestamp,
                matcher.group("message")
            ));

        }

        return Optional.empty();
    }

    private long parseTimestamp(Matcher matcher, SimpleDateFormat sdf) {
        try {
            return sdf.parse(matcher.group("timestamp")).toInstant().toEpochMilli();
        } catch(Exception e) {
            LoggerFactory.getLogger(ChatReader.class).warn("Failed to parse timestamp: {} using {}", matcher.group("timestamp"), sdf.toPattern());
        }
        return -1;
    }
}

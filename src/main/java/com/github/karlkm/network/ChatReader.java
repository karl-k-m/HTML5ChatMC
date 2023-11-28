package com.github.karlkm.network;

import com.github.karlkm.dataobjects.MessageCircularBuffer;
import com.github.karlkm.dataobjects.MessageObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ChatReader {
    ArrayList<MessageObject> messageBuffer = new ArrayList<>();
    int roomId;
    private final Logger logger;

    public ChatReader(int roomId, Logger logger) {
        this.roomId = roomId;
        this.logger = logger;

        MessageObject[] last10 = getLast10Messages();
    }

    public String requestJson() {
        try {
            URL url = new URL("https://html5-chat.com/ajax.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String data = "a=getChatMessages&maxChats=10&roomid=" + roomId;

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            return response.toString();
        } catch (Exception e) {
            logger.severe("Error accessing HTML5-Chat: " + e.getMessage());
        }

        return null;
    }

    public MessageObject[] parseJson(String json) {
        MessageObject[] out = new MessageObject[10];

        JSONArray postsArr = new JSONArray(json);

        for (int i = 0; i < postsArr.length(); i++) {
            JSONObject post = postsArr.getJSONObject(i);

            MessageObject msg = new MessageObject(post.getString("username"), post.getString("message"));

            out[i] = msg;
        }

        return out;
    }

    public MessageObject[] getLast10Messages() {
        return parseJson(requestJson());
    }

    public ArrayList<MessageObject> getNewMessages() {
        MessageObject[] last10 = getLast10Messages();

        ArrayList<MessageObject> out = new ArrayList<>();

        for (MessageObject i : last10) {
            if (!messageBuffer.contains(i)) {
                messageBuffer.add(i);
                out.add(i);
            }
        }

        logger.warning("Logged " + out.size() + " new messages.");

        return out;
    }

}

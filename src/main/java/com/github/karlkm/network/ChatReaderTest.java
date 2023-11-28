package com.github.karlkm.network;

import com.github.karlkm.dataobjects.MessageObject;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ChatReaderTest {

    @Test
    void requestJson() {
        ChatReader reader = new ChatReader(54654, null);
        String json = reader.requestJson();

        assertNotEquals("", json);
    }

    @Test
    void parseJson() {
        ChatReader reader = new ChatReader(54654, null);
        String json = reader.requestJson();
        MessageObject[] messages = reader.parseJson(json);

        for (MessageObject i : messages) {
            System.out.println(i.toString());
        }

        assertNotEquals(messages[0], null);
    }
}
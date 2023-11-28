package com.github.karlkm.dataobjects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.Objects;

public class MessageObject {
    String sender;
    String message;

    public MessageObject(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public TextComponent toTextComponent() {
        return Component.text("<"  + sender + ">").color(TextColor.color(255, 102, 204))
                .append(Component.text(": " + message, NamedTextColor.WHITE));
    }

    public String toString() {
        return sender + ": " + message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MessageObject that = (MessageObject) obj;
        return Objects.equals(sender, that.sender) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, message);
    }
}

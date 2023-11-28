package com.github.karlkm.services;

import com.github.karlkm.dataobjects.MessageObject;
import com.github.karlkm.network.ChatReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class MessageServeService {
    private final JavaPlugin plugin;
    private final Set<UUID> subscribedPlayers;
    private final int roomId;
    private ChatReader reader;
    private boolean isFeatureEnabled;

    public MessageServeService(JavaPlugin plugin, boolean isFeatureEnabled) {
        this.plugin = plugin;
        this.roomId = 1;
        this.subscribedPlayers = new HashSet<>();
        this.reader = new ChatReader(roomId, plugin.getLogger());
        this.isFeatureEnabled = isFeatureEnabled;

        if (isFeatureEnabled) {
            startMessageTask();
        }
    }

    public void toggleFeature() {
        isFeatureEnabled = !isFeatureEnabled;
        if (isFeatureEnabled) {
            startMessageTask();
        }
        if (!isFeatureEnabled) {
            subscribedPlayers.clear();
        }
    }

    public void startMessageTask() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::sendMessageToPlayers, 0L, 20L * 10);
    }

    private void sendMessageToPlayers() {
        if (!isFeatureEnabled) {
            return;
        }
        ArrayList<MessageObject> messages = reader.getNewMessages();
        for (UUID playerId : subscribedPlayers) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                for (MessageObject msg : messages) {
                    player.sendMessage(msg.toTextComponent());
                }
            }
        }
    }

    public void togglePlayerSubscription(Player player) {
        if (!isFeatureEnabled) {
            player.sendMessage(Component.text("HTML5Chat is not enabled. (/html5toggle to toggle)", TextColor.color(255, 0, 0))
                    .decoration(TextDecoration.BOLD, true));
            return;
        }

        UUID playerId = player.getUniqueId();
        if (subscribedPlayers.contains(playerId)) {
            subscribedPlayers.remove(playerId);
            player.sendMessage(Component.text("HTML5-Chat is now hidden.", TextColor.color(152, 2, 72))
                    .decoration(TextDecoration.BOLD, true));
        } else {
            subscribedPlayers.add(playerId);
            player.sendMessage(Component.text("Now displaying HTML5-Chat.", TextColor.color(152, 2, 72))
                    .decoration(TextDecoration.BOLD, true));
        }
    }

    public boolean isFeatureEnabled() {
        return isFeatureEnabled;
    }
}

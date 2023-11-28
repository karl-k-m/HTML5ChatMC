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

    public MessageServeService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.roomId = 54654;
        this.subscribedPlayers = new HashSet<>();
        this.reader = new ChatReader(roomId, plugin.getLogger());

        startMessageTask();
    }

    public void startMessageTask() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::sendMessageToPlayers, 0L, 20L * 10);
    }

    private void sendMessageToPlayers() {
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
        UUID playerId = player.getUniqueId();
        if (subscribedPlayers.contains(playerId)) {
            subscribedPlayers.remove(playerId);
            player.sendMessage(Component.text("HTML5Chat Disabled.", TextColor.color(152, 2, 72))
                    .decoration(TextDecoration.BOLD, true));
        } else {
            subscribedPlayers.add(playerId);
            player.sendMessage(Component.text("HTML5Chat Enabled.", TextColor.color(152, 2, 72))
                    .decoration(TextDecoration.BOLD, true));
        }
    }
}

package com.github.karlkm.html5chatmc;

import com.github.karlkm.services.MessageServeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class HTML5ChatMC extends JavaPlugin {
    private MessageServeService messageServeService;

    @Override
    public void onEnable() {
        Logger logger = this.getLogger();
        messageServeService = new MessageServeService(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("html5") && sender instanceof Player) {
            messageServeService.togglePlayerSubscription((Player) sender);
            return true;
        }

        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

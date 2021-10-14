package com.theb2d.expresstp;

import com.theb2d.expresstp.commands.TPrequest;
import com.theb2d.expresstp.events.Events;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExpressTp extends JavaPlugin {
    public Events events;

    @Override
    public void onEnable() {
        TPrequest command = new TPrequest();
        events = new Events(this);
        getServer().getPluginManager().registerEvents(events, this);
        getServer().getConsoleSender().sendMessage("[ExpressTP] Plugin v0.5 has been enabled" + ChatColor.GREEN);
        getCommand("etp").setExecutor(command);
        this.saveDefaultConfig();
        this.getConfig().addDefault("tp.multiplier", 1);
        this.getConfig().addDefault("tp.default_amount", 3);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[ExpressTP] Plugin v0.5 has been disabled" + ChatColor.RED);
    }

}

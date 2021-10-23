package com.theb2d.expresstp;

import com.theb2d.expresstp.commands.LocationTP;
import com.theb2d.expresstp.commands.TPrequest;
import com.theb2d.expresstp.events.Events;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExpressTp extends JavaPlugin {
    public Events events;
    public LocationTP Tp_loc;

    @Override
    public void onEnable() {
        TPrequest TP_req = new TPrequest();
        Tp_loc = new LocationTP(this);
        events = new Events(this);

        getServer().getPluginManager().registerEvents(events, this);
        getServer().getConsoleSender().sendMessage("[ExpressTP] Plugin has been enabled" + ChatColor.GREEN);
        getCommand("etp").setExecutor(TP_req);
        getCommand("ltp").setExecutor(Tp_loc);
        this.saveDefaultConfig();
        this.getConfig().addDefault("tp.multiplier", 0.5);
        this.getConfig().addDefault("tp.default_amount", 3);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[ExpressTP] Plugin has been disabled" + ChatColor.RED);
    }

}

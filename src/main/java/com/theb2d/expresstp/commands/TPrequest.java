package com.theb2d.expresstp.commands;

import com.theb2d.expresstp.handlers.MessageAwaiter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPrequest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.getServer().getConsoleSender().sendMessage("You are not a player");
            return true;
        }
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("etp")) {
            if (args.length == 0) {
                sender.sendMessage("§l§e(!)§r§c Usage: §7/etp <player>");
                return true;
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);

            if(player==target){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§l§e(!)§r§7 Why would you want to teleport to yourself?"));
                return true;
            }

            if (target != null) {
                MessageAwaiter.setPlayer(target.getDisplayName(), player.getDisplayName()); //[target, sender]
                target.sendMessage("§l§e(!)§r§7 " + player.getDisplayName() + " wants to tp to you. Enter §l§b\"y\"§r§7 in the chat to approve the request");
            } else if (target == null) {
                player.sendMessage("§l§e(!)§r§c This player is offline!");
            }
        }

        return true;
    }
}

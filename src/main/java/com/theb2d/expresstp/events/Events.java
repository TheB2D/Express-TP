package com.theb2d.expresstp.events;

import com.theb2d.expresstp.ExpressTp;
import com.theb2d.expresstp.handlers.MessageAwaiter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {

    private static ExpressTp mainClass;

    public Events(ExpressTp main){
        this.mainClass = main; //main instance
    }

    public static ExpressTp plugin = ExpressTp.getPlugin(ExpressTp.class);

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event){
        Player target = event.getPlayer();
        if(MessageAwaiter.getPlayerList().containsValue(target.getDisplayName())){

            String senderStr = (String) MessageAwaiter.getPlayerList().get(target.getDisplayName()); //sender
            Player sender = Bukkit.getServer().getPlayer(senderStr);

            if(event.getMessage().equalsIgnoreCase("y")){

                Location senderLoc = sender.getLocation() , targetLoc = target.getLocation();
                FileConfiguration config = mainClass.getConfig(); //config

                int default_amount = config.getInt("tp.default_amount");
                int multiplier = config.getInt("tp.multiplier");
                int distance = (int) sender.getLocation().distance(target.getLocation());
                int amount = (distance*multiplier)<default_amount ? default_amount : (distance*multiplier);

                if(sender.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), amount)){
                    target.sendTitle("Stay still!", null);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sender.teleport(targetLoc);
                        }
                    }.runTask(plugin);
                    sender.getInventory().removeItem(new ItemStack(Material.DIAMOND, amount));
                    sender.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    sender.sendMessage("§l§e(!)§r§7 You have successfully been teleported to " + target.getDisplayName() + "! " + Integer.toString(amount) + " diamonds has been deducted from you!");
                }

                else{
                    sender.playSound(sender.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                    sender.sendMessage("§l§e(!)§r§c You do not have enough resources to teleport to this person");
                }
            }

            else{
                sender.sendMessage("§l§e(!)§r§c " + target.getDisplayName() + " has denied your TP request!");
                sender.playSound(sender.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                target.sendMessage("§l§e(!)§r§c You have denied " + target.getDisplayName() + "'s TP request");
            }

            MessageAwaiter.clearTargetBind(target.getDisplayName());
            event.setCancelled(true);
        }
    }
}

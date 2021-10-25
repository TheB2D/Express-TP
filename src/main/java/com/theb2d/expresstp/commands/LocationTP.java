package com.theb2d.expresstp.commands;

import com.theb2d.expresstp.ExpressTp;
import com.theb2d.expresstp.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationTP implements CommandExecutor {

    private static ExpressTp mainClass;

    public LocationTP(ExpressTp main){
        this.mainClass = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("ltp")){


            Player player = (Player) sender;

            File file = new File("locations.yml");
            FileConfiguration locations = YamlConfiguration.loadConfiguration(file);
            List<String> location_list = locations.getKeys(false).stream().toList();


            if(!(sender instanceof Player)){
                sender.getServer().getConsoleSender().sendMessage("You are not a player");
                return true;
            }

            if(args.length==0){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c Usage:&7 /ltp <location or action> <optional: name>"));
                return true;
            }

            if(args[0].equalsIgnoreCase("create")){

                if(args.length!=2){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c Usage:&7 /ltp <location or action> <optional: name>"));
                    return true;
                }

                Location loc = player.getLocation();
                String loc_name = args[1].toString();

                if(locations.contains(args[1])){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c This location already exists!"));
                    return true;
                }


                if(!file.exists()){
                    try {
                        file.createNewFile();
                    }catch(IOException e){
                        Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
                    }
                }

                locations.set(loc_name, loc);
                try{
                    locations.save(file);
                }catch(IOException e){
                    Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
                }


                String msg = "&l&e(!)&r&a Created a new location named: &r&l" + loc_name + "&r&a!";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }

            else if(args[0].equalsIgnoreCase("remove")){

                if(args.length!=2){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c Usage:&7 /ltp <location or action> <opti0nal: name>"));
                    return true;
                }

                String loc = args[1].toString();
                if(locations.contains(loc)){
                    locations.set(loc, null);
                    try{
                        locations.save(file);
                    }catch(IOException e){
                        Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&a Successfully deleted warp " + loc));
                }else if(!locations.contains(loc)){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c No location named: " + "&7&l" + loc + "&r&c! Execute /ltp list to view the list of location warps"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                }
            }

            else if(args[0].equalsIgnoreCase("list")){
                String loc_list = (location_list.size()!=0) ? "&r&6=============&l&b Warp List &r&6=============&a\n\n"+String.join("\n",location_list)+"\n\n&6===================================" : "&l&e(!)&r&c No available location warps. Create a warp by /ltp create <name>"; // TODO: Check if this works
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', loc_list));
            }

            else if(locations.contains(args[0])){

                FileConfiguration config = mainClass.getConfig();

                String dest_name = args[0];

                int default_amount = config.getInt("tp.default_amount");
                float multiplier = (float)config.getInt("tp.multiplier")/100;
                float distance = (float) player.getLocation().distance(locations.getLocation(dest_name));
                int amount = (distance*multiplier)<(float)default_amount ? default_amount : (int)Math.round(((float)distance*multiplier));

                String payment = config.getString("tp.payment_material");
                ItemStack payment_IS = new ItemStack(Material.matchMaterial(payment.toUpperCase()));

                if(!(player.getInventory().containsAtLeast(payment_IS, amount))){ //TODO
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&c You do not have enough resources to teleport to this location! Required resource: &r&l&b"+ Utils.getAmount(player, payment_IS) + "/"+ Integer.toString(amount) + "&r&c " + payment.toLowerCase())); //TODO
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                    return true;
                }

                player.teleport(locations.getLocation(dest_name));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&e(!)&r&7 Successfully teleported to " + dest_name + "! " + amount + " "+ payment.toLowerCase() + "s has been deducted from you!"));
                player.getInventory().removeItem(new ItemStack(Material.getMaterial(payment), amount));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            }

            else{
                String msg = "&l&e(!)&r&c Action or location by the name of " + args[0].toLowerCase() + " is not found!";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
        return true;
    }
}

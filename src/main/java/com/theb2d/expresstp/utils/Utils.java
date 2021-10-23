package com.theb2d.expresstp.utils;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
    public static synchronized int getAmount(Player player, ItemStack looking_for) throws NullArgumentException {
        int amount = 0;
        Material mat = looking_for.getType();
        ItemStack[] items = player.getInventory().getContents();
        for(ItemStack item : items){
            if ((item!=null) && (item.getType()==mat) && (item.getAmount()>0)){
                amount+=item.getAmount();
            }
        }
        return amount;
    }
}

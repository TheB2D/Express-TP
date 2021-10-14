package com.theb2d.expresstp.handlers;

import java.util.HashMap;

public class MessageAwaiter {
    public static HashMap<String, String> playerBinds = new HashMap<String, String>();

    public static void setPlayer(String player, String target){
        playerBinds.put(target, player);
    }

    public static HashMap getPlayerList(){
        return playerBinds;
    }

    public static void clearTargetBind(String target){
        playerBinds.remove(target);
    }
}

package com.data_stream.proximitytextchat.Events;

import com.data_stream.proximitytextchat.Config.ProxTextConfig;
import com.data_stream.proximitytextchat.Main;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;


import java.awt.*;
import java.util.Random;

public class chatEventHandler {
    @SubscribeEvent
    public static void chatEvent(ServerChatEvent event) {
        final BlockPos playerPos = event.getPlayer().blockPosition();
        final PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for (int i = 0; i < playerList.getPlayers().size(); i++) {
            final double distance = Math.sqrt(playerList.getPlayers().get(i).blockPosition().distSqr(playerPos));
            if (distance <= ProxTextConfig.PROX_RANGE.get()) {
                playerList.getPlayers().get(i).sendMessage(new StringTextComponent("<" + event.getPlayer().getName().getString() + "> " + event.getMessage().toString()), ChatType.SYSTEM, Util.NIL_UUID);
            }
            if (distance >= ProxTextConfig.PROX_RANGE.get() && distance <= ProxTextConfig.DIST_RANGE.get() && ProxTextConfig.TOGGLEDISTORTION.get()) {
                String[] words = event.getMessage().toString().split("\\s+");
                String newmessage = "";
                Random rand = new Random();
                for (int j = 0; j < words.length; j++) {
                    int randomint = rand.nextInt(4);
                    if (randomint == 2) {
                        if (!ProxTextConfig.UNDERSCOREMODE.get()) {
                            words[j] = ProxTextConfig.WORD_REPLACEMENT.get();
                        } else {
                            String newString = "";
                            for (int e = 0; e  < words[j].length(); e++) {
                                newString = newString + "_";
                            }
                            words[j] = newString;
                        }
                    }
                    newmessage = newmessage + words[j] + " ";
                }
                playerList.getPlayers().get(i).sendMessage(new StringTextComponent("<" + event.getPlayer().getName().getString() + "> " + newmessage), ChatType.SYSTEM, Util.NIL_UUID);
            }
        }

        event.setCanceled(true);
    }
}

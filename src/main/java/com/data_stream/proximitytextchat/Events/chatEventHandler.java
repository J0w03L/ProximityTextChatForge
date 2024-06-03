package com.data_stream.proximitytextchat.Events;

import com.data_stream.proximitytextchat.Config.ProxTextConfig;
import com.data_stream.proximitytextchat.Main;
import net.minecraft.Util;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class chatEventHandler {
    @SubscribeEvent
    public static void chatEvent(ServerChatEvent event) {
        final BlockPos playerPos = event.getPlayer().blockPosition();
        final PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
        List<String> playersHeardFull = new ArrayList<String>();
        List<String> playersHeardPartial = new ArrayList<String>();

        for (int i = 0; i < playerList.getPlayers().size(); i++) {
            final double distance = Math.sqrt(playerList.getPlayers().get(i).blockPosition().distSqr(playerPos));
            if (distance <= ProxTextConfig.PROX_RANGE.get()) {
                if (playerList.getPlayers().get(i).getUUID() != event.getPlayer().getUUID()) {
                    playersHeardFull.add(playerList.getPlayers().get(i).getName().getString());
                }

                playerList.getPlayers().get(i).sendMessage(new TextComponent("<" + event.getPlayer().getName().getString() + "> " + event.getMessage().toString()), ChatType.SYSTEM, Util.NIL_UUID);
            }
            if (distance >= ProxTextConfig.PROX_RANGE.get() && distance <= ProxTextConfig.DIST_RANGE.get() && ProxTextConfig.TOGGLEDISTORTION.get()) {
                if (playerList.getPlayers().get(i).getUUID() != event.getPlayer().getUUID()) {
                    playersHeardPartial.add(playerList.getPlayers().get(i).getName().getString());
                }

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
                playerList.getPlayers().get(i).sendMessage(new TextComponent("<" + event.getPlayer().getName().getString() + "> " + newmessage), ChatType.SYSTEM, Util.NIL_UUID);
            }
        }

        LogUtils.getLogger().info("<" + event.getPlayer().getName().getString() + ">[" + String.join(", ", playersHeardFull) + "](" + String.join(", ", playersHeardPartial) + ") " + event.getMessage().toString());

        event.setCanceled(true);
    }
}

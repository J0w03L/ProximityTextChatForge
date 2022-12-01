package com.data_stream.proximitytextchat.Events;

import com.data_stream.proximitytextchat.Main;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

public class chatEventHandler {
    @SubscribeEvent
    public static void chatEvent(ServerChatEvent.Submitted event) {
        final BlockPos playerPos = event.getPlayer().blockPosition();
        final PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for (int i = 0; i < playerList.getPlayers().size(); i++) {
            if (Math.sqrt(playerList.getPlayers().get(i).blockPosition().distSqr(playerPos)) < 50) {
                playerList.getPlayers().get(i).sendSystemMessage(Component.literal("<" + event.getPlayer().getName().getString() + "> " + event.getMessage().getString()));
            }
        }

        event.setCanceled(true);
    }
}

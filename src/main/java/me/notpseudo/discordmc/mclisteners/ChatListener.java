package me.notpseudo.discordmc.mclisteners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.notpseudo.discordmc.DiscordMC;
import me.notpseudo.discordmc.dclisteners.MessageListener;
import me.notpseudo.discordmc.mcutils.CustomChatRenderer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    public ChatListener(DiscordMC plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(new CustomChatRenderer());
        MessageListener.sendMessageToDiscord(event);
    }

}
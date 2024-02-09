package me.notpseudo.discordmc.mclisteners;

import me.notpseudo.discordmc.DiscordMC;
import me.notpseudo.discordmc.dclisteners.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MCEventsListener implements Listener {

    public MCEventsListener(DiscordMC plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        MessageListener.sendJoinMessage(event);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        MessageListener.sendLeaveMessage(event);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        MessageListener.sendDeathMessage(event);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        MessageListener.sendAchievementMessage(event);
    }

}
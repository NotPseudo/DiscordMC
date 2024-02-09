package me.notpseudo.discordmc;

import me.notpseudo.discordmc.dclisteners.DCCommandListeners;
import me.notpseudo.discordmc.dclisteners.MessageListener;
import me.notpseudo.discordmc.mccommands.DisableCommand;
import me.notpseudo.discordmc.mccommands.EnableCommand;
import me.notpseudo.discordmc.mccommands.ReloadDCCommand;
import me.notpseudo.discordmc.mclisteners.ChatListener;
import me.notpseudo.discordmc.mclisteners.MCEventsListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommand;

import java.util.concurrent.CompletionException;
import java.util.logging.Level;

public final class DiscordMC extends JavaPlugin {

    private static DiscordMC plugin;
    private static DiscordApi api;
    private static ChatListener chatListener;
    private static MCEventsListener MCEventsListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        if (!enableDiscordAPI()) {
            return;
        }
        new DisableCommand(this);
        new EnableCommand(this);
        new ReloadDCCommand(this);
    }

    @Override
    public void onDisable() {
        disableDiscordAPI();
    }

    private static void disablePlugin(String reason) {
        Bukkit.getLogger().log(Level.WARNING, "DiscordMC was disabled because of: " + reason);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public static DiscordMC getPlugin() {
        return plugin;
    }

    public static DiscordApi getDiscordAPI() {
        return api;
    }

    public static boolean enableDiscordAPI() {
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            String TOKEN = plugin.getConfig().getString("bot_token");
            String SERVER_ID = plugin.getConfig().getString("server_id");
            String CHANNEL_ID = plugin.getConfig().getString("channel_id");
            if (TOKEN == null) {
                disablePlugin("bot_token not specified in config.yml");
                return false;
            }
            if (SERVER_ID == null) {
                disablePlugin("server_token not specified in config.yml");
                return false;
            }
            if (CHANNEL_ID == null) {
                disablePlugin("channel_token not specified in config.yml");
                return false;
            }
            api = new DiscordApiBuilder().setToken(TOKEN).addIntents(Intent.GUILD_MEMBERS, Intent.GUILD_PRESENCES, Intent.GUILD_MESSAGES).login().join();
            SlashCommand.with("online", "View who's on the Minecraft server").createGlobal(api).join();
            api.updateActivity(ActivityType.WATCHING, "your Minecraft and Discord servers");
            api.addListener(new MessageListener(SERVER_ID, CHANNEL_ID));
            api.addListener(new DCCommandListeners());
            chatListener = new ChatListener(plugin);
            MCEventsListener = new MCEventsListener(plugin);
            return true;
        } catch (CompletionException exception) {
            disablePlugin("There was an error starting the Discord bot");
            return false;
        }
    }

    public static void disableDiscordAPI() {
        HandlerList.unregisterAll(chatListener);
        HandlerList.unregisterAll(MCEventsListener);
        if (api != null) {
            api.disconnect();
        }
        api = null;
    }

    public static void reloadDiscordAPI() {
        HandlerList.unregisterAll(chatListener);
        HandlerList.unregisterAll(MCEventsListener);
        disableDiscordAPI();
        enableDiscordAPI();
    }

}
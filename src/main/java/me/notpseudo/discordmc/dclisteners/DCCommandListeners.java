package me.notpseudo.discordmc.dclisteners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.awt.*;

public class DCCommandListeners implements SlashCommandCreateListener {

    public DCCommandListeners() {

    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        InteractionOriginalResponseUpdater responseUpdater = interaction.respondLater().join();
        interaction.getServer().ifPresentOrElse(s -> {
                    switch (interaction.getCommandName()) {
                        case "online" -> {
                            String online = "";
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                online += player.getName() + "\n";
                            }
                            if (online.equals("")) {
                                online = " ";
                            }
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("**Online Players**")
                                    .setDescription("There are currently **" + Bukkit.getOnlinePlayers().size() + "** players online")
                                    .addField("Player List:", "```" + online + "```")
                                    .setColor(Color.CYAN)
                                    .setTimestampToNow();
                            responseUpdater.addEmbed(embed).update();
                        }
                    }
                },
                () -> {
                    responseUpdater.setContent("Use this command in a server").update();
                });
    }

}
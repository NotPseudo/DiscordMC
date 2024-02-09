package me.notpseudo.discordmc.dclisteners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.notpseudo.discordmc.DiscordMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;

public class MessageListener implements MessageCreateListener {

    private static String serverID;
    private static String channelID;

    public MessageListener(String serverID, String channelID) {
        MessageListener.serverID = serverID;
        MessageListener.channelID = channelID;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (!event.getChannel().getIdAsString().equals(channelID)) {
            return;
        }
        event.getServer().ifPresentOrElse(s -> {
            if (!s.getIdAsString().equals(serverID)) {
                return;
            }
            event.getMessage().getAuthor().asUser().ifPresentOrElse(u -> {
                if (u.isBot()) {
                    return;
                }
                String name = s.getNickname(u).orElse(u.getName());
                String message = event.getMessageContent();
                Color color = s.getRoleColor(u).orElse(Color.WHITE);
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                TextColor mcColor = TextColor.fromHexString(hex);
                Component toMinecraft = Component.text("[DC] ", NamedTextColor.BLUE).append(Component.text(name, mcColor)).append(Component.text(": " + message, NamedTextColor.WHITE))
                .hoverEvent(HoverEvent.showText(Component.text("Discord ", NamedTextColor.BLUE).append(Component.text("message sent by ", NamedTextColor.WHITE)).append(Component.text(name + " (" + u.getDiscriminatedName() + ")", mcColor))))
                .clickEvent(ClickEvent.openUrl(event.getMessageLink()));
                Bukkit.getServer().broadcast(toMinecraft);
            }, () -> {
            });
        }, () -> {});
    }

    public static void sendMessageToDiscord(AsyncChatEvent event) {
        DiscordApi api = DiscordMC.getDiscordAPI();
        if (api == null) {
            return;
        }
        api.getChannelById(channelID).ifPresent(c -> {
            if (!(c instanceof TextChannel channel)) {
                return;
            }
            Player player = event.getPlayer();
            World.Environment env = player.getWorld().getEnvironment();
            String name = player.getName();
            String content = componentToString(event.message());
            String namemc = "https://namemc.com/profile/" + name;
            String mcheads = "https://mc-heads.net/avatar/" + name + "/100";
            EmbedBuilder builder = new EmbedBuilder().setDescription(content).setTimestampToNow().setAuthor(name, namemc, mcheads).setColor(Color.decode("#5865F2"));
            channel.sendMessage(builder);
        });
    }

    public static void sendJoinMessage(PlayerJoinEvent event) {
        DiscordApi api = DiscordMC.getDiscordAPI();
        if (api == null) {
            return;
        }
        api.getChannelById(channelID).ifPresent(c -> {
            if (!(c instanceof TextChannel channel)) {
                return;
            }
            Player player = event.getPlayer();
            String name = player.getName();
            String namemc = "https://namemc.com/profile/" + name;
            String mcheads = "https://mc-heads.net/avatar/" + name + "/100";
            EmbedBuilder builder = new EmbedBuilder().setTimestampToNow().setAuthor(name + " joined", namemc, mcheads).setColor(Color.GREEN);
            channel.sendMessage(builder);
        });
    }

    public static void sendLeaveMessage(PlayerQuitEvent event) {
        DiscordApi api = DiscordMC.getDiscordAPI();
        if (api == null) {
            return;
        }
        api.getChannelById(channelID).ifPresent(c -> {
            if (!(c instanceof TextChannel channel)) {
                return;
            }
            Player player = event.getPlayer();
            String name = player.getName();
            String namemc = "https://namemc.com/profile/" + name;
            String mcheads = "https://mc-heads.net/avatar/" + name + "/100";
            EmbedBuilder builder = new EmbedBuilder().setTimestampToNow().setAuthor(name + " left", namemc, mcheads).setColor(Color.RED);
            channel.sendMessage(builder);
        });
    }

    public static void sendDeathMessage(PlayerDeathEvent event) {
        DiscordApi api = DiscordMC.getDiscordAPI();
        if (api == null) {
            return;
        }
        api.getChannelById(channelID).ifPresent(c -> {
            if (!(c instanceof TextChannel channel)) {
                return;
            }
            Player player = event.getPlayer();
            String name = player.getName();
            String namemc = "https://namemc.com/profile/" + name;
            String mcheads = "https://mc-heads.net/avatar/" + name + "/100";
            EmbedBuilder builder = new EmbedBuilder().setTimestampToNow().setAuthor(componentToString(event.deathMessage()), namemc, mcheads).setColor(Color.RED);
            channel.sendMessage(builder);
        });
    }

    public static void sendAchievementMessage(PlayerAdvancementDoneEvent event) {
        DiscordApi api = DiscordMC.getDiscordAPI();
        if (api == null) {
            return;
        }
        api.getChannelById(channelID).ifPresent(c -> {
            if (!(c instanceof TextChannel channel)) {
                return;
            }
            Player player = event.getPlayer();
            String name = player.getName();
            String namemc = "https://namemc.com/profile/" + name;
            String mcheads = "https://mc-heads.net/avatar/" + name + "/100";
            EmbedBuilder builder = new EmbedBuilder().setTimestampToNow().setAuthor(componentToString(event.message()), namemc, mcheads).setColor(Color.YELLOW);
            channel.sendMessage(builder);
        });
    }

    public static String componentToString(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    private static Color getEnvironmentColor(World.Environment env) {
        return switch (env) {
            case NORMAL -> Color.GREEN;
            case NETHER -> Color.RED;
            case THE_END -> Color.MAGENTA;
            default -> Color.WHITE;
        };
    }

    private static String getEnvironmentName(World.Environment env) {
        return capitalizeFirst(env == World.Environment.NORMAL ? "Overworld" : env.name());
    }

    public static String capitalizeFirst(String string) {
        String[] split = string.split("_");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            name.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1).toLowerCase());
            if (i < split.length - 1) {
                name.append(" ");
            }
        }
        return name.toString();
    }

}
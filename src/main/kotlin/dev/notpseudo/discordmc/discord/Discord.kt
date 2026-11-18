package dev.notpseudo.discordmc.discord

import dev.notpseudo.discordmc.discord.config.DiscordSettings
import dev.notpseudo.discordmc.discord.listeners.MessageListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.minecraft.advancements.Advancement
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.TextColor
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level
import java.awt.Color
import java.net.URI

class Discord(private val currentSettings: () -> DiscordSettings) {
    private val jda: JDA = JDABuilder.createDefault(currentSettings().botToken)
        .addEventListeners(MessageListener(this))
        .build()

    @Volatile
    private var minecraftServer: MinecraftServer? = null

    fun attachServer(server: MinecraftServer) {
        minecraftServer = server
    }

    fun detachServer() {
        minecraftServer = null
    }

    fun onPlayerJoin(name: String) {

    }

    fun onPlayerLeave(name: String) {

    }

    fun onPlayerAdvancement(name: String, advancement: Advancement) {

    }

    fun onPlayerWorldChange(name: String, origin: Level, destination: Level) {

    }

    fun onDiscordMessage(sender: Member, message: Message) {
        val config = currentSettings()
        if (!config.sendDiscordChat) return

        val name = sender.effectiveName
        val roleColor = sender.colors.primary?: Color.WHITE

        val content = message.contentStripped

        val messageUrl: URI = URI.create(message.jumpUrl)

        val toMinecraft = Component.literal("");
        if (config.discordIndicatorPrefix.isNotEmpty()) {
            toMinecraft.append(Component.literal("${config.discordIndicatorPrefix} ").withColor(config.discordIndicatorColor.rgb))
        }
        toMinecraft.append(Component.literal(name).withColor(roleColor.rgb))
        toMinecraft.append(Component.literal(": $content").withColor(TextColor.WHITE))

        val hoverComponent = Component.literal("Discord").withColor(config.discordIndicatorColor.rgb)
        hoverComponent.append(Component.literal(" message sent by ").withColor(TextColor.WHITE))
        hoverComponent.append(Component.literal(name).withColor(roleColor.rgb))

        toMinecraft.withStyle() { style ->
            style
                .withHoverEvent(HoverEvent.ShowText(hoverComponent))
                .withClickEvent(ClickEvent.OpenUrl(messageUrl))
        }

        val server = minecraftServer?: return
        server.execute {
            server.playerList.broadcastSystemMessage(toMinecraft, false)
        }
    }
}
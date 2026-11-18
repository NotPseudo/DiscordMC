package dev.notpseudo.discordmc.discord

import dev.notpseudo.discordmc.config.Settings
import dev.notpseudo.discordmc.discord.config.DiscordSettings
import dev.notpseudo.discordmc.discord.listeners.MessageListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.minecraft.advancements.Advancement
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.TextColor
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level
import java.awt.Color
import java.net.URI
import java.time.Instant

class Discord(val currentSettings: () -> Settings) {
    private val jda: JDA = JDABuilder.createDefault(currentSettings().discord.botToken)
        .addEventListeners(MessageListener(this))
        .build()

    enum class MinecraftToDiscordMessageType {
        CHAT, JOIN, LEAVE, ADVANCEMENT, WORLD
    }

    data class MinecraftPlayerInfo(
        val namemcURL: String,
        val mcheadsURL: String
    )

    @Volatile
    private var minecraftServer: MinecraftServer? = null

    fun attachServer(server: MinecraftServer) {
        minecraftServer = server
    }

    fun detachServer() {
        minecraftServer = null
    }

    fun getMinecraftPlayerInfo(name: String): MinecraftPlayerInfo {
        return MinecraftPlayerInfo(
            namemcURL = " https://namemc.com/profile/$name",
            mcheadsURL =  "https://mc-heads.net/avatar/$name/100"
        )
    }

    fun getChannelIdForMessageType(messageType: MinecraftToDiscordMessageType): String {
        val config = currentSettings().discord
        val id = when (messageType) {
            MinecraftToDiscordMessageType.CHAT -> config.chatChannelId
            MinecraftToDiscordMessageType.JOIN, MinecraftToDiscordMessageType.LEAVE -> if (config.separateJoinLeave) config.joinLeaveChannelId else null
            MinecraftToDiscordMessageType.ADVANCEMENT -> if (config.separateAdvancement) config.advancementChannelId else null
            MinecraftToDiscordMessageType.WORLD -> if (config.separateWorldChange) config.worldChangeChannelId else null
        }
        return id?.trim()?.takeIf { it.isNotEmpty() } ?: config.chatChannelId
    }

    fun sendMessageToDiscord(embed: MessageEmbed, channelId: String) {
        val channel = jda.getTextChannelById(channelId) ?: return
        channel.sendMessageEmbeds(embed).queue()
    }

    fun sendMessageToMinecraft(component: Component) {
        val server = minecraftServer?: return
        server.execute {
            server.playerList.broadcastSystemMessage(component, false)
        }
    }

    fun onPlayerJoin(name: String) {
        if (!currentSettings().minecraft.sendPlayerJoinLeave) return
        val playerInfo = getMinecraftPlayerInfo(name)
        EmbedBuilder().setTimestamp(Instant.now()).setAuthor("$name joined", playerInfo.namemcURL, playerInfo.mcheadsURL).setColor(Color.GREEN).build()
    }

    fun onPlayerLeave(name: String) {
        if (!currentSettings().minecraft.sendPlayerJoinLeave) return
        val playerInfo = getMinecraftPlayerInfo(name)
        EmbedBuilder().setTimestamp(Instant.now()).setAuthor("$name joined", playerInfo.namemcURL, playerInfo.mcheadsURL).setColor(Color.RED).build()
    }

    fun onPlayerAdvancement(name: String, advancement: Advancement) {
        if (!currentSettings().minecraft.sendPlayerAdvancement) return
    }

    fun onPlayerWorldChange(name: String, origin: Level, destination: Level) {
        if (!currentSettings().minecraft.sendPlayerWorldChange) return
    }

    fun onDiscordMessage(sender: Member, message: Message) {
        val config = currentSettings().discord
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

        sendMessageToMinecraft(toMinecraft)
    }
}
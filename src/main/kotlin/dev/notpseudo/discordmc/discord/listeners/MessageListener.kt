package dev.notpseudo.discordmc.discord.listeners

import dev.notpseudo.discordmc.discord.Discord
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import java.awt.Color

class MessageListener(private val discord: Discord) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.channel.id.equals(discord.currentSettings().discord.chatChannelId, ignoreCase = true)) return
        if (!event.isFromType(ChannelType.TEXT)) return
        val message = event.message
        if (message.isWebhookMessage) return
        val sender = message.member
        if (sender == null || sender.user.isBot) return
        discord.onDiscordMessage(sender, message)
    }

}
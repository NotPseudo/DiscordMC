package dev.notpseudo.discordmc.discord.listeners

import dev.notpseudo.discordmc.discord.Discord
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener(private val discord: Discord) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromType(ChannelType.TEXT)) return
        val config = discord.currentSettings().discord
        if (event.channel.id != config.chatChannelId.trim()) return
        if (config.discordServerId.isNotBlank() && event.guild.id != config.discordServerId.trim()) return
        val message = event.message
        if (message.isWebhookMessage) return
        val sender = message.member
        if (sender == null || sender.user.isBot) return
        discord.onDiscordMessage(sender, message)
    }

}
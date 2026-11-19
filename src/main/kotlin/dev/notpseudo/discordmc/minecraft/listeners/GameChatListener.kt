package dev.notpseudo.discordmc.minecraft.listeners

import dev.notpseudo.discordmc.discord.Discord
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents

class GameChatListener(val discord: Discord): Listener {

    override fun register() {
        ServerMessageEvents.CHAT_MESSAGE.register { message, sender, _ ->
            val text = message.decoratedContent().string
            discord.onMinecraftMessage(sender, text)
        }
    }
}
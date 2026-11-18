package dev.notpseudo.discordmc.minecraft.listeners

import dev.notpseudo.discordmc.discord.Discord
import dev.notpseudo.discordmc.minecraft.events.PlayerAdvancementCallback

class PlayerAdvancementListener(private val discord: Discord) : Listener {

    override fun register() {
        PlayerAdvancementCallback.EVENT.register { player, holder ->
            if (holder.value().display().isPresent) {
                discord.onPlayerAdvancement(player, holder)
            }
        }
    }
}

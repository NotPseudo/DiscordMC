package dev.notpseudo.discordmc.minecraft.listeners

import dev.notpseudo.discordmc.discord.Discord
import dev.notpseudo.discordmc.minecraft.events.PlayerAdvancementCallback

class PlayerAdvancementListener(private val discord: Discord) : Listener {

    override fun register() {
        PlayerAdvancementCallback.EVENT.register { player, advancement ->
            if (advancement.display().isPresent) {
                discord.onPlayerAdvancement(player.displayName.string, advancement)
            }
        }
    }
}

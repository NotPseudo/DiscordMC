package dev.notpseudo.discordmc.minecraft.listeners

import dev.notpseudo.discordmc.discord.Discord
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents

class PlayerWorldListener(val discord: Discord): Listener {

    override fun register() {
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register { player, origin, destination ->
            discord.onPlayerWorldChange(player.displayName.string, origin, destination)
        }
    }
}
package dev.notpseudo.discordmc.minecraft.listeners

import dev.notpseudo.discordmc.discord.Discord
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class PlayerJoinLeaveListener(val discord : Discord) : Listener {

    override fun register() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            discord.onPlayerJoin(handler.player.displayName.string)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            discord.onPlayerLeave(handler.player.displayName.string)
        }
    }
}
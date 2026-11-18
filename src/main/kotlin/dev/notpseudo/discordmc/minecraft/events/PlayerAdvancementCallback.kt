package dev.notpseudo.discordmc.minecraft.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancements.Advancement
import net.minecraft.world.entity.player.Player

fun interface PlayerAdvancementCallback  {

    companion object {
        val EVENT: Event<PlayerAdvancementCallback> = EventFactory.createArrayBacked(PlayerAdvancementCallback::class.java) {
            listeners ->
            PlayerAdvancementCallback { player, advancement ->
                for (listener in listeners) {
                    listener.onAdvancementCompleted(player, advancement)
                }
            }
        }

    }


    fun onAdvancementCompleted(player: Player, advancement: Advancement)
}
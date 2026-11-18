package dev.notpseudo.discordmc

import dev.notpseudo.discordmc.config.Settings
import dev.notpseudo.discordmc.discord.Discord
import dev.notpseudo.discordmc.discord.config.DiscordSettings
import dev.notpseudo.discordmc.minecraft.config.MinecraftSettings
import dev.notpseudo.discordmc.minecraft.listeners.PlayerAdvancementListener
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

object DiscordMC : ModInitializer {
	const val MOD_ID: String = "discordmc"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)

	@Volatile
	private var settings: Settings = Settings()

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		val discord = Discord { settings }
		ServerLifecycleEvents.SERVER_STARTED.register { server ->
			discord.attachServer(server)
		}
		ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
			discord.detachServer()
		}

		PlayerAdvancementListener(discord).register()

		LOGGER.info("Hello Fabric world!")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}

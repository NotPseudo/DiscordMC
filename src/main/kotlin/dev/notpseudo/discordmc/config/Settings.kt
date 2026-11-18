package dev.notpseudo.discordmc.config

import dev.notpseudo.discordmc.discord.config.DiscordSettings
import dev.notpseudo.discordmc.minecraft.config.MinecraftSettings

data class Settings(
    val discord: DiscordSettings = DiscordSettings(),
    val minecraft: MinecraftSettings = MinecraftSettings(),
)

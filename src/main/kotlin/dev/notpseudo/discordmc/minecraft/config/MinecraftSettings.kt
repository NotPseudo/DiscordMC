package dev.notpseudo.discordmc.minecraft.config

data class MinecraftSettings(
    val sendPlayerChat: Boolean = true,
    val sendPlayerJoinLeave: Boolean = true,
    val sendPlayerAdvancement: Boolean = false,
    val sendPlayerWorldChange: Boolean = false,
    val showPlayerWorld: Boolean = false,
)

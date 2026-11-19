package dev.notpseudo.discordmc.minecraft.config

import com.google.gson.annotations.SerializedName

data class MinecraftSettings(
    @SerializedName("send_player_chat")
    val sendPlayerChat: Boolean = true,

    @SerializedName("send_player_join_leave")
    val sendPlayerJoinLeave: Boolean = true,

    @SerializedName("send_player_advancement")
    val sendPlayerAdvancement: Boolean = false,

    @SerializedName("send_player_world_change")
    val sendPlayerWorldChange: Boolean = false,

    @SerializedName("show_player_world")
    val showPlayerWorld: Boolean = false,
)

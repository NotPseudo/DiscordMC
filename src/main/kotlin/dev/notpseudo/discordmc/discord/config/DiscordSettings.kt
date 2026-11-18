package dev.notpseudo.discordmc.discord.config

import com.google.gson.annotations.SerializedName
import java.awt.Color

data class DiscordSettings(
    @SerializedName("bot_token")
    val botToken: String = "",

    @SerializedName("discord_server_id")
    val discordServerId: String = "",

    @SerializedName("send_discord_chat")
    val sendDiscordChat: Boolean = true,

    @SerializedName("chat_channel_id")
    val chatChannelId: String = "",

    @SerializedName("discord_indicator_prefix")
    val discordIndicatorPrefix: String = "[DC]",

    @SerializedName("discord_indicator_color")
    val discordIndicatorColor: Color = Color.BLUE,

    @SerializedName("separate_join_leave")
    val separateJoinLeave: Boolean = false,

    @SerializedName("join_leave_channel_id")
    val joinLeaveChannelId: String? = null,

    @SerializedName("separate_advancement")
    val separateAdvancement: Boolean = false,

    @SerializedName("advancement_channel_id")
    val advancementChannelId: String? = null,

    @SerializedName("separate_world_change")
    val separateWorldChange: Boolean = false,

    @SerializedName("world_change_channel_id")
    val worldChangeChannelId: String? = null,
)

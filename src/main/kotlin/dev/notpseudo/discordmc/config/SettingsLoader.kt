package dev.notpseudo.discordmc.config

import com.google.gson.Gson
import dev.notpseudo.discordmc.discord.config.DiscordSettings
import dev.notpseudo.discordmc.minecraft.config.MinecraftSettings
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SettingsLoader {

    private val gson = Gson()

    internal fun loadSettings(): Settings {
        val directory = FabricLoader.getInstance().configDir.resolve("discordmc")

        Files.createDirectories(directory)

        val discord = loadOrCreate(directory.resolve("discord_settings.json"), DiscordSettings())
        val minecraft = loadOrCreate(directory.resolve("minecraft_settings.json"), MinecraftSettings())

        return Settings(discord, minecraft)
    }

    private inline fun <reified T : Any> loadOrCreate(path: Path, defaults: T): T {
        try {
            Files.writeString(
                path,
                gson.toJson(defaults) + "\n",
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE
            )
            return defaults
        } catch (_: FileAlreadyExistsException) {
            // Read the existing config below.
        }

        return Files.newBufferedReader(path).use { reader ->
            requireNotNull(gson.fromJson(reader, T::class.java)) {
                "${path.fileName} must contain a settings object"
            }
        }
    }

}
package me.notpseudo.discordmc.mccommands;

import me.notpseudo.discordmc.DiscordMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadDCCommand implements CommandExecutor {

    public ReloadDCCommand(DiscordMC plugin) {
        plugin.getCommand("reloaddiscord").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("You don't have permission to use this command", NamedTextColor.RED));
            return true;
        }
        DiscordMC.reloadDiscordAPI();
        sender.sendMessage(Component.text("Reloaded the Discord bot and channels", NamedTextColor.GREEN));
        return true;
    }
}

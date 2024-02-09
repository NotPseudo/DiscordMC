package me.notpseudo.discordmc.mccommands;

import me.notpseudo.discordmc.DiscordMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DisableCommand implements CommandExecutor {

    public DisableCommand(DiscordMC plugin) {
        plugin.getCommand("disable").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("You don't have permission to use this command", NamedTextColor.RED));
            return true;
        }
        DiscordMC.disableDiscordAPI();
        sender.sendMessage(Component.text("Disabled the Discord bot", NamedTextColor.YELLOW));
        return true;
    }
}

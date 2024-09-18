package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SetSpawnCommand implements CommandExecutor {

    private static final Component MSG_ONLY_PLAYERS = Component.text("This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_NO_PERMISSION = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component MSG_SPAWN_SET_SUCCESS = Component.text("Spawn location set successfully!", NamedTextColor.GREEN);

    private final EssentialsPro plugin;

    public SetSpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.setspawn")) {
            player.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        try {
            plugin.setSpawnLocation(player.getLocation());
            player.sendMessage(MSG_SPAWN_SET_SUCCESS);
        } catch (Exception e) {
            player.sendMessage(Component.text("An error occurred while setting spawn location: " + e.getMessage(), NamedTextColor.RED));
            return true;
        }

        return true;
    }
}
package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

public class FeedCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    // Common messages using Adventure components with Unicode symbols.
    private static final Component NO_PERMISSION = MessageUtils.noPermission();
    private static final Component ONLY_PLAYERS = MessageUtils.onlyPlayers();
    private static final Component PLAYER_NOT_FOUND = MessageUtils.playerNotFound();
    private static final Component SELF_FEED_SUCCESS = Component.text("✔ You have been fed.", NamedTextColor.GREEN);
    private static final String OTHERS_FEED_SUCCESS_FORMAT = "✔ You have fed %s.";
    private static final String TARGET_FEED_MESSAGE_FORMAT = "✔ You have been fed by %s.";

    public FeedCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is executed by a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        // Check if the player has permission to feed.
        if (!player.hasPermission(Permissions.FEED)) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // If an argument is provided, attempt to feed another player.
        if (args.length > 0) {
            // Verify permission to feed others.
            if (!player.hasPermission(Permissions.FEED_OTHERS)) {
                player.sendMessage(NO_PERMISSION);
                return true;
            }
            Player target = PlayerLookup.findOnlinePlayer(args[0]);
            if (target == null) {
                player.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }
            feedPlayer(target);
            // Inform both the sender and the target.
            player.sendMessage(Component.text(String.format(OTHERS_FEED_SUCCESS_FORMAT, target.getName()), NamedTextColor.GREEN));
            target.sendMessage(Component.text(String.format(TARGET_FEED_MESSAGE_FORMAT, player.getName()), NamedTextColor.GREEN));
        } else {
            // No arguments: feed the sender.
            feedPlayer(player);
            player.sendMessage(SELF_FEED_SUCCESS);
        }
        return true;
    }

    /**
     * Fully restores the target player's food level and saturation.
     *
     * @param target The player to feed.
     */
    private void feedPlayer(Player target) {
        target.setFoodLevel(20);
        target.setSaturation(5.0f);
    }
}
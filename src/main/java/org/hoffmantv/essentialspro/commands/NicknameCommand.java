package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameCommand implements CommandExecutor {

    private final EssentialsPro plugin;
    private final Map<UUID, String> nicknames;
    private FileConfiguration nicknameConfig;
    private File nicknameFile;

    public NicknameCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        this.nicknames = new HashMap<>();
        this.nicknameFile = new File(plugin.getDataFolder(), "nicknames.yml");
        reloadNicknameConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /nickname <nickname>");
            return true;
        }

        String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);

        // Check if the nickname is already taken
        if (isNicknameTaken(nickname)) {
            player.sendMessage(ChatColor.RED + "The nickname '" + nickname + "' is already taken. Please choose another one.");
            return true;
        }

        // You can add additional checks here to ensure the nickname is valid and not offensive, etc.

        // Update the nickname in the nicknames map
        nicknames.put(player.getUniqueId(), nickname);

        // Update the player's display name
        player.setDisplayName(nickname);

        // Save the nickname to the nicknames.yml file
        saveNickname(player.getUniqueId(), nickname);

        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to: " + nickname);

        return true;
    }

    private boolean isNicknameTaken(String nickname) {
        for (String existingNickname : nicknames.values()) {
            if (existingNickname.equalsIgnoreCase(nickname)) {
                return true;
            }
        }
        return false;
    }

    private void reloadNicknameConfig() {
        if (!nicknameFile.exists()) {
            plugin.saveResource("nicknames.yml", false);
        }
        nicknameConfig = plugin.getConfig();
        loadNicknames();
    }

    private void loadNicknames() {
        ConfigurationSection section = nicknameConfig.getConfigurationSection("nicknames");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID playerUUID = UUID.fromString(key);
                String nickname = section.getString(key);
                nicknames.put(playerUUID, nickname);
            }
        }
    }

    private void saveNickname(UUID playerUUID, String nickname) {
        nicknameConfig.set("nicknames." + playerUUID.toString(), nickname);
        try {
            nicknameConfig.save(nicknameFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

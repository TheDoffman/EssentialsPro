package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
            removeNickname(player);
        } else {
            String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);
            setNickname(player, nickname);
        }

        return true;
    }

    private boolean isNicknameTaken(String nickname) {
        return nicknames.containsValue(nickname);
    }

    private void removeNickname(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (nicknames.containsKey(playerUUID)) {
            nicknames.remove(playerUUID);
            player.setDisplayName(player.getName());
            nicknameConfig.set("nicknames." + playerUUID.toString(), null);
            saveNicknameConfig();
            player.sendMessage(ChatColor.GREEN + "Your nickname has been removed.");
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a nickname set.");
        }
    }

    private void setNickname(Player player, String nickname) {
        if (isNicknameTaken(nickname)) {
            player.sendMessage(ChatColor.RED + "The nickname '" + nickname + "' is already taken. Please choose another one.");
            return;
        }

        // Update the nickname in the nicknames map and save it
        UUID playerUUID = player.getUniqueId();
        nicknames.put(playerUUID, nickname);
        nicknameConfig.set("nicknames." + playerUUID.toString(), nickname);
        saveNicknameConfig();

        // Update the player's display name
        player.setDisplayName(nickname);
        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to: " + nickname);
    }

    private void reloadNicknameConfig() {
        if (!nicknameFile.exists()) {
            try {
                nicknameFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        nicknameConfig = YamlConfiguration.loadConfiguration(nicknameFile);
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

    private void saveNicknameConfig() {
        try {
            nicknameConfig.save(nicknameFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

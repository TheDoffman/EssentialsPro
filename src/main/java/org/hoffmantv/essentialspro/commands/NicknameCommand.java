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
        nicknames = new HashMap<>();
        nicknameFile = new File(plugin.getDataFolder(), "nicknames.yml");
        reloadNicknameConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        String nickname = args.length > 0 ? ChatColor.translateAlternateColorCodes('&', args[0]) : null;

        if (nickname == null) {
            removeNickname(player);
        } else {
            setNickname(player, nickname);
        }

        return true;
    }

    private void removeNickname(Player player) {
        UUID playerId = player.getUniqueId();
        if (nicknames.containsKey(playerId)) {
            nicknames.remove(playerId);
            player.setDisplayName(player.getName());
            nicknameConfig.set(String.format("nicknames.%s", playerId), null);
            saveNicknameConfig();
            player.sendMessage(ChatColor.GREEN + "Your nickname has been removed.");
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a nickname set.");
        }
    }

    private void setNickname(Player player, String nickname) {
        if (nicknames.containsValue(nickname)) {
            player.sendMessage(ChatColor.RED + String.format("The nickname '%s' is already taken. Please choose another one.", nickname));
            return;
        }

        UUID playerId = player.getUniqueId();
        nicknames.put(playerId, nickname);
        nicknameConfig.set(String.format("nicknames.%s", playerId), nickname);
        saveNicknameConfig();

        player.setDisplayName(nickname);
        player.sendMessage(ChatColor.GREEN + String.format("Your nickname has been set to: %s", nickname));
    }

    private void reloadNicknameConfig() {
        if (!nicknameFile.exists()) {
            createNewNicknameFile();
        }

        nicknameConfig = YamlConfiguration.loadConfiguration(nicknameFile);
        loadNicknames();
    }

    private void createNewNicknameFile() {
        try {
            nicknameFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNicknames() {
        ConfigurationSection section = nicknameConfig.getConfigurationSection("nicknames");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID playerId = UUID.fromString(key);
                String nickname = section.getString(key);
                nicknames.put(playerId, nickname);
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

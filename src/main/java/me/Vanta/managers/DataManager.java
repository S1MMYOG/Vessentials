package me.Vanta.managers;

import me.Vanta.Vessentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {

    private final Vessentials plugin;
    private final File userFolder;

    // Simple in-memory caches
    private final Map<UUID, FileConfiguration> cache = new HashMap<>();
    private final Set<UUID> muted = new HashSet<>();

    public DataManager(Vessentials plugin) {
        this.plugin = plugin;
        this.userFolder = new File(plugin.getDataFolder(), "players");
        if (!userFolder.exists()) userFolder.mkdirs();
    }

    private File getFile(UUID uuid) {
        return new File(userFolder, uuid.toString() + ".yml");
    }

    public FileConfiguration loadPlayer(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        File f = getFile(uuid);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cache.put(uuid, cfg);
        return cfg;
    }

    public void savePlayerData(UUID uuid) {
        File f = getFile(uuid);
        FileConfiguration cfg = loadPlayer(uuid);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            plugin.getLogger().severe("Failed to save data for " + uuid);
            ex.printStackTrace();
        }
    }

    public void saveAll() {
        for (UUID id : new ArrayList<>(cache.keySet())) {
            savePlayerData(id);
        }
    }

    // convenience methods
    public boolean hasPlayedBefore(UUID uuid) {
        File f = getFile(uuid);
        return f.exists();
    }

    public void markPlayed(UUID uuid) {
        FileConfiguration cfg = loadPlayer(uuid);
        cfg.set("first-join", System.currentTimeMillis());
        savePlayerData(uuid);
    }

    public boolean isMuted(UUID uuid) {
        FileConfiguration cfg = loadPlayer(uuid);
        return cfg.getBoolean("muted", false);
    }

    public void setMuted(UUID uuid, boolean muted) {
        FileConfiguration cfg = loadPlayer(uuid);
        cfg.set("muted", muted);
        savePlayerData(uuid);
    }

    public boolean isVanished(UUID uuid) {
        FileConfiguration cfg = loadPlayer(uuid);
        return cfg.getBoolean("vanished", false);
    }

    public void setVanished(UUID uuid, boolean v) {
        FileConfiguration cfg = loadPlayer(uuid);
        cfg.set("vanished", v);
        savePlayerData(uuid);
    }

    public String getPrefix(UUID uuid) {
        FileConfiguration cfg = loadPlayer(uuid);
        return cfg.getString("prefix", "");
    }

    public void mutePlayer(UUID playerId) {
    muted.add(playerId);
}

    public void unmutePlayer(UUID playerId) {
        muted.remove(playerId);
    }
}

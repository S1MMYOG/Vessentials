package me.Vanta.managers;

import me.Vanta.Vessentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final Vessentials plugin;
    private final Set<UUID> vanished = new HashSet<>();

    public VanishManager(Vessentials plugin) {
        this.plugin = plugin;
    }

    public void toggleVanish(Player p) {
        if (isVanished(p)) {
            showToAll(p);
            vanished.remove(p.getUniqueId());
            plugin.getDataManager().setVanished(p.getUniqueId(), false);
            p.sendMessage("You are now visible.");
        } else {
            hideFromAll(p);
            vanished.add(p.getUniqueId());
            plugin.getDataManager().setVanished(p.getUniqueId(), true);
            p.sendMessage("You are now vanished.");
        }
    }

    public boolean isVanished(Player p) {
        return vanished.contains(p.getUniqueId());
    }

    public void hideFromAll(Player p) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(p)) other.hidePlayer(plugin, p);
        }
    }

    public void showToAll(Player p) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(p)) other.showPlayer(plugin, p);
        }
    }

    // Silent vanish used on login if stored vanished
    public void silentVanish(Player p) {
        hideFromAll(p);
        vanished.add(p.getUniqueId());
    }
}

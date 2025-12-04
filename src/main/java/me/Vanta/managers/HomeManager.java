package me.Vanta.managers;

import me.Vanta.Vessentials;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HomeManager {

    private final Vessentials plugin;
    public HomeManager(Vessentials plugin) {
        this.plugin = plugin;
    }

    public void setHome(Player p, String name) {
        UUID id = p.getUniqueId();
        plugin.getDataManager().loadPlayer(id).set("homes." + name, p.getLocation());
        plugin.getDataManager().savePlayerData(id);
    }

    public void teleportHome(Player p, String name) {
        UUID id = p.getUniqueId();
        Object obj = plugin.getDataManager().loadPlayer(id).get("homes." + name);
        if (obj instanceof Location) {
            Location loc = (Location) obj;
            p.teleport(loc);
            p.sendMessage("Teleported to home " + name);
        } else {
            p.sendMessage("Home not found.");
        }
    }
}

package me.Vanta.listeners;

import me.Vanta.Vessentials;
import me.Vanta.managers.AfkManager;
import me.Vanta.managers.VanishManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final Vessentials plugin;
    private final AfkManager afk;
    private final VanishManager vanish;

    public PlayerListener(Vessentials plugin) {
        this.plugin = plugin;
        this.afk = plugin.getAfkManager();
        this.vanish = plugin.getVanishManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // If player is vanished in stored data, make them vanish
        if (plugin.getDataManager().isVanished(p.getUniqueId())) {
            vanish.silentVanish(p);
            e.setJoinMessage(null); // no join message for vanished players
            return;
        }

        // Normal join message
        e.setJoinMessage(ChatColor.DARK_PURPLE + "» " + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GRAY + " joined VantaMC");
        p.sendMessage(ChatColor.DARK_PURPLE + "Welcome to VantaMC!");
        // First join welcome:
        if (!plugin.getDataManager().hasPlayedBefore(p.getUniqueId())) {
            plugin.getDataManager().markPlayed(p.getUniqueId());
            p.sendMessage(ChatColor.GOLD + "Thanks for joining VantaMC for the first time!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (vanish.isVanished(p)) {
            e.setQuitMessage(null);
        } else {
            e.setQuitMessage(ChatColor.DARK_PURPLE + "« " + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GRAY + " left VantaMC");
        }
        afk.clear(p);
        plugin.getDataManager().savePlayerData(p.getUniqueId()); // persist on quit
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (plugin.getDataManager().isMuted(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You are muted.");
            e.setCancelled(true);
            return;
        }

        // Simple chat formatting: [Vanta] name: message
        String prefix = plugin.getDataManager().getPrefix(p.getUniqueId());
        if (prefix == null) prefix = "";
        e.setFormat(ChatColor.LIGHT_PURPLE + prefix + p.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        // Cancel AFK on movement (if they moved more than tiny delta)
        if (e.getFrom().distanceSquared(e.getTo()) > 0.0001) {
            afk.updateActivity(p);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        // cancel teleport if AFK? (example)
        // Or cancel pending teleport on move (if using delayed teleports)
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        // kept for expansion; only one chat handler runs realistically
    }
}

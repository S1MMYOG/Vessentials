package me.Vanta.managers;

import me.Vanta.Vessentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AfkManager {

    private final Vessentials plugin;
    private final Map<UUID, Long> lastActive = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> afkStatus = new ConcurrentHashMap<>();
    private BukkitTask checker;

    private final long AFK_THRESHOLD_MS;

    public AfkManager(Vessentials plugin) {
        this.plugin = plugin;
        this.AFK_THRESHOLD_MS = plugin.getConfig().getLong("afk.timeout-seconds", 300) * 1000L;
        startChecker();
    }

    public void updateActivity(Player p) {
        UUID id = p.getUniqueId();
        lastActive.put(id, System.currentTimeMillis());
        if (afkStatus.getOrDefault(id, false)) {
            afkStatus.put(id, false);
            Bukkit.broadcastMessage("» " + p.getName() + " is no longer AFK.");
        }
    }

    public void toggleAfk(Player p) {
        UUID id = p.getUniqueId();
        boolean nowAfk = !afkStatus.getOrDefault(id, false);
        afkStatus.put(id, nowAfk);
        if (nowAfk) {
            Bukkit.broadcastMessage("» " + p.getName() + " is now AFK.");
        } else {
            Bukkit.broadcastMessage("» " + p.getName() + " is no longer AFK.");
            updateActivity(p);
        }
    }

    public boolean isAfk(UUID id) {
        return afkStatus.getOrDefault(id, false);
    }

    public void clear(Player p) {
        UUID id = p.getUniqueId();
        lastActive.remove(id);
        afkStatus.remove(id);
    }

    private void startChecker() {
        checker = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            for (Player p : Bukkit.getOnlinePlayers()) {
                UUID id = p.getUniqueId();
                long last = lastActive.getOrDefault(id, System.currentTimeMillis());
                if (!afkStatus.getOrDefault(id, false) && (now - last) > AFK_THRESHOLD_MS) {
                    afkStatus.put(id, true);
                    Bukkit.broadcastMessage("» " + p.getName() + " is now AFK.");
                }
            }
        }, 20L * 10, 20L * 10);
    }

    public void stop() {
        if (checker != null) checker.cancel();
    }
}

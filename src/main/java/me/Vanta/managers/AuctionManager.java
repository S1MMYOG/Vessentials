package me.Vanta.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AuctionManager {

    private final JavaPlugin plugin;

    private boolean running = false;
    private ItemStack item;
    private Player seller;
    private Player highestBidder;
    private double highestBid = 0;
    private int timeLeft = 0;

    public AuctionManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean startAuction(Player p, ItemStack item, double startingBid, int seconds) {
        if (running) return false;

        this.running = true;
        this.seller = p;
        this.item = item.clone();
        this.highestBid = startingBid;
        this.timeLeft = seconds;

        Bukkit.broadcastMessage("§d§lAUCTION STARTED!");
        Bukkit.broadcastMessage("§fItem: §d" + item.getType());
        Bukkit.broadcastMessage("§fStarting Bid: §a$" + startingBid);
        Bukkit.broadcastMessage("§fUse §e/bid <amount> §fto bid!");

        startTimer();
        return true;
    }

    public boolean placeBid(Player p, double amount) {
        if (!running) return false;
        if (amount <= highestBid) return false;

        highestBid = amount;
        highestBidder = p;
        Bukkit.broadcastMessage("§dNew highest bid! §f" + p.getName() + " §d- §a$" + amount);
        return true;
    }

    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                if (timeLeft <= 0) {
                    finishAuction();
                    cancel();
                    return;
                }

                if (timeLeft % 10 == 0 || timeLeft <= 5) {
                    Bukkit.broadcastMessage("§dAuction ends in §e" + timeLeft + "§d seconds!");
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void finishAuction() {
        running = false;

        if (highestBidder == null) {
            Bukkit.broadcastMessage("§cAuction ended. No bids.");
            return;
        }

        highestBidder.getInventory().addItem(item);
        Bukkit.broadcastMessage("§aAuction sold to §f" + highestBidder.getName() + " §afor §a$" + highestBid);
    }

    public boolean isRunning() {
        return running;
    }

    public Player getSeller() {
    return seller;
}

}

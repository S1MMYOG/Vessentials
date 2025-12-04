package me.Vanta.commands;

import me.Vanta.managers.AuctionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BidCommand implements CommandExecutor {

    private final AuctionManager auction;

    public BidCommand(AuctionManager auction) {
        this.auction = auction;
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {

        if (!(s instanceof Player p)) return true;

        if (a.length != 1) {
            p.sendMessage(ChatColor.RED + "Usage: /bid <amount>");
            return true;
        }

        if (!auction.isRunning()) {
            p.sendMessage("§cNo auction running right now!");
            return true;
        }

        double amount = Double.parseDouble(a[0]);

        if (!auction.placeBid(p, amount)) {
            p.sendMessage("§cYour bid must be higher than the current bid!");
            return true;
        }

        return true;
    }
}


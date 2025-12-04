package me.Vanta.commands;

import me.Vanta.managers.AuctionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionCommand implements CommandExecutor {

    private final AuctionManager auction;

    public AuctionCommand(AuctionManager auction) {
        this.auction = auction;
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {

        if (!(s instanceof Player p)) return true;

        // /auction start <startingBid> <timeSeconds>
        if (a.length == 3 && a[0].equalsIgnoreCase("start")) {

            if (auction.isRunning()) {
                p.sendMessage("§cAn auction is already running.");
                return true;
            }

            double startBid = Double.parseDouble(a[1]);
            int time = Integer.parseInt(a[2]);

            ItemStack item = p.getInventory().getItemInMainHand();
if (item == null || item.getType() == Material.AIR) {
    p.sendMessage(ChatColor.RED + "You must hold an item.");
    return true;
}

            auction.startAuction(
                    p,
                    p.getInventory().getItemInMainHand(),
                    startBid,
                    time
            );

            return true;
        }

        p.sendMessage(ChatColor.RED + "Usage: /auction start <startingBid> <seconds>");
        return true;
    }
}


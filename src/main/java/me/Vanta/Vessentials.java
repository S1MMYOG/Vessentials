package me.Vanta;

import me.Vanta.commands.BidCommand;
import me.Vanta.commands.AuctionCommand;
import me.Vanta.commands.TeamCommand;
import me.Vanta.listeners.PlayerListener;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Vanta.managers.AfkManager;
import me.Vanta.managers.AuctionManager;
import me.Vanta.managers.DataManager;
import me.Vanta.managers.HomeManager;
import me.Vanta.managers.TeamManager;
import me.Vanta.managers.VanishManager;

public class Vessentials extends JavaPlugin {

    private static Vessentials instance;

    private DataManager dataManager;
    private AfkManager afkManager;
    private VanishManager vanishManager;
    private HomeManager homeManager;
    private TeamManager teamManager;
    private AuctionManager auctionManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Managers
        this.dataManager = new DataManager(this);
        this.afkManager = new AfkManager(this);
        this.vanishManager = new VanishManager(this);
        this.homeManager = new HomeManager(this);
        this.teamManager = new TeamManager(this);
        this.auctionManager = new AuctionManager(this);

        // Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);

        // Register commands (simple lambdas or dedicated classes recommended)
        getCommand("afk").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("Only players.");
                return true;
            }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
            afkManager.toggleAfk(p);
            return true;
        });

        getCommand("vanish").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("Only players.");
                return true;
            }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
            vanishManager.toggleVanish(p);
            return true;
        });

        getCommand("sethome").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("Only players.");
                return true;
            }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
            homeManager.setHome(p, "home");
            p.sendMessage("Home set.");
            return true;
        });

        getCommand("home").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("Only players.");
                return true;
            }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
            homeManager.teleportHome(p, "home");
            return true;
        });

        getCommand("spawn").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage("Only players.");
                return true;
            }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
            org.bukkit.Location spawn = getServer().getWorlds().get(0).getSpawnLocation();
            p.teleport(spawn);
            p.sendMessage("Teleported to spawn.");
            return true;
        });

        PluginCommand teamCmd = getCommand("team");
            if (teamCmd != null) {
               teamCmd.setExecutor(new TeamCommand(teamManager));
     }

        PluginCommand auctionCmd = getCommand("auction");
            if (auctionCmd != null) {
               auctionCmd.setExecutor(new AuctionCommand(auctionManager));
     }

        PluginCommand bidCmd = getCommand("bid");
           if (bidCmd != null) {
              bidCmd.setExecutor(new BidCommand(auctionManager));
     }



    

        getLogger().info("Vessentials enabled.");}
    
    

    @Override
    public void onDisable() {
        // Save necessary data
        dataManager.saveAll();
        getLogger().info("Vessentials disabled.");
    }

    public static Vessentials getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public AfkManager getAfkManager() {
        return afkManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
}

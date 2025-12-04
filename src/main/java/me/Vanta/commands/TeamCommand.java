package me.Vanta.commands;

import me.Vanta.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private final TeamManager teams;

    public TeamCommand(TeamManager teams) {
        this.teams = teams;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] a) {

        if (!(s instanceof Player p)) {
            s.sendMessage("Players only.");
            return true;
        }

        if (a.length == 0) {
            p.sendMessage("§d/team create <name>");
            p.sendMessage("§d/team join <name>");
            p.sendMessage("§d/team leave");
            p.sendMessage("§d/team chat <message>");
            p.sendMessage("§d/team members");
            return true;
        }

        switch (a[0].toLowerCase()) {

            case "create":
                if (a.length < 2) {
                    p.sendMessage("§cUsage: /team create <name>");
                    return true;
                }
                if (!teams.createTeam(p, a[1])) {
                    p.sendMessage("§cA team with that name already exists.");
                } else {
                    p.sendMessage("§aTeam created!");
                }
                return true;

            case "join":
                if (a.length < 2) {
                    p.sendMessage("§cUsage: /team join <name>");
                    return true;
                }
                if (!teams.joinTeam(p, a[1])) {
                    p.sendMessage("§cTeam not found.");
                } else {
                    p.sendMessage("§aYou joined the team!");
                }
                return true;

            case "leave":
                teams.leaveTeam(p);
                p.sendMessage("§eYou left your team.");
                return true;

            case "chat":
                if (a.length < 2) {
                    p.sendMessage("§cUsage: /team chat <message>");
                    return true;
                }
                String team = teams.getTeam(p);
                if (team == null) {
                    p.sendMessage("§cYou’re not on a team.");
                    return true;
                }
                String msg = String.join(" ", a).replace("chat ", "");
                teams.getTeamMembers(team).forEach(uuid -> {
                    Player member = Bukkit.getPlayer(uuid);
                    if (member != null) {
                        member.sendMessage("§d[Team] §f" + p.getName() + ": " + msg);
                    }
                });
                return true;

            case "members":
                String t = teams.getTeam(p);
                if (t == null) {
                    p.sendMessage("§cYou’re not on a team.");
                    return true;
                }
                p.sendMessage("§dMembers:");
                for (var uuid : teams.getTeamMembers(t)) {
                    Player m = Bukkit.getPlayer(uuid);
                    if (m != null) p.sendMessage("§f- " + m.getName());
                }
                return true;

            default:
                p.sendMessage("§c/team help");
                return true;
        }
    }
}

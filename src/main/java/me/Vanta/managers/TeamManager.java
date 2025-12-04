package me.Vanta.managers;

import org.bukkit.entity.Player;

import me.Vanta.Vessentials;

import java.util.*;

public class TeamManager {

    private final Map<String, Set<UUID>> teams = new HashMap<>();
    private final Map<UUID, String> playerTeams = new HashMap<>();

    public boolean createTeam(Player creator, String name) {
        name = name.toLowerCase();

        if (teams.containsKey(name)) return false;

        teams.put(name, new HashSet<>());
        teams.get(name).add(creator.getUniqueId());
        playerTeams.put(creator.getUniqueId(), name);
        return true;
    }

    public boolean joinTeam(Player player, String name) {
        name = name.toLowerCase();
        if (!teams.containsKey(name)) return false;

        leaveTeam(player);
        teams.get(name).add(player.getUniqueId());
        playerTeams.put(player.getUniqueId(), name);
        return true;
    }

    public void leaveTeam(Player player) {
        UUID uuid = player.getUniqueId();

        if (!playerTeams.containsKey(uuid)) return;

        String team = playerTeams.get(uuid);
        teams.get(team).remove(uuid);
        playerTeams.remove(uuid);
    }

    public String getTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    public Set<UUID> getTeamMembers(String name) {
        return teams.getOrDefault(name.toLowerCase(), new HashSet<>());
    }

    public boolean isOnTeam(Player player) {
        return playerTeams.containsKey(player.getUniqueId());
    }

    public TeamManager(Vessentials essentials) {
    // constructor body
}

}

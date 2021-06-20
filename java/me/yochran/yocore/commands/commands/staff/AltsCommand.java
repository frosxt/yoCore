package me.yochran.yocore.commands.staff;

import me.yochran.yocore.management.PlayerManagement;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsCommand implements CommandExecutor {

    private final yoCore plugin;
    private final PlayerManagement playerManagement = new PlayerManagement();

    public AltsCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("yocore.alts")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Alts.NoPermission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Alts.IncorrectUsage")));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!plugin.playerData.config.contains(target.getUniqueId().toString())) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Alts.InvalidPlayer")));
            return true;
        }

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Alts.GatheringMessage")
                .replace("%target%", playerManagement.getPlayerColor(target))));

        List<String> alts = new ArrayList<>();

        for (String alt : plugin.playerData.config.getKeys(false)) {
            if (plugin.playerData.config.getString(alt + ".IP").equalsIgnoreCase(plugin.playerData.config.getString(target.getUniqueId().toString() + ".IP")) && !target.getName().equalsIgnoreCase(plugin.playerData.config.getString(alt + ".Name"))) {
                String display = "&7" + Bukkit.getOfflinePlayer(UUID.fromString(alt)).getName();
                if (Bukkit.getOfflinePlayer(alt).isOnline()) display = "&a" + Bukkit.getOfflinePlayer(UUID.fromString(alt)).getName();
                if (plugin.muted_players.containsKey(UUID.fromString(alt))) display = "&6" + Bukkit.getOfflinePlayer(UUID.fromString(alt)).getName();
                if (plugin.banned_players.containsKey(UUID.fromString(alt))) display = "&c" + Bukkit.getOfflinePlayer(UUID.fromString(alt)).getName();
                if (plugin.blacklisted_ips.containsKey(plugin.playerData.config.getString(alt + ".IP"))) display = "&4" + Bukkit.getOfflinePlayer(UUID.fromString(alt)).getName();

                alts.add(display);
            }
        }

        String altMessage = "";
        for (String alt : alts) {
            if (altMessage.equalsIgnoreCase("")) altMessage = alt;
            else altMessage = altMessage + "&3, " + alt;
        }

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Alts.ExecutorMessage")
                .replace("%target%", playerManagement.getPlayerColor(target))
                .replace("%alts%", altMessage)));

        return true;
    }
}

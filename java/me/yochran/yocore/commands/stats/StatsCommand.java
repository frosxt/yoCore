package me.yochran.yocore.commands.stats;

import me.yochran.yocore.management.PlayerManagement;
import me.yochran.yocore.management.StatsManagement;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class StatsCommand implements CommandExecutor {

    private final yoCore plugin;
    private final StatsManagement statsManagement = new StatsManagement();
    private final PlayerManagement playerManagement = new PlayerManagement();

    public StatsCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.Command.MustBePlayer")));
            return true;
        }

        if (!statsManagement.statsAreEnabled(((Player) sender).getWorld().getName())) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.NotEnabledMessage")));
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.Command.IncorrectUsage")));
            return true;
        }

        if (args.length == 0) {
            Map<String, String> stats = statsManagement.getAllStats(((Player) sender).getWorld().getName(), (Player) sender);

            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.Command.Format")
                    .replace("%player%", playerManagement.getPlayerColor((Player) sender))
                    .replace("%kills%", stats.get("Kills"))
                    .replace("%deaths%", stats.get("Deaths"))
                    .replace("%kdr%", stats.get("KDR"))
                    .replace("%streak%", stats.get("Streak"))));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!statsManagement.isInitialized(((Player) sender).getWorld().getName(), target)) {
                sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.Command.InvalidPlayer")));
                return true;
            }

            Map<String, String> stats = statsManagement.getAllStats(((Player) sender).getWorld().getName(), target);

            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.Command.Format")
                    .replace("%player%", playerManagement.getPlayerColor(target))
                    .replace("%kills%", stats.get("Kills"))
                    .replace("%deaths%", stats.get("Deaths"))
                    .replace("%kdr%", stats.get("KDR"))
                    .replace("%streak%", stats.get("Streak"))));
        }

        return true;
    }
}
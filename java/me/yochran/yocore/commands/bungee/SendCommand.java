package me.yochran.yocore.commands.bungee;

import me.yochran.yocore.management.PlayerManagement;
import me.yochran.yocore.management.ServerManagement;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SendCommand implements CommandExecutor {

    private final yoCore plugin;
    private final PlayerManagement playerManagement = new PlayerManagement();
    private final ServerManagement serverManagement = new ServerManagement();

    public SendCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("yocore.send")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Send.NoPermission")));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Send.IncorrectUsage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Send.InvalidPlayer")));
            return true;
        }

        if (!serverManagement.getServers().contains(args[1].toUpperCase())) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Send.InvalidServer")));
            return true;
        }

        playerManagement.sendToSpawn(args[1].toUpperCase(), target);

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Send.ExecutorMessage")
                .replace("%target%", playerManagement.getPlayerColor(target))
                .replace("%server%", serverManagement.getName(args[1].toUpperCase()))));

        target.sendMessage(Utils.translate(plugin.getConfig().getString("Send.TargetMessage")
                .replace("%server%", serverManagement.getName(args[1].toUpperCase()))));

        return true;
    }
}

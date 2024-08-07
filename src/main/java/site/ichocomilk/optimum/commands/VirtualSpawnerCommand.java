package site.ichocomilk.optimum.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import site.ichocomilk.optimum.commands.subcommands.virtualspawner.AddSpawnerSubCommand;
import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.inventory.InventoryStorage;

public final class VirtualSpawnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players");
            return true;
        }

        final Player player = (Player)sender;

        if (!player.hasPermission("optimum.use")) {
            Messages.send(player, "no-permission");
            return true;
        }
        if (args.length != 1) {
            InventoryStorage.getStorage().getMain().open(player);
            return true;
        }
    
        switch (args[0].toLowerCase()) {
            case "add":
                AddSpawnerSubCommand.addSpawner(player);
                break;
            case "open":
                InventoryStorage.getStorage().getMain().open(player);    
                break;
            default:
                Messages.send(player, "virtual-format");
                break;
        }
        return true;
    }
}
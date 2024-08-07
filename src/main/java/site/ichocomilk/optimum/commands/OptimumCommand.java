package site.ichocomilk.optimum.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import site.ichocomilk.optimum.OptimumPlugin;
import site.ichocomilk.optimum.commands.subcommands.optimum.MsgSubCommand;
import site.ichocomilk.optimum.commands.subcommands.optimum.ReloadSubCommand;
import site.ichocomilk.optimum.config.langs.Messages;

public final class OptimumCommand implements CommandExecutor {

    private final OptimumPlugin plugin;

    public OptimumCommand(OptimumPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("optimum.admin")) {
            Messages.send(((Player)sender), "no-permission");
            return true;
        }
       
        if (args.length < 1) {
            sender.sendMessage(format());
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload":
                ReloadSubCommand.handle(plugin, sender, args);
                return true;
            case "msg":
                MsgSubCommand.handle(sender, args);
                return true;
            default:
                sender.sendMessage(format());
                break;
        }
        return true;
    }

    private String format() {
        return
            """
            \n
                §6§lOptimum §7- §e§lSPAWNERS
                
                §e/optimum §7->
                    §freload §e(all) §8- §7Reload plugin | (\"all\" includes database)
                    §fmsg §e(section) §8- §7Message with section \n
            """;
    }
}
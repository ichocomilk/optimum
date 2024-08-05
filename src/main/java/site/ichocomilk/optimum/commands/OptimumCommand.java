package site.ichocomilk.optimum.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import site.ichocomilk.optimum.OptimumPlugin;
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
                long time = System.currentTimeMillis();
                try {
                    plugin.load();
                    plugin.disable();
                } catch (Exception e) {
                    return true;
                }
                time = System.currentTimeMillis() - time;
                sender.sendMessage("Plugin reloaded in... " + time + "ms");
                return true;

            case "msg":
                if (args.length != 2) {
                    sender.sendMessage("§cFormat: /optimum msg §6(section) §7- Example: \"inventory.main.spawner.lore\"");
                    return true;
                }
                final String language = (sender instanceof Player) ? ((Player)sender).spigot().getLocale() : Messages.getDefaultLang();
                final Object object = Messages.get(language, args[1]);
                if (object == null) {
                    sender.sendMessage("§cThe section §e" + args[1] + "§c is null");
                    return true;
                }
                if (!(object instanceof List<?>)){
                    sender.sendMessage(object.toString());
                    return true;
                }
                final List<?> list = (List<?>)object;
                for (final Object obj2 : list) {
                    sender.sendMessage((obj2 == null) ? "" : obj2.toString());
                }
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
                    §freload §8- §7Reload plugin
                    §fmsg §e(section) §8- §7Message with section \n
            """;
    }
    
}

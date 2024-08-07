package site.ichocomilk.optimum.commands.subcommands.optimum;

import org.bukkit.command.CommandSender;

import site.ichocomilk.optimum.OptimumPlugin;

public class ReloadSubCommand {

    public static void handle(final OptimumPlugin plugin, final CommandSender sender, final String[] args) {
        long time = System.currentTimeMillis();
        OptimumPlugin.LOAD_OPTION option = OptimumPlugin.LOAD_OPTION.ONLY_CONFIG;
        if (args.length == 2 && args[1].equals("all")) {
            option = OptimumPlugin.LOAD_OPTION.ALL;
        }

        try {
            plugin.reload(option);
        } catch (Exception e) {
            sender.sendMessage("§cError reloading the plugin. Try fix the configuration and reload the plugin");
            return;
        }

        time = System.currentTimeMillis() - time;
        sender.sendMessage("§aPlugin reloaded in... " + time + "ms");
    }
}

package site.ichocomilk.optimum.commands.subcommands.optimum;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import site.ichocomilk.optimum.config.langs.Messages;

public class MsgSubCommand {

    public static void handle(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cFormat: /optimum msg §6(section) §7- Example: \"inventory.main.spawner.lore\"");
            return;
        }
        final String language = (sender instanceof Player) ? ((Player)sender).spigot().getLocale() : Messages.getDefaultLang();
        final Object object = Messages.get(language, args[1]);
        if (object == null) {
            sender.sendMessage("§cThe section §e" + args[1] + "§c is null");
            return;
        }
        if (!(object instanceof List<?>)){
            sender.sendMessage(object.toString());
            return;
        }
        final List<?> list = (List<?>)object;
        for (final Object obj2 : list) {
            sender.sendMessage((obj2 == null) ? "" : obj2.toString());
        }
    }
}
package site.ichocomilk.optimum.inventory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.milkbowl.vault.economy.Economy;
import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.inventory.creator.BuildeableInventoryCreator;
import site.ichocomilk.optimum.inventory.creator.CacheableInventoryCreator;
import site.ichocomilk.optimum.inventory.data.BuildeableInventory;
import site.ichocomilk.optimum.inventory.data.CacheableInventory;
import site.ichocomilk.optimum.inventory.item.DropClickHandler;
import site.ichocomilk.optimum.inventory.utils.IntegerUtils;
import site.ichocomilk.optimum.inventory.utils.InventoryUtils;

public final class StartInventory {

    private final ConfigManager manager ;
    private final Economy economy;

    public StartInventory(ConfigManager manager, Economy economy) {
        this.manager = manager;
        this.economy = economy;
    }

    public void start() {
        final Map<String, Integer>
            mainNecesary = new HashMap<>(1),
            spawnerNecesary = new HashMap<>(1),
            dropsNecesary = new HashMap<>(1);

        final CacheableInventory main = new CacheableInventoryCreator(manager).create(
            mainNecesary,
            "main",
            "spawner"
        );

        final BuildeableInventory spawner = new BuildeableInventoryCreator(manager).create(
            spawnerNecesary,
            "spawner",
            "back"
        );

        final BuildeableInventory drops = new BuildeableInventoryCreator(manager).create(
            dropsNecesary,
            "drops",
            "back"
        );

        new InventorySetup().setup(
            getDropClickHandlers(manager.getPlugin().getConfig()),
            main, mainNecesary,
            spawner, spawnerNecesary,
            drops, dropsNecesary
        );
    }

    private DropClickHandler[] getDropClickHandlers(final FileConfiguration config) {
        final Object section = config.get("spawner-click");
        if (section == null || !(section instanceof MemorySection)) {
            return new DropClickHandler[ClickType.values().length];
        }
        final MemorySection clickSection = (MemorySection)section;
        final ClickType[] clickTypes = ClickType.values();
        final DropClickHandler[] handlers = new DropClickHandler[clickTypes.length];
    
        final DropClickHandler sellHandler = (economy == null)
        ? (playerDrop, drop, event) -> {
            Messages.send((Player)event.getWhoClicked(), "no-economy-found");
            return false;
        }
        : (playerDrop, drop, event) -> {
            if (playerDrop.amount == 0) {
                return false;
            }
            if (drop.getSellPrice() <= 0) {
                Messages.send((Player)event.getWhoClicked(), "cant-sell");
                return false;
            }
            economy.depositPlayer((OfflinePlayer) event.getWhoClicked(), drop.getSellPrice() * playerDrop.amount);
            event.getWhoClicked().sendMessage(Messages.getString(((Player)event.getWhoClicked()).spigot().getLocale(), "sell")
                .replace("%amount%", String.valueOf(playerDrop.amount))
                .replace("%price%", String.valueOf(drop.getSellPrice() * playerDrop.amount)));
            playerDrop.amount = 0;
            return true;
        };

        final DropClickHandler giveHalf = (playerDrop, drop, event) -> {
            if (playerDrop.amount == 0) {
                return false;
            }
            if (playerDrop.amount == 1) {
                final int remain = InventoryUtils.addItem(event.getWhoClicked().getInventory(), drop.getMaterial(), 1);
                playerDrop.amount = (playerDrop.amount - 1) + remain;
                return true;
            }
            final int remain = InventoryUtils.addItem(event.getWhoClicked().getInventory(), drop.getMaterial(), playerDrop.amount / 2);
            playerDrop.amount = (playerDrop.amount / 2) + remain;
            return true;
        };
        final DropClickHandler giveAll = (playerDrop, drop, event) ->{
            if (playerDrop.amount == 0) {
                return false;
            }
            playerDrop.amount = InventoryUtils.addItem(event.getWhoClicked().getInventory(), drop.getMaterial(), playerDrop.amount);
            return true;
        };

        for (final ClickType type : clickTypes) {
            String action = clickSection.getString(type.name());
            if (action == null) {
                continue;
            }
            action = action.toUpperCase();

            switch (action) {
                case "SELL":
                    handlers[type.ordinal()] = sellHandler;
                    continue;
                case "GIVE_HALF":
                    handlers[type.ordinal()] = giveHalf;
                    continue;
                case "GIVE_ALL":
                    handlers[type.ordinal()] = giveAll;
                    continue;
                default:
                    if (!action.contains("GIVE")) {
                        manager.getPlugin().getLogger().warning("Can't found action in " + type.name() + " . Available actions: SELL, GIVE_HALF, GIVE_ALL and GIVE");
                        continue;
                    }
                    final String[] split = StringUtils.split(action, ',');
                    final int amount = (split.length >= 2) ? IntegerUtils.parsePositive(split[1], 1) : 1;
                    handlers[type.ordinal()] = (playerDrop, drop, event) -> {
                        if (playerDrop.amount == 0) {
                            return false;
                        }
                        if (playerDrop.amount <= amount) {
                            playerDrop.amount = InventoryUtils.addItem(event.getWhoClicked().getInventory(), drop.getMaterial(), playerDrop.amount);
                            return true;
                        }
                        final int remain = InventoryUtils.addItem(event.getWhoClicked().getInventory(), drop.getMaterial(),amount);
                        playerDrop.amount = playerDrop.amount - amount + remain;
                        return true;
                    };
                    continue;
            }
        }
        return handlers;
    }
}
package site.ichocomilk.optimum;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import site.ichocomilk.optimum.commands.OptimumCommand;
import site.ichocomilk.optimum.commands.VirtualSpawnerCommand;
import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.config.langs.StartLangs;
import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.nitrite.StartNitrite;
import site.ichocomilk.optimum.inventory.InventoryStorage;
import site.ichocomilk.optimum.inventory.StartInventory;
import site.ichocomilk.optimum.listeners.ListenerRegister;
import site.ichocomilk.optimum.listeners.types.InventoryClickListener;
import site.ichocomilk.optimum.listeners.types.InventoryDragListener;
import site.ichocomilk.optimum.listeners.types.PlayerJoinListener;
import site.ichocomilk.optimum.listeners.types.PlayerQuitListener;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.StartSpawners;
import site.ichocomilk.optimum.upgrades.StartUpgrades;
import site.ichocomilk.optimum.upgrades.UpgradeStorage;

public final class OptimumPlugin extends JavaPlugin {

    public static enum LOAD_OPTION {
        ALL, ONLY_CONFIG
    }

    private Database database;
    private Economy economy;

    @Override
    public void onEnable() {
        economy = setupEconomy();
        if (economy == null) {
            getLogger().warning("Sell drops are disable, because don't found Vault or any economy plugin");
        }

        getCommand("vs").setExecutor(new VirtualSpawnerCommand());
        getCommand("optimum").setExecutor(new OptimumCommand(this));

        try {   
            start(LOAD_OPTION.ALL);
        } catch (Exception e) {
            clearData();
            getLogger().warning("Error loading the plugin. Try fix the error and execute /optimum reload");
            return;
        }
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.saveAll();
            database.close();
        }
        HandlerList.unregisterAll(this);
        clearData();
    }

    public void clearData() {
        OptimumStorage.getStorage().clear();
        InventoryStorage.setStorage(null);
        Messages.setInstance(null);
        SpawnerStorage.setStorage(null);
        UpgradeStorage.setStorage(null);
    }

    public void reload(final LOAD_OPTION option) throws Exception {
        if (option == LOAD_OPTION.ALL) {
            onDisable();
        } else {
            clearData();
        }
        start(option);
    }

    private void start(final LOAD_OPTION option) throws Exception {
        final ConfigManager config = new ConfigManager(this);
        saveDefaultConfig();

        getLogger().info("");
        getLogger().info("  §6Optimum §7- §5SPAWNERS§r");
        printTime("  §bLangs", () -> new StartLangs().start(config));
        printTime("  §3Inventory", () -> new StartInventory(config, economy).start());
        printTime("  §9Spawners", () -> new StartSpawners(config).start());
        printTime("  §5Upgrades", () -> new StartUpgrades().start(config));

        if (option == LOAD_OPTION.ALL) {
            printTime("  §dDatabase", () -> database = getConfig().getString("database").equalsIgnoreCase("nitrite")
                ? new StartNitrite().create(new ConfigManager(this))
                : null);

            final ListenerRegister listeners = new ListenerRegister(this);
            listeners.register(new InventoryClickListener());
            listeners.register(new InventoryDragListener());
            listeners.register(new PlayerJoinListener(database));
            listeners.register(new PlayerQuitListener(database));
        }

        getLogger().info("");
    }

    private void printTime(final String startType, final Runnable action) throws Exception {
        try {
            long time = System.currentTimeMillis();
            action.run();
            time = System.currentTimeMillis() - time;
            getLogger().info((startType + " started in... " + time + "ms"));    
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error loading " + startType + ". Error: ", e);
            throw e;
        }
    }

    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }
}
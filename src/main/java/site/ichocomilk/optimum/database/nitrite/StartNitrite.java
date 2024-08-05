package site.ichocomilk.optimum.database.nitrite;

import org.bukkit.configuration.file.FileConfiguration;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.mvstore.MVStoreModule;

import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.database.Database;

public final class StartNitrite {
    
    public Database create(final ConfigManager manager) {
        final FileConfiguration config = manager.getPlugin().getConfig();
    
        final MVStoreModule storeModule = MVStoreModule.withConfig()
            .filePath(manager.getPlugin().getDataFolder().getAbsolutePath() + "/nitrite.db") 
            .compress(true)
            .build();
        
        final Nitrite db = Nitrite.builder()
            .loadModule(storeModule)
            .openOrCreate(config.getString("nitrite.user"), config.getString("nitrite.password"));

        return new NitriteDatabase(db);
    }
}

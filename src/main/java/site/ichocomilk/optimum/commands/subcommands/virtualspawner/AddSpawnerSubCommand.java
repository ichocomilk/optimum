package site.ichocomilk.optimum.commands.subcommands.virtualspawner;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.data.Spawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;

public final class AddSpawnerSubCommand {

    public static void addSpawner(final Player player) {
        player.sendMessage(player.spigot().getLocale());
        
        final ItemStack item = player.getItemInHand();
        if (item == null) {
            Messages.send(player, "no-spawner-in-hand");
            return;
        }

        final Material type = item.getType();
        final Spawner spawner = getSpawnerByMaterial(type.ordinal());
        if (spawner == null) {
            Messages.send(player, "no-spawner-in-hand");
            return;
        }

        final PlayerData data = OptimumStorage.getStorage().getPlayers().get(player.getUniqueId());
        if (data == null) {
            final List<PlayerSpawner> spawners = new ArrayList<>(3);
            final PlayerSpawner playerSpawner = new PlayerSpawner(spawner);
            spawners.add(playerSpawner);
            OptimumStorage.getStorage().getPlayers().put(player.getUniqueId(), new PlayerData(spawners));
            removeHandAndSendMsg(playerSpawner, player, item);
            return;
        }
        PlayerSpawner playerSpawner = getPlayerSpawner(data.getSpawners(), spawner.hashCode());
        if (playerSpawner != null) {
            removeHandAndSendMsg(playerSpawner, player, item);
            return;
        }
        final List<PlayerSpawner> spawners = new ArrayList<>(3);
        playerSpawner = new PlayerSpawner(spawner);
        spawners.add(playerSpawner);
        removeHandAndSendMsg(playerSpawner, player, item);
    }

    private static void removeHandAndSendMsg(final PlayerSpawner spawner, final Player player, final ItemStack item) {
        final String message = Messages.getString(player.spigot().getLocale(), "spawner.add")
            .replace("%amount%", String.valueOf(item.getAmount()));
    
        spawner.addSpawners(item.getAmount());
        player.sendMessage(message);

        player.setItemInHand(null);
    }

    private static Spawner getSpawnerByMaterial(final int material) {
        final Spawner[] spawners = SpawnerStorage.getStorage().getSpawners();

        if (spawners.length <= 40) {
            for (final Spawner spawner : spawners) {
                if (spawner.hashCode() == material) {
                    return spawner;
                }
            }
            return null;
        }

        int low = 0;
        int high = spawners.length - 1;
        while (low <= high){
            final int middleIndex = (low + high) / 2;
            final int middleIndexNumber = spawners[middleIndex].hashCode();
            if (material == middleIndexNumber){
                return spawners[middleIndex];
            }
            if (material < middleIndexNumber){
                high = middleIndex - 1;
            }
            if (material > middleIndexNumber){
                low = middleIndex + 1;
            }
        }
        return null;
    }

    private static PlayerSpawner getPlayerSpawner(final List<PlayerSpawner> spawners, final int material) {
        for (final PlayerSpawner spawner : spawners) {
            if (spawner != null && spawner.getSpawner().hashCode() == material) {
                return spawner;
            }
        }
        return null;
    }
}
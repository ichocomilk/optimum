package site.ichocomilk.optimum.spawners.updater;

import java.util.List;

import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;
import site.ichocomilk.optimum.upgrades.modifiers.AmountModifier;
import site.ichocomilk.optimum.upgrades.modifiers.TimeModifier;

public final class SpawnerUpdater {
    
    public static void saveDrops(final PlayerData data) {
        final List<PlayerSpawner> spawners = data.getSpawners();
        if (spawners == null) {
            return;
        }
        final long time = System.currentTimeMillis();
        for (final PlayerSpawner spawner : spawners) {
            int i = 0;
            for (final Drop drop : spawner.getSpawner().getDrops()) {
                calculateDrop(time, data, drop, spawner.getDrops()[i++], spawner.getSpawnersAmount());
            }
        }
    }

    public static void calculateDrop(
        final long time,
        final PlayerData data,
        final Drop drop,
        final PlayerSpawnerDrop playerDropInfo,
        final int amountSpawners
    ) {
        if (playerDropInfo.amount == drop.getMax()) {
            return;
        }
        long diferenceTime = (time - playerDropInfo.lastGeneratedTime) + playerDropInfo.compensation;
        final double seconds = diferenceTime / 1000D;
        if (data.getTimeUpgrades() != null) {
            for (final TimeModifier modifier : data.getTimeUpgrades()) {
                diferenceTime = modifier.handle(diferenceTime);
            }
        }

        if (seconds < drop.getSecondsToGenerate()) {
            return;
        }
        final double amountToGenerate = (seconds / drop.getSecondsToGenerate());
        final long compensation = (long)((amountToGenerate - (int)amountToGenerate) * 1000D);
        playerDropInfo.compensation = compensation;

        int itemsToAdd = ((int)amountToGenerate) * amountSpawners * drop.getAmount();
        if (data.getAmountModifiers() != null) {
            for (final AmountModifier modifier : data.getAmountModifiers()) {
                diferenceTime = modifier.handle(itemsToAdd);
            }
        }

        int newAmount = playerDropInfo.amount + itemsToAdd;
        if (newAmount >= drop.getMax()) {
            newAmount = drop.getMax();
        }
        playerDropInfo.amount = newAmount;
        playerDropInfo.lastGeneratedTime = time;
    }
}
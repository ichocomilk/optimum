package site.ichocomilk.optimum.spawners.updater;

import java.util.List;

import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;

public final class SpawnerUpdater {
    
    public static void saveDrops(final List<PlayerSpawner> spawners) {
        final long time = System.currentTimeMillis();
        for (final PlayerSpawner spawner : spawners) {
            int i = 0;
            for (final Drop drop : spawner.getSpawner().getDrops()) {
                calculateDrop(time, drop, spawner.getDrops()[i++], spawner.getSpawnersAmount());
            }
        }
    }

    public static void calculateDrop(final long time, final Drop drop, final PlayerSpawnerDrop playerDropInfo, final int amountSpawners) {
        if (playerDropInfo.amount == drop.getMax()) {
            return;
        }
        final long diferenceTime = (time - playerDropInfo.lastGeneratedTime) + playerDropInfo.compensation;
        final double seconds = diferenceTime / 1000D;

        if (seconds < drop.getSecondsToGenerate()) {
            return;
        }
        final double amountToGenerate = (seconds / drop.getSecondsToGenerate());
        final long compensation = (long)((amountToGenerate - (int)amountToGenerate) * 1000D);
        playerDropInfo.compensation = compensation;

        final int itemsToAdd = ((int)amountToGenerate) * amountSpawners * drop.getAmount();
        int newAmount = playerDropInfo.amount + itemsToAdd;
        if (newAmount >= drop.getMax()) {
            newAmount = drop.getMax();
        }
        playerDropInfo.amount = newAmount;
        playerDropInfo.lastGeneratedTime = time;
    }
}
package site.ichocomilk.optimum.spawners.data.player;

import site.ichocomilk.optimum.spawners.data.Spawner;

public final class PlayerSpawner {

    private final Spawner spawner;
    private final PlayerSpawnerDrop[] drops;

    private int spawnersAmount = 1;

    public PlayerSpawner(final Spawner spawner) {
        this.spawner = spawner;
        this.drops = createDrops(spawner.getDrops().length);
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public PlayerSpawnerDrop[] getDrops() {
        return drops;
    }

    public int getSpawnersAmount() {
        return spawnersAmount;
    }

    public void addSpawners(int amount) {
        this.spawnersAmount += amount;
    }

    private static PlayerSpawnerDrop[] createDrops(int length) {
        final PlayerSpawnerDrop[] drops = new PlayerSpawnerDrop[length];
        final long time = System.currentTimeMillis();

        for (int i = 0; i < length; i++) {
            final PlayerSpawnerDrop drop = new PlayerSpawnerDrop();
            drop.amount = 0;
            drop.lastGeneratedTime = time;
            drops[i] = drop;
        }

        return drops;
    }
}
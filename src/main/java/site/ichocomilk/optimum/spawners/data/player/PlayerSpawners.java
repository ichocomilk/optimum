package site.ichocomilk.optimum.spawners.data.player;

import java.util.List;

public final class PlayerSpawners {
    private final List<PlayerSpawner> spawners;
    private final int hash;

    public PlayerSpawners(int hash, List<PlayerSpawner> spawners){
        this.hash = hash;
        this.spawners = spawners;
    }

    public List<PlayerSpawner> getSpawners() {
        return spawners;
    }

    @Override
    public final int hashCode() {
        return hash;
    }
    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof PlayerSpawners) ? ((PlayerSpawners)obj).hash == this.hash : false;
    }
}

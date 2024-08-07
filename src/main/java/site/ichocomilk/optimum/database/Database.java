package site.ichocomilk.optimum.database;

public interface Database {

    PlayerData get(final String playerName);
    void save(final PlayerData data, final String playerName);
    void saveAll();
    void close();
}
package site.ichocomilk.optimum.upgrades;

import java.util.Map;

public interface Upgrade {
    
    Upgrade deserialize(final Map<String, Object> in);
    Map<String, Object> serialize();

    boolean levelUp(final int money);

}

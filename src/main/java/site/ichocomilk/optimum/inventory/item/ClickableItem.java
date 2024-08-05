package site.ichocomilk.optimum.inventory.item;

public final class ClickableItem {
    private final int slot;
    private final ClickHandler handler;

    public ClickableItem(int slot, ClickHandler handler) {
        this.slot = slot;
        this.handler = handler;
    }

    public ClickHandler getHandler() {
        return handler;
    }

    @Override
    public final int hashCode() {
        return slot;
    }
    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof ClickableItem) ? ((ClickableItem)obj).slot == this.slot : false; 
    }
}

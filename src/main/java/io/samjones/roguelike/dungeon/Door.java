package io.samjones.roguelike.dungeon;

public class Door implements Tile {
    private boolean locked;

    public Door(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void accept(TileVisitor tileVisitor) {
        tileVisitor.visit(this);
    }
}

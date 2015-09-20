package io.samjones.roguelike.dungeon.tiles;

public class Door implements Tile {
    private boolean locked;

    public Door(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }
}

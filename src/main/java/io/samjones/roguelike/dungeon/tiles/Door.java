package io.samjones.roguelike.dungeon.tiles;

/**
 * A door tile. Can be locked.
 */
public class Door implements Tile {
    private boolean locked;

    /**
     * Constructs a new door.
     *
     * @param locked true if the door is to be locked
     */
    public Door(boolean locked) {
        this.locked = locked;
    }

    /**
     * Returns true if the door is locked.
     *
     * @return true if the door is locked
     */
    public boolean isLocked() {
        return locked;
    }
}

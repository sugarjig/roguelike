package io.samjones.roguelike.dungeon.tiles;

/**
 * A wall tile.
 */
public class Wall implements Tile {
    private WallType wallType;

    /**
     * Constructs a new wall tile.
     *
     * @param wallType the type of wall (vertical, horizontal, or a corner)
     */
    public Wall(WallType wallType) {
        this.wallType = wallType;
    }

    /**
     * Gets the type of this wall tile.
     *
     * @return the type of wall
     */
    public WallType getWallType() {
        return wallType;
    }

}

package io.samjones.roguelike.dungeon.tiles;

public class Wall implements Tile {
    private WallType wallType;

    public Wall(WallType wallType) {
        this.wallType = wallType;
    }

    public WallType getWallType() {
        return wallType;
    }

}

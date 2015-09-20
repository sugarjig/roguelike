package io.samjones.roguelike.dungeon.tiles;

/**
 * Monster tile type. Obviously a real dungeon implementation would not make monsters a tile.
 */
public class Monster implements Tile {
    private boolean boss;

    public Monster(boolean boss) {
        this.boss = boss;
    }

    public boolean isBoss() {
        return boss;
    }
}

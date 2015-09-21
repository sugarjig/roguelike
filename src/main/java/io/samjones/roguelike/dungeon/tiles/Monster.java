package io.samjones.roguelike.dungeon.tiles;

/**
 * A monster tile.
 * <p>
 * Obviously a real dungeon implementation would not make monsters a tile.
 */
public class Monster implements Tile {
    private boolean boss;

    /**
     * Constructs a new monster tile.
     *
     * @param boss true if the monster is a boss
     */
    public Monster(boolean boss) {
        this.boss = boss;
    }

    /**
     * Returns true if the monster is a boss.
     *
     * @return true if the monster is a boss
     */
    public boolean isBoss() {
        return boss;
    }
}

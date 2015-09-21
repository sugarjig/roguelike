package io.samjones.roguelike.dungeon;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import io.samjones.roguelike.dungeon.tiles.Tile;

import java.util.Collections;

/**
 * A grid-based region. The coordinates in the region are 0-based rows and columns, starting from the top left.
 */
public class Region {
    // since the table keys are integers, use a RowSortedSet; this will (in theory) make retrieval operations faster
    private RowSortedTable<Integer, Integer, Tile> tiles;

    /**
     * Constructs a new region.
     */
    Region() {
        this.tiles = TreeBasedTable.create();
    }

    /**
     * Gets the width of the region.
     *
     * @return the width of the region
     */
    public int getWidth() {
        return this.tiles.columnKeySet().size() == 0 ? 0 : Collections.max(this.tiles.columnKeySet()) + 1;
    }

    /**
     * Gets the height of the region.
     *
     * @return the height of the region
     */
    public int getHeight() {
        return this.tiles.rowKeySet().size() == 0 ? 0 : this.tiles.rowKeySet().last() + 1;
    }

    /**
     * Gets a tile from the region.
     *
     * @param coordinates the coordinates of the tile
     * @return the tile; null if none exist at the coordinates
     */
    public Tile getTile(Coordinates coordinates) {
        return tiles.get(coordinates.getRow(), coordinates.getColumn());
    }

    /**
     * Adds a tile to the region. Will overwrite any existing tile at the specified coordinates.
     *
     * @param coordinates the coordinates to place the tile at
     * @param tile        the tile to place in the region
     */
    public void addTile(Coordinates coordinates, Tile tile) {
        if (coordinates.getRow() < 0) {
            throw new IllegalArgumentException("row must be non-negative");
        } else if (coordinates.getColumn() < 0) {
            throw new IllegalArgumentException("column must be non-negative");
        }
        tiles.put(coordinates.getRow(), coordinates.getColumn(), tile);
    }

    @Override
    public String toString() {
        return "Dungeon{" +
                "tiles=" + tiles +
                '}';
    }
}

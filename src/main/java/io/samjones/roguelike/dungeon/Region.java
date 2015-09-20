package io.samjones.roguelike.dungeon;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import java.util.Collections;

public class Region {
    // since the table keys are integers, use a RowSortedSet; this will (in theory) make retrieval operations faster
    protected RowSortedTable<Integer, Integer, Tile> tiles;

    public Region() {
        this.tiles = TreeBasedTable.create();
    }

    public int getWidth() {
        return this.tiles.columnKeySet().size() == 0 ? 0 : Collections.max(this.tiles.columnKeySet()) + 1;
    }

    public int getHeight() {
        return this.tiles.rowKeySet().size() == 0 ? 0 : this.tiles.rowKeySet().last() + 1;
    }

    public Tile getTile(int row, int col) {
        return tiles.get(row, col);
    }

    @Override
    public String toString() {
        return "Dungeon{" +
                "tiles=" + tiles +
                '}';
    }

    /**
     * Add a tile to the region. Will overwrite any existing tile at the specified coordinates.
     *
     * @param coordinate the coordinates to place the tile at
     * @param tile       the tile to place in the region
     */
    public void addTile(Coordinate coordinate, Tile tile) {
        if (coordinate.getRow() < 0) {
            throw new IllegalArgumentException("row must be non-negative");
        } else if (coordinate.getColumn() < 0) {
            throw new IllegalArgumentException("column must be non-negative");
        }
        tiles.put(coordinate.getRow(), coordinate.getColumn(), tile);
    }
}

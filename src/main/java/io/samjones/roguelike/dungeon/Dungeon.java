package io.samjones.roguelike.dungeon;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and the client uses them to reference tiles.
 */
public class Dungeon {
    private Table<Integer, Integer, Tile> tiles;
    private int numCols;
    private int numRows;

    public Dungeon(int numRows, int numCols) {
        this.numCols = numCols;
        this.numRows = numRows;
        // TODO - ArrayTable?
        tiles = HashBasedTable.create(numRows, numCols);
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public Tile getTile(int row, int col) {
        return tiles.get(row, col);
    }

    /**
     * Add a tile to the dungeon. Will overwrite any existing tile at the specified coordinates.
     *
     * @param row  the row to place the tile at
     * @param col  the column to place the tile at
     * @param tile the tile to place in the dungeon
     */
    public void addTile(int row, int col, Tile tile) {
        if (row > this.getNumRows() - 1) {
            throw new IllegalArgumentException("specified row is out of bounds");
        } else if (col > this.getNumCols()) {
            throw new IllegalArgumentException("specified column is out of bounds");
        }
        tiles.put(row, col, tile);
    }
}

package io.samjones.roguelike.dungeon;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and the client uses them to reference tiles.
 * <p>
 * The implementation does not require
 */
public class Dungeon {
    // since the table keys are integers, use a RowSortedSet; this will (in theory) make retrieval operations faster
    private RowSortedTable<Integer, Integer, Tile> tiles;
    private List<Room> rooms;

    public Dungeon() {
        this.tiles = TreeBasedTable.create();
        this.rooms = new ArrayList<>();
    }

    public int getNumCols() {
        return this.tiles.columnKeySet().size() == 0 ? 0 : Collections.max(this.tiles.columnKeySet()) + 1;
    }

    public int getNumRows() {
        return this.tiles.rowKeySet().last() + 1;
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
        tiles.put(row, col, tile);
    }

    public static class Room {
        private RowSortedTable<Integer, Integer, Tile> tiles;

        public Room() {
            this.tiles = TreeBasedTable.create();
        }

        public void addTile(int row, int col, Tile tile) {
            tiles.put(row, col, tile);
        }

        public int getNumCols() {
            return Collections.max(this.tiles.columnKeySet()) + 1;
        }

        public int getNumRows() {
            return this.tiles.rowKeySet().last() + 1;
        }

        public Tile getTile(int row, int col) {
            return tiles.get(row, col);
        }
    }
}

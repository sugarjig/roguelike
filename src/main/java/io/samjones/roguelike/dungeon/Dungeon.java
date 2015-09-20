package io.samjones.roguelike.dungeon;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO - factor out box/area/region class
/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and clients uses them to reference tiles.
 * <p>
 * The dungeon does not have a static size, so tiles can be placed using any row and column.
 */
public class Dungeon {
    // since the table keys are integers, use a RowSortedSet; this will (in theory) make retrieval operations faster
    private RowSortedTable<Integer, Integer, Tile> tiles;
    private List<Room> rooms;

    public Dungeon() {
        this.tiles = TreeBasedTable.create();
        this.rooms = new ArrayList<>();
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
                ", rooms=" + rooms +
                '}';
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

    public boolean addRoom(Room room, Coordinate offset) {
        if (canPlace(room, offset)) {
            for (int row = 0; row < room.getHeight(); row++) {
                for (int col = 0; col < room.getWidth(); col++) {
                    Tile tile = room.getTile(row, col);
                    this.addTile(row + offset.getRow(), col + offset.getColumn(), tile);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean canPlace(Room room, Coordinate offset) {
        for (int row = 0; row < room.getHeight(); row++) {
            for (int col = 0; col < room.getWidth(); col++) {
                if (this.getTile(row + offset.getRow(), col + offset.getColumn()) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class Room {
        private RowSortedTable<Integer, Integer, Tile> tiles;

        public Room() {
            this.tiles = TreeBasedTable.create();
        }

        public void addTile(int row, int col, Tile tile) {
            tiles.put(row, col, tile);
        }

        public int getWidth() {
            return this.tiles.columnKeySet().size() == 0 ? 0 : Collections.max(this.tiles.columnKeySet()) + 1;
        }

        public int getHeight() {
            return this.tiles.rowKeySet().last() + 1;
        }

        public Tile getTile(int row, int col) {
            return tiles.get(row, col);
        }

        @Override
        public String toString() {
            return "Room{" +
                    "tiles=" + tiles +
                    '}';
        }
    }
}

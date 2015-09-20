package io.samjones.roguelike.dungeon;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import java.util.Collections;

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

    public Dungeon() {
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
     * Add a tile to the dungeon. Will overwrite any existing tile at the specified coordinates.
     *
     * @param row  the row to place the tile at
     * @param col  the column to place the tile at
     * @param tile the tile to place in the dungeon
     */
    public void addTile(int row, int col, Tile tile) {
        if (row < 0) {
            throw new IllegalArgumentException("row must be non-negative");
        } else if (col < 0) {
            throw new IllegalArgumentException("column must be non-negative");
        }
        tiles.put(row, col, tile);
    }

    public boolean addRoom(Room room, Coordinate offset) {
        if (offset == null) {
            throw new IllegalArgumentException(("offset must not be null"));
        } else if (offset.getRow() < 0) {
            throw new IllegalArgumentException("row offset must be non-negative");
        } else if (offset.getColumn() < 0) {
            throw new IllegalArgumentException("column offset must be non-negative");
        }

        if (canAddRoom(room, offset)) {
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

    public boolean canAddRoom(Room room, Coordinate offset) {
        if (offset.getRow() < 0 || offset.getColumn() < 0) {
            return false;
        }

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

        public static Dungeon.Room createEmptyRoom(int height, int width) {
            Dungeon.Room room = new Dungeon.Room();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Tile tile;
                    if (row == 0 || row == height - 1) {
                        tile = new Wall();
                    } else if (col == 0 || col == width - 1) {
                        tile = new Wall();
                    } else {
                        tile = new Floor();
                    }
                    room.addTile(row, col, tile);
                }
            }
            return room;
        }

        public static Room createCorridor(int height, int width) {
            Dungeon.Room room = new Dungeon.Room();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    room.addTile(row, col, new Corridor());
                }
            }
            return room;
        }

        public void addTile(int row, int col, Tile tile) {
            if (row < 0) {
                throw new IllegalArgumentException("row must be non-negative");
            } else if (col < 0) {
                throw new IllegalArgumentException("column must be non-negative");
            }
            tiles.put(row, col, tile);
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
            return "Room{" +
                    "tiles=" + tiles +
                    '}';
        }
    }
}

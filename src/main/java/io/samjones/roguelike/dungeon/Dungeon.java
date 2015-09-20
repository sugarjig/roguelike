package io.samjones.roguelike.dungeon;

import io.samjones.roguelike.dungeon.tiles.Tile;

/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and clients uses them to reference tiles.
 * <p>
 * The dungeon does not have a static size, so tiles can be placed using any row and column.
 * <p>
 * One big limitation of this design is that once rooms are placed in the dungeon, they cannot be referenced again. The
 * dungeon does not keep track of the rooms that are placed in it, only the tiles.
 */
public class Dungeon extends Region {

    public Dungeon() {
        super();
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
                    Coordinate location = new Coordinate(row, col);
                    Tile tile = room.getTile(location);
                    Coordinate coordinate = location.add(offset);
                    this.addTile(coordinate, tile);
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
                Coordinate location = new Coordinate(row, col);
                if (this.getTile(location.add(offset)) != null) {
                    return false;
                }
            }
        }

        return true;
    }
}

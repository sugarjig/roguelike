package io.samjones.roguelike.dungeon;

/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and clients uses them to reference tiles.
 * <p>
 * The dungeon does not have a static size, so tiles can be placed using any row and column.
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
                    Tile tile = room.getTile(row, col);
                    Coordinate coordinate = new Coordinate(row + offset.getRow(), col + offset.getColumn());
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
                if (this.getTile(row + offset.getRow(), col + offset.getColumn()) != null) {
                    return false;
                }
            }
        }

        return true;
    }
}

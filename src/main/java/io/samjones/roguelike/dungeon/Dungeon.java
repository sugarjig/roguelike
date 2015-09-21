package io.samjones.roguelike.dungeon;

import io.samjones.roguelike.dungeon.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple grid-based dungeon. Each tile in the grid represents features of the dungeon, such as floors, wall, doors,
 * etc. The rows and columns are 0-based, and clients uses them to reference tiles.
 * <p>
 * The dungeon does not have a static size, so tiles can be placed using any non-negative row and column.
 * <p>
 * The dungeon contains a list of rooms and corridors that have been added, but any later updates to them will not be
 * reflected in the dungeon.
 */
public class Dungeon extends Region {
    private List<Room> rooms;
    private List<Room> corridors;

    public Dungeon() {
        super();
        rooms = new ArrayList<>();
        corridors = new ArrayList<>();
    }

    /**
     * Adds a room to the dungeon.
     * <p>
     * The room's tiles will be copies to the dungeon's tiles, and the room will be added to the dungeon's room list or
     * corridor list.
     *
     * @param room   the room to add
     * @param offset the grid offset for the room
     * @return true if there was space for the room and it was successfully added
     */
    public boolean addRoom(Room room, Coordinates offset) {
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
                    Coordinates location = new Coordinates(row, col);
                    Tile tile = room.getTile(location);
                    Coordinates coordinates = location.add(offset);
                    this.addTile(coordinates, tile);
                }
            }
            room.setOffset(offset);
            if (room.getRoomType() == RoomType.CORRIDOR) {
                this.corridors.add(room);
            } else {
                rooms.add(room);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the dungeon has space for a room.
     *
     * @param room   the room in consideration
     * @param offset the offset for the room
     * @return true if the dungeon has space for the room at the specified offset
     */
    public boolean canAddRoom(Room room, Coordinates offset) {
        if (offset.getRow() < 0 || offset.getColumn() < 0) {
            return false;
        }

        for (int row = 0; row < room.getHeight(); row++) {
            for (int col = 0; col < room.getWidth(); col++) {
                Coordinates location = new Coordinates(row, col);
                if (this.getTile(location.add(offset)) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets the list of rooms in this dungeon.
     *
     * @return the list of rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Gets the list of corridors in this dungeon.
     *
     * @return the list of corridors
     */
    public List<Room> getCorridors() {
        return corridors;
    }
}

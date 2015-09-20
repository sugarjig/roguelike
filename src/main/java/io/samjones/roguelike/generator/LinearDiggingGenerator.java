package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Coordinate;
import io.samjones.roguelike.dungeon.Room;
import io.samjones.roguelike.dungeon.tiles.Door;
import io.samjones.roguelike.dungeon.tiles.Tile;
import io.samjones.roguelike.dungeon.tiles.Wall;

import java.util.Random;

/**
 * A simple implementation of a digging generator. This generator starts with a room, then for each subsequent room it
 * randomly tries to dig a corridor and room, stopping when it can successfully do so.
 */
public class LinearDiggingGenerator extends DiggingGenerator {
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MAX_ROOM_HEIGHT = 15;
    public static final int MAX_ROOM_WIDTH = 15;
    public static final int MIN_CORRIDOR_LENGTH = 1;
    public static final int MAX_CORRIDOR_LENGTH = 10;
    public static final int MAX_DOOR_TRIES = 2 * (MAX_ROOM_HEIGHT + MAX_ROOM_WIDTH);
    public static final int MAX_ROOM_TRIES = 10;
    private Random random = new Random();
    private Coordinate previousOffset = new Coordinate(0, 0);

    private Door generateDoor() {
        return new Door(false);
    }

    private Coordinate chooseDoorLocation(Room room) throws Exception {
        for (int i = 0; i < MAX_DOOR_TRIES; i++) {
            int row = random.nextInt(room.getHeight());
            int col = random.nextInt(room.getWidth());
            if (isNotACorner(room, row, col)) {
                Tile tile = room.getTile(row, col);
                if (tile instanceof Wall) {
                    return new Coordinate(row, col);
                }
            }
        }
        throw new Exception("too many tries to choose a door location");
    }

    private boolean isNotACorner(Room room, int row, int col) {
        return (row == 0 || row == room.getHeight() - 1) && col > 0 && col < room.getWidth() - 1
                || (col == 0 || col == room.getWidth() - 1) && row > 0 && row < room.getHeight() - 1;
    }

    @Override
    protected Room digRoom(Room previousRoom) throws Exception {
        // TODO -refactor this beast
        Room room = generateRoom();
        if (previousRoom == null) {
            dungeon.addRoom(room, new Coordinate(0, 0));
            return room;
        } else {
            for (int i = 0; i < MAX_ROOM_TRIES; i++) {
                Coordinate doorLocation = chooseDoorLocation(previousRoom);

                int corridorLength = random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                Coordinate corridorOffset = calculateCorridorOffset(previousRoom, doorLocation, this.previousOffset, corridorLength);

                // if corridor can be placed, attempt to place a room
                Room corridor = generateCorridor(previousRoom, doorLocation, corridorLength);
                if (this.dungeon.canAddRoom(corridor, corridorOffset)) {
                    CorridorDirection corridorDirection = determineCorridorDirection(previousRoom, doorLocation);
                    Coordinate lastCorridorTile = calculateLastCorridorTile(corridor, corridorDirection, corridorOffset);
                    Coordinate roomOffset = calculateRoomOffset(room, corridorDirection, lastCorridorTile);

                    if (dungeon.canAddRoom(room, roomOffset)) {
                        this.dungeon.addTile(doorLocation.add(this.previousOffset), generateDoor());
                        this.dungeon.addRoom(corridor, corridorOffset);
                        this.dungeon.addRoom(room, roomOffset);

                        Coordinate otherDoorCoords = calculateOtherDoorCoords(corridorDirection, lastCorridorTile);
                        this.dungeon.addTile(otherDoorCoords, generateDoor());

                        this.previousOffset = roomOffset;

                        return room;
                    }
                }
            }
            throw new Exception("too many tries to find a room location");
        }
    }

    /**
     * Calculates the offset of the room. The coordinate of the side adjacent to the corridor is randomly chosen.
     *
     * @param room              the room
     * @param corridorDirection the direction of the corridor going into the room
     * @param lastCorridorTile  the coordinates of the last tile in the corridor
     * @return the coordinates of the offset for the room
     */
    private Coordinate calculateRoomOffset(Room room, CorridorDirection corridorDirection, Coordinate lastCorridorTile) {
        if (corridorDirection == CorridorDirection.UP) {
            int row = lastCorridorTile.getRow() - room.getHeight();
            int col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
            return new Coordinate(row, col);
        } else if (corridorDirection == CorridorDirection.DOWN) {
            int row = lastCorridorTile.getRow() + 1;
            int col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
            return new Coordinate(row, col);
        } else if (corridorDirection == CorridorDirection.LEFT) {
            int row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
            int col = lastCorridorTile.getColumn() - room.getWidth();
            return new Coordinate(row, col);
        } else { // CorridorDirection.RIGHT
            int row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
            int col = lastCorridorTile.getColumn() + 1;
            return new Coordinate(row, col);
        }
    }

    /**
     * Calculates the coordinates of the door on the other side of a corridor.
     *
     * @param corridorDirection the direction the corridor is going
     * @param lastCorridorTile  the coordinates of the last tile in the corridor
     * @return the coordinates of the door
     */
    private Coordinate calculateOtherDoorCoords(CorridorDirection corridorDirection, Coordinate lastCorridorTile) {
        if (corridorDirection == CorridorDirection.UP) {
            return new Coordinate(lastCorridorTile.getRow() - 1, lastCorridorTile.getColumn());
        } else if (corridorDirection == CorridorDirection.DOWN) {
            return new Coordinate(lastCorridorTile.getRow() + 1, lastCorridorTile.getColumn());
        } else if (corridorDirection == CorridorDirection.LEFT) {
            return new Coordinate(lastCorridorTile.getRow(), lastCorridorTile.getColumn() - 1);
        } else { // CorridorDirection.RIGHT
            return new Coordinate(lastCorridorTile.getRow(), lastCorridorTile.getColumn() + 1);
        }
    }

    /**
     * Calculates the coordinates of the last tile of a corridor.
     *
     * @param corridor  the corridor
     * @param direction the direction the corridor is going
     * @param offset    the offset of the corridor
     * @return the coordinates of last tile of the corridor
     */
    private Coordinate calculateLastCorridorTile(Room corridor, CorridorDirection direction, Coordinate offset) {
        if (direction == CorridorDirection.UP || direction == CorridorDirection.LEFT) {
            return new Coordinate(offset.getRow(), offset.getColumn());
        } else if (direction == CorridorDirection.DOWN) {
            return new Coordinate(offset.getRow() + corridor.getHeight() - 1, offset.getColumn());
        } else { // CorridorDirection.RIGHT
            return new Coordinate(offset.getRow(), offset.getColumn() + corridor.getWidth() - 1);
        }
    }

    /**
     * Calculates the offset for a corridor.
     *
     * @param room           the room the corridor is coming from
     * @param doorLocation   the door where the corridor starts, relative to the room
     * @param roomOffset     the offset for the room
     * @param corridorLength the length of the corridor
     * @return the coordinates of the offset for the corridor
     */
    private Coordinate calculateCorridorOffset(Room room, Coordinate doorLocation, Coordinate roomOffset, int corridorLength) {
        Coordinate doorLocationInDungeon = doorLocation.add(roomOffset);
        if (doorLocation.getRow() == 0) { // top wall
            return new Coordinate(doorLocationInDungeon.getRow() - corridorLength, doorLocationInDungeon.getColumn());
        } else if (doorLocation.getRow() == room.getHeight() - 1) { // bottom wall
            return new Coordinate(doorLocationInDungeon.getRow() + 1, doorLocationInDungeon.getColumn());
        } else if (doorLocation.getColumn() == 0) { // left wall
            return new Coordinate(doorLocationInDungeon.getRow(), doorLocationInDungeon.getColumn() - corridorLength);
        } else { // right wall
            return new Coordinate(doorLocationInDungeon.getRow(), doorLocationInDungeon.getColumn() + 1);
        }
    }

    /**
     * Generates a corridor based on the location of a door in a room.
     *
     * @param room         the room where the corridor is coming from
     * @param doorLocation the location of the door where the corridor starts
     * @param length       the length of the corridor
     * @return the Room that contains the corridor tiles
     */
    private Room generateCorridor(Room room, Coordinate doorLocation, int length) {
        if (doorLocation.getRow() == 0 || doorLocation.getRow() == room.getHeight() - 1) { // top or bottom wall
            return Room.createCorridor(length, 1);
        } else { // left or right wall
            return Room.createCorridor(1, length);
        }
    }

    /**
     * Determines direction of corridor; assumes no doors on corner walls.
     *
     * @param room         the room the corridor is coming from
     * @param doorLocation the location of the door at the start of the corridor, relative to the room
     * @return the direction of the corridor
     */
    private CorridorDirection determineCorridorDirection(Room room, Coordinate doorLocation) {
        if (doorLocation.getRow() == 0) { // top wall
            return CorridorDirection.UP;
        } else if (doorLocation.getRow() == room.getHeight() - 1) { // bottom wall
            return CorridorDirection.DOWN;
        } else if (doorLocation.getColumn() == 0) { // left wall
            return CorridorDirection.LEFT;
        } else { // right wall
            return CorridorDirection.RIGHT;
        }
    }

    private Room generateRoom() {
        int height = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int width = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        return Room.createEmptyRoom(height, width);
    }

    /**
     * Enum for the direction a corridor is going in.
     */
    public enum CorridorDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}

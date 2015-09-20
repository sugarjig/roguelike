package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class LinearDiggingGenerator extends DiggingGenerator {
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MAX_ROOM_HEIGHT = 15;
    public static final int MAX_ROOM_WIDTH = 15;
    public static final int MIN_CORRIDOR_LENGTH = 1;
    public static final int MAX_CORRIDOR_LENGTH = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiggingGenerator.class);
    private Random random = new Random();
    private Coordinate previousOffset = new Coordinate(0, 0);

    protected Door generateDoor() {
        return new Door(false);
    }

    private Coordinate chooseDoorLocation(Room room) {
        Coordinate wallCoordinate = null;
        while (wallCoordinate == null) { // TODO - deal with infinite loops
            int row = random.nextInt(room.getHeight());
            int col = random.nextInt(room.getWidth());
            if (isNotACorner(room, row, col)) {
                Tile tile = room.getTile(row, col);
                if (tile instanceof Wall) {
                    wallCoordinate = new Coordinate(row, col);
                }
            }
        }
        return wallCoordinate;
    }

    private boolean isNotACorner(Room room, int row, int col) {
        return (row == 0 || row == room.getHeight() - 1) && col > 0 && col < room.getWidth() - 1
                || (col == 0 || col == room.getWidth() - 1) && row > 0 && row < room.getHeight() - 1;
    }

    @Override
    protected Room addRoom(Room previousRoom) {
        // TODO -refactor this beast
        Room room = generateRoom();
        if (previousRoom == null) {
            dungeon.addRoom(room, new Coordinate(0, 0));
        } else {
            boolean roomAdded = false;
            while (!roomAdded) { // TODO - prevent infinite loops
                Coordinate doorLocation = chooseDoorLocation(previousRoom);

                int corridorLength = random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;

                // determine direction of corridor; assumes no doors on corner walls
                Room corridor = new Room();
                Coordinate doorLocationInDungeon = new Coordinate(
                        doorLocation.getRow() + this.previousOffset.getRow(),
                        doorLocation.getColumn() + this.previousOffset.getColumn()
                );
                Coordinate corridorOffset = new Coordinate(0, 0);
                CorridorDirection corridorDirection = CorridorDirection.DOWN;
                if (doorLocation.getRow() == 0) { // top wall
                    corridor = Room.createCorridor(corridorLength, 1);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow() - corridorLength,
                            doorLocationInDungeon.getColumn()
                    );
                    corridorDirection = CorridorDirection.UP;
                } else if (doorLocation.getRow() == previousRoom.getHeight() - 1) { // bottom wall
                    corridor = Room.createCorridor(corridorLength, 1);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow() + 1,
                            doorLocationInDungeon.getColumn()
                    );
                    corridorDirection = CorridorDirection.DOWN;
                } else if (doorLocation.getColumn() == 0) { // left wall
                    corridor = Room.createCorridor(1, corridorLength);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow(),
                            doorLocationInDungeon.getColumn() - corridorLength
                    );
                    corridorDirection = CorridorDirection.LEFT;
                } else if (doorLocation.getColumn() == previousRoom.getWidth() - 1) { // right wall
                    corridor = Room.createCorridor(1, corridorLength);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow(),
                            doorLocationInDungeon.getColumn() + 1
                    );
                    corridorDirection = CorridorDirection.RIGHT;
                }

                // if corridor can be placed, attempt to place a room
                if (this.dungeon.canAddRoom(corridor, corridorOffset)) {
                    // check if room can be placed next to last corridor tile
                    Coordinate lastCorridorTile; // absolute location in dungeon
                    int row;
                    int col;
                    Coordinate otherDoorCoords;
                    if (corridorDirection == CorridorDirection.UP) {
                        lastCorridorTile = new Coordinate(corridorOffset.getRow(), corridorOffset.getColumn());
                        // put the new room just above the corridor
                        row = lastCorridorTile.getRow() - room.getHeight();
                        col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
                        otherDoorCoords = new Coordinate(lastCorridorTile.getRow() - 1, lastCorridorTile.getColumn());
                    } else if (corridorDirection == CorridorDirection.DOWN) {
                        lastCorridorTile = new Coordinate(corridorOffset.getRow() + corridor.getHeight() - 1, corridorOffset.getColumn());
                        // put the new room just below the corridor
                        row = lastCorridorTile.getRow() + 1;
                        col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
                        otherDoorCoords = new Coordinate(lastCorridorTile.getRow() + 1, lastCorridorTile.getColumn());
                    } else if (corridorDirection == CorridorDirection.LEFT) {
                        lastCorridorTile = new Coordinate(corridorOffset.getRow(), corridorOffset.getColumn());
                        // put the new room just to the left of the corridor
                        row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
                        col = lastCorridorTile.getColumn() - room.getWidth();
                        otherDoorCoords = new Coordinate(lastCorridorTile.getRow(), lastCorridorTile.getColumn() - 1);
                    } else { // CorridorDirection.RIGHT
                        lastCorridorTile = new Coordinate(corridorOffset.getRow(), corridorOffset.getColumn() + corridor.getWidth() - 1);
                        // put the new room just tot he right of the corridor
                        row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
                        col = lastCorridorTile.getColumn() + 1;
                        otherDoorCoords = new Coordinate(lastCorridorTile.getRow(), lastCorridorTile.getColumn() + 1);
                    }

                    Coordinate roomOffset = new Coordinate(row, col);

                    if (dungeon.canAddRoom(room, roomOffset)) {
                        this.dungeon.addTile(doorLocationInDungeon, generateDoor());
                        this.dungeon.addRoom(corridor, corridorOffset);
                        this.dungeon.addRoom(room, roomOffset);
                        this.dungeon.addTile(otherDoorCoords, generateDoor());
                        this.previousOffset = roomOffset;
                        roomAdded = true;
                    }
                }
            }
        }
        return room;
    }

    private Room generateRoom() {
        int height = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int width = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        return Room.createEmptyRoom(height, width);
    }

    public enum CorridorDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}

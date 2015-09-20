package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SimpleRoomByRoomGenerator extends RoomByRoomGenerator {
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MAX_ROOM_HEIGHT = 15;
    public static final int MAX_ROOM_WIDTH = 15;
    public static final int MAX_DUNGEON_HEIGHT = 25;
    public static final int MAX_DUNGEON_WIDTH = 80;
    public static final int MIN_CORRIDOR_LENGTH = 5;
    public static final int MAX_CORRIDOR_LENGTH = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomByRoomGenerator.class);
    private Random random = new Random();
    private Dungeon.Room previousRoom;
    private Coordinate previousOffset = new Coordinate(0, 0);

    protected Door generateDoor() {
        return new Door(false);
    }

    private Coordinate chooseDoorLocation(Dungeon.Room room) {
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

    private boolean isNotACorner(Dungeon.Room room, int row, int col) {
        return (row == 0 || row == room.getHeight() - 1) && col > 0 && col < room.getWidth() - 1
                || (col == 0 || col == room.getWidth() - 1) && row > 0 && row < room.getHeight() - 1;
    }

    /**
     * Add a door to a room in the dungeon.
     *
     * @param room the room to add a door to
     * @return the coordinate of the door, relative to the room
     */
    private Coordinate addDoor(Dungeon.Room room) {
        Coordinate wallLocation = chooseDoorLocation(room);
        Door door = generateDoor();
        int row = wallLocation.getRow() + this.previousOffset.getRow();
        int col = wallLocation.getColumn() + this.previousOffset.getColumn();
        Coordinate doorLocation = new Coordinate(row, col);
        LOGGER.debug("door location: " + doorLocation);
        this.dungeon.addTile(row, col, door);
        return wallLocation; // location relative to room
    }

    @Override
    protected Dungeon.Room addRoom(Dungeon.Room previousRoom) {
        Dungeon.Room room = generateRoom();
        if (previousRoom == null) {
            dungeon.addRoom(room, new Coordinate(0, 0));
        } else {
            boolean roomAdded = false;
            while (!roomAdded) {
                Coordinate doorLocation = chooseDoorLocation(previousRoom);

                int corridorLength = random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;

                // determine direction of corridor; assumes no doors on corner walls
                Dungeon.Room corridor = new Dungeon.Room();
                Coordinate doorLocationInDungeon = new Coordinate(
                        doorLocation.getRow() + this.previousOffset.getRow(),
                        doorLocation.getColumn() + this.previousOffset.getColumn()
                );
                Coordinate corridorOffset = new Coordinate(0, 0);
                if (doorLocation.getRow() == 0) { // top wall
                    corridor = Dungeon.Room.createCorridor(corridorLength, 1);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow() - corridorLength,
                            doorLocationInDungeon.getColumn()
                    );
                } else if (doorLocation.getRow() == previousRoom.getHeight() - 1) { // bottom wall
                    corridor = Dungeon.Room.createCorridor(corridorLength, 1);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow() + 1,
                            doorLocationInDungeon.getColumn()
                    );
                } else if (doorLocation.getColumn() == 0) { // left wall
                    corridor = Dungeon.Room.createCorridor(1, corridorLength);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow(),
                            doorLocationInDungeon.getColumn() - corridorLength
                    );
                } else if (doorLocation.getColumn() == previousRoom.getWidth() - 1) { // right wall
                    corridor = Dungeon.Room.createCorridor(1, corridorLength);
                    corridorOffset = new Coordinate(
                            doorLocationInDungeon.getRow(),
                            doorLocationInDungeon.getColumn() + 1
                    );
                }

                // if corridor can be placed, attempt to place a room
                if (this.dungeon.canAddRoom(corridor, corridorOffset)) {
                    // check if room can be placed next to last corridor tile
                    Coordinate lastCorridorTile = new Coordinate(
                            corridorOffset.getRow() + corridor.getHeight() - 1,
                            corridorOffset.getColumn() + corridor.getWidth() - 1
                    );

                    LOGGER.debug("adding corridor at offset: " + corridorOffset);
                    this.dungeon.addTile(doorLocationInDungeon.getRow(), doorLocationInDungeon.getColumn(), generateDoor());
                    this.dungeon.addRoom(corridor, corridorOffset);

                    roomAdded = true;
                }
            }
        }
        return room;
    }

    private Dungeon.Room generateRoom() {
        int height = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int width = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        return Dungeon.Room.createEmptyRoom(height, width);
    }

    private Coordinate placeRoom(Dungeon.Room room, Dungeon.Room previousRoom) {
        Coordinate offset;
        if (previousRoom == null) { // first room in the dungeon
            offset = new Coordinate(0, 0);
        } else {
            // TODO - use different method of finding offset
            int rowOffset = random.nextInt(MAX_DUNGEON_HEIGHT);
            int colOffset = random.nextInt(MAX_DUNGEON_WIDTH);
            offset = new Coordinate(rowOffset, colOffset);
        }

        boolean roomAdded = false;
        while (!roomAdded) { // TODO - deal with infinite loops
            roomAdded = dungeon.addRoom(room, offset);
        }

        // set these so we can use them in later operations
        this.previousRoom = room;
        this.previousOffset = offset;
        LOGGER.debug("new offset: " + offset);

        return offset;
    }
}

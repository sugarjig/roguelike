package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.CardinalDirection;
import io.samjones.roguelike.dungeon.Coordinate;
import io.samjones.roguelike.dungeon.Room;
import io.samjones.roguelike.dungeon.RoomType;
import io.samjones.roguelike.dungeon.tiles.*;

import java.util.List;
import java.util.Random;

/**
 * A simple implementation of a digging generator. This generator starts with a room, then for each subsequent room it
 * randomly chooses an existing room and tries to dig a new corridor and new room, stopping when it can successfully do
 * so.
 */
public class RandomRoomDiggingGenerator extends DiggingGenerator {
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MAX_ROOM_HEIGHT = 15;
    public static final int MAX_ROOM_WIDTH = 15;
    public static final int MIN_CORRIDOR_LENGTH = 1;
    public static final int MAX_CORRIDOR_LENGTH = 10;
    public static final int MAX_DOOR_TRIES = 2 * (MAX_ROOM_HEIGHT + MAX_ROOM_WIDTH);
    public static final int MAX_ROOM_TRIES = 25;
    public static final int MAX_ROOM_TILE_TRIES = (MAX_ROOM_HEIGHT - 2) * (MAX_ROOM_WIDTH - 2);
    public static final double CHANCE_OF_CHEST = 0.5;
    public static final double CHANCE_OF_MONSTER = 0.75;
    public static final double CHANCE_OF_TRAP = 0.25;
    private Random random = new Random();

    /**
     * Calculates the offset for a corridor. Depends on the corridor being either one tile wide or one tile tall.
     *
     * @param room           the room the corridor is coming from
     * @param doorLocation   the door where the corridor starts, relative to the room
     * @param corridorLength the length of the corridor
     * @return the offset for the corridor
     */
    private static Coordinate calculateCorridorOffset(Room room, Coordinate doorLocation, int corridorLength) {
        Coordinate doorLocationInDungeon = doorLocation.add(room.getOffset());
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
     * Calculates the coordinates of the last tile of a corridor. Depends on the corridor being either one tile wide or
     * one tile tall.
     *
     * @param corridor  the corridor
     * @param direction the direction the corridor is going
     * @param offset    the offset of the corridor
     * @return the coordinates of last tile of the corridor
     */
    private static Coordinate calculateLastCorridorTile(Room corridor, CardinalDirection direction, Coordinate offset) {
        if (direction == CardinalDirection.NORTH || direction == CardinalDirection.WEST) {
            return new Coordinate(offset.getRow(), offset.getColumn());
        } else if (direction == CardinalDirection.SOUTH) {
            return new Coordinate(offset.getRow() + corridor.getHeight() - 1, offset.getColumn());
        } else { // CorridorDirection.EAST
            return new Coordinate(offset.getRow(), offset.getColumn() + corridor.getWidth() - 1);
        }
    }

    @Override
    protected Room digRoom(RoomType roomType) throws Exception {
        Room room = generateRoom(roomType);
        if (this.dungeon.getRooms().size() == 0) {
            dungeon.addRoom(room, new Coordinate(0, 0));
            return room;
        } else {
            for (int i = 0; i < MAX_ROOM_TRIES; i++) {
                // find a location for a door
                Room doorRoom = chooseRandomRoom();
                Coordinate doorLocation = chooseDoorLocation(doorRoom);

                // generate a corridor with a random length and find the offset within the dungeon
                int corridorLength = random.nextInt(MAX_CORRIDOR_LENGTH - MIN_CORRIDOR_LENGTH) + MIN_CORRIDOR_LENGTH;
                Room corridor = generateCorridor(doorRoom, doorLocation, corridorLength);
                Coordinate corridorOffset = calculateCorridorOffset(doorRoom, doorLocation, corridorLength);

                // if corridor can be placed, attempt to place a room
                if (this.dungeon.canAddRoom(corridor, corridorOffset)) {
                    // calculate corridor direction and location of last tile
                    CardinalDirection corridorDirection = doorRoom.determineWallDirection(doorLocation);
                    Coordinate lastCorridorTile = calculateLastCorridorTile(corridor, corridorDirection, corridorOffset);

                    // choose an offset for the next room
                    Coordinate roomOffset = chooseRoomOffset(room, corridorDirection, lastCorridorTile);

                    // try to place the next room
                    if (dungeon.canAddRoom(room, roomOffset)) {
                        // add the door to the previous room
                        this.dungeon.addTile(doorLocation.add(doorRoom.getOffset()), generateDoor());

                        // add the corridor and the new room
                        this.dungeon.addRoom(corridor, corridorOffset);
                        this.dungeon.addRoom(room, roomOffset);

                        // place a door in the new room where the corridor ends
                        Coordinate otherDoorCoords = lastCorridorTile.calculateNeighborCoordinate(corridorDirection);
                        this.dungeon.addTile(otherDoorCoords, generateDoor());

                        return room;
                    }
                }
            }
            throw new Exception("too many tries to find a room location");
        }
    }

    private Door generateDoor() {
        return new Door(false); // TODO - generate locked and secret doors
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
     * Chooses the offset of the room, depending on where a corridor's last tile is located. The coordinate of the side
     * adjacent to the corridor is randomly chosen.
     *
     * @param room              the room
     * @param corridorDirection the direction of the corridor going into the room
     * @param lastCorridorTile  the coordinates of the last tile in the corridor
     * @return the coordinates of the offset for the room
     */
    private Coordinate chooseRoomOffset(Room room, CardinalDirection corridorDirection, Coordinate lastCorridorTile) {
        if (corridorDirection == CardinalDirection.NORTH) {
            int row = lastCorridorTile.getRow() - room.getHeight();
            int col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
            return new Coordinate(row, col);
        } else if (corridorDirection == CardinalDirection.SOUTH) {
            int row = lastCorridorTile.getRow() + 1;
            int col = random.nextInt(room.getWidth() - 2) + lastCorridorTile.getColumn() - (room.getWidth() - 2);
            return new Coordinate(row, col);
        } else if (corridorDirection == CardinalDirection.WEST) {
            int row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
            int col = lastCorridorTile.getColumn() - room.getWidth();
            return new Coordinate(row, col);
        } else { // CorridorDirection.EAST
            int row = random.nextInt(room.getHeight() - 2) + lastCorridorTile.getRow() - (room.getHeight() - 2);
            int col = lastCorridorTile.getColumn() + 1;
            return new Coordinate(row, col);
        }
    }

    private Room generateRoom(RoomType roomType) throws Exception {
        int height = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int width = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        Room room = Room.createEmptyRoom(height, width, roomType);
        if (roomType == RoomType.ENTRANCE || roomType == RoomType.EXIT) {
            Coordinate location = chooseEmptyLocation(room);
            Tile tile = roomType == RoomType.ENTRANCE ? new StairsUp() : new StairsDown();
            room.addTile(location, tile);
        }

        // place some items and monsters in the room
        if (roomType == RoomType.EMPTY) {
            if (random.nextDouble() < CHANCE_OF_CHEST) {
                Coordinate chestLocation = chooseEmptyLocation(room);
                room.addTile(chestLocation, new Chest(null));
            }

            if (random.nextDouble() < CHANCE_OF_MONSTER) {
                Coordinate monsterLocation = chooseEmptyLocation(room);
                room.addTile(monsterLocation, new Monster(false));
            }

            if (random.nextDouble() < CHANCE_OF_TRAP) {
                Coordinate trapLocation = chooseEmptyLocation(room);
                room.addTile(trapLocation, new Trap());
            }
        }

        if (roomType == RoomType.EXIT) {
            Coordinate bossLocation = chooseEmptyLocation(room);
            room.addTile(bossLocation, new Monster(true));
        }

        return room;
    }

    private Room chooseRandomRoom() {
        List<Room> rooms = this.dungeon.getRooms();
        int roomNum = random.nextInt(rooms.size());
        return rooms.get(roomNum);
    }

    // TODO - create common method for finding locations

    /**
     * Randomly chooses the location of the door to place in the room.
     *
     * @param room the room to place a door in
     * @return the coordinate of the door, relative to the room
     * @throws Exception if there has been too many tries finding a location for the door
     */
    private Coordinate chooseDoorLocation(Room room) throws Exception {
        for (int i = 0; i < MAX_DOOR_TRIES; i++) {
            int row = random.nextInt(room.getHeight());
            int col = random.nextInt(room.getWidth());
            Coordinate location = new Coordinate(row, col);
            if (room.isANonCornerWall(location)) {
                return location;
            }
        }
        throw new Exception("too many tries to choose a door location");
    }

    private Coordinate chooseEmptyLocation(Room room) throws Exception {
        // find a location for the entrance/exit tile
        for (int i = 0; i < MAX_ROOM_TILE_TRIES; i++) {
            int row = random.nextInt(room.getHeight() - 2) + 1;
            int col = random.nextInt(room.getWidth() - 2) + 1;
            Coordinate location = new Coordinate(row, col);
            if (room.isAnEmptyFloor(location)) {
                return location;
            }
        }
        throw new Exception("too many tries finding a location in the room");
    }
}

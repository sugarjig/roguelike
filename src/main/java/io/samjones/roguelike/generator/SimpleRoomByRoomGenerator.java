package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SimpleRoomByRoomGenerator extends RoomByRoomGenerator {
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MAX_ROOM_HEIGHT = 20;
    public static final int MAX_ROOM_WIDTH = 20;
    public static final int MAX_DUNGEON_HEIGHT = 25;
    public static final int MAX_DUNGEON_WIDTH = 80;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomByRoomGenerator.class);
    private Random random = new Random();
    private Dungeon.Room previousRoom;
    private Coordinate previousOffset = new Coordinate(0, 0);

    protected Door generateDoor() {
        return new Door(false);
    }

    protected Coordinate chooseWall(Dungeon.Room room) {
        Coordinate wallCoordinate = null;
        while (wallCoordinate == null) { // TODO - deal with infinite loops
            int row = random.nextInt(room.getHeight());
            int col = random.nextInt(room.getWidth());
            Tile tile = room.getTile(row, col);
            if (tile instanceof Wall) {
                wallCoordinate = new Coordinate(row, col);
            }
        }
        return wallCoordinate;
    }

    @Override
    protected Coordinate addDoor(Dungeon.Room room) {
        Coordinate wallLocation = chooseWall(room);
        Door door = generateDoor();
        int row = wallLocation.getRow() + this.previousOffset.getRow();
        int col = wallLocation.getColumn() + this.previousOffset.getColumn();
        Coordinate doorLocation = new Coordinate(row, col);
        LOGGER.debug("door location: " + doorLocation);
        this.dungeon.addTile(row, col, door);
        return doorLocation;
    }

    @Override
    protected Dungeon.Room generateRoom() {
        int height = Math.max(MIN_ROOM_HEIGHT, random.nextInt(MAX_ROOM_HEIGHT + 1));
        int width = Math.max(MIN_ROOM_WIDTH, random.nextInt(MAX_ROOM_WIDTH + 1));
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
        LOGGER.debug("room generated: " + room);
        return room;
    }

    @Override
    protected boolean placeRoom(Dungeon.Room room, Dungeon dungeon) {
        int rowOffset = Math.max(1, random.nextInt(MAX_DUNGEON_HEIGHT));
        int colOffset = Math.max(1, random.nextInt(MAX_DUNGEON_WIDTH));
        Coordinate offset = new Coordinate(rowOffset, colOffset);
        boolean roomAdded = dungeon.addRoom(room, offset);
        LOGGER.debug("room added: " + roomAdded);
        if (roomAdded) {
            this.previousRoom = room;
            this.previousOffset = offset;
            LOGGER.debug("new offset: " + offset);
        }
        return roomAdded;
    }

    @Override
    protected void addCorridor(Coordinate doorLocation) {

    }
}

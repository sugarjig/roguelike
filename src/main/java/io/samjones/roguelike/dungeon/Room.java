package io.samjones.roguelike.dungeon;

import io.samjones.roguelike.dungeon.tiles.*;

/**
 * A room in a grid-based dungeon.
 */
public class Room extends Region {
    private Coordinates offset;
    private RoomType roomType;

    /**
     * Constructs a new room of the specified type.
     *
     * @param roomType the type of room (empty, corridor, entrance, exit, etc.)
     */
    public Room(RoomType roomType) {
        super();
        this.offset = new Coordinates(0, 0);
        this.roomType = roomType;
    }

    /**
     * Creates an empty room of the specified size and type.
     *
     * @param height   the height of the room
     * @param width    the width of the room
     * @param roomType the type of the room
     * @return the created room
     */
    public static Room createEmptyRoom(int height, int width, RoomType roomType) {
        Room room = new Room(roomType);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // have to specify the wall type when creating; if we store more information about rooms in a dungeon,
                // we can do this work in the view
                Tile tile;
                if (row == 0) {
                    if (col == 0) {
                        tile = new Wall(WallType.UPPER_LEFT);
                    } else if (col == width - 1) {
                        tile = new Wall(WallType.UPPER_RIGHT);
                    } else {
                        tile = new Wall(WallType.HORIZONTAL);
                    }
                } else if (row == height - 1) {
                    if (col == 0) {
                        tile = new Wall(WallType.LOWER_LEFT);
                    } else if (col == width - 1) {
                        tile = new Wall(WallType.LOWER_RIGHT);
                    } else {
                        tile = new Wall(WallType.HORIZONTAL);
                    }
                } else if (col == 0 || col == width - 1) {
                    tile = new Wall(WallType.VERTICAL);
                } else {
                    tile = new Floor();
                }
                room.addTile(new Coordinates(row, col), tile);
            }
        }
        return room;
    }

    /**
     * Creates a corridor of the specified size.
     *
     * @param height the height of the corridor
     * @param width  the width of the corridor
     * @return the created corridor
     */
    public static Room createCorridor(int height, int width) {
        Room room = new Room(RoomType.CORRIDOR);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                room.addTile(new Coordinates(row, col), new Corridor());
            }
        }
        return room;
    }

    /**
     * Returns true if a set of coordinates points to a non-corner wall.
     *
     * @param coordinates the coordinates to check
     * @return true if the coordinates point to a non-corner wall
     */
    public boolean isANonCornerWall(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getColumn();
        boolean isHorizontalWall = (row == 0 || row == this.getHeight() - 1) && col > 0 && col < this.getWidth() - 1;
        boolean isVerticalWall = (col == 0 || col == this.getWidth() - 1) && row > 0 && row < this.getHeight() - 1;
        return isHorizontalWall || isVerticalWall;
    }

    /**
     * Returns true if a set of coordinates points to an empty floor.
     *
     * @param coordinates the coordinates to check
     * @return true if the coordinates point to an empty floor tile
     */
    public boolean isAnEmptyFloor(Coordinates coordinates) {
        return this.getTile(coordinates) instanceof Floor;
    }

    /**
     * Determines the direction a wall is facing.
     *
     * @param wallLocation the location of the wall in the room; assumes the passed in value is a non-corner wall
     * @return the direction of the wall is facing; null if the coordinates do not point to a wall
     */
    public CardinalDirection determineWallDirection(Coordinates wallLocation) {
        if (wallLocation.getRow() == 0) { // top wall
            return CardinalDirection.NORTH;
        } else if (wallLocation.getRow() == this.getHeight() - 1) { // bottom wall
            return CardinalDirection.SOUTH;
        } else if (wallLocation.getColumn() == 0) { // left wall
            return CardinalDirection.WEST;
        } else if (wallLocation.getColumn() == this.getWidth() - 1) { // right wall
            return CardinalDirection.EAST;
        } else {
            return null;
        }
    }

    /**
     * Gets the offset of the room within a dungeon.
     *
     * @return the offset
     */
    public Coordinates getOffset() {
        return offset;
    }

    /**
     * Sets the offset of the room within a dungeon.
     *
     * @param offset the offset
     */
    public void setOffset(Coordinates offset) {
        this.offset = offset;
    }

    /**
     * Gets the room type.
     *
     * @return the room type
     */
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Room{" +
                "tiles=" + tiles +
                '}';
    }
}

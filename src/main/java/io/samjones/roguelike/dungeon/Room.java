package io.samjones.roguelike.dungeon;

import io.samjones.roguelike.dungeon.tiles.Corridor;
import io.samjones.roguelike.dungeon.tiles.Floor;
import io.samjones.roguelike.dungeon.tiles.Tile;
import io.samjones.roguelike.dungeon.tiles.Wall;

public class Room extends Region {
    public Room() {
        super();
    }

    public static Room createEmptyRoom(int height, int width) {
        Room room = new Room();
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
                room.addTile(new Coordinate(row, col), tile);
            }
        }
        return room;
    }

    public static Room createCorridor(int height, int width) {
        Room room = new Room();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                room.addTile(new Coordinate(row, col), new Corridor());
            }
        }
        return room;
    }

    public boolean isANonCornerWall(Coordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getColumn();
        boolean isHorizontalWall = (row == 0 || row == this.getHeight() - 1) && col > 0 && col < this.getWidth() - 1;
        boolean isVerticalWall = (col == 0 || col == this.getWidth() - 1) && row > 0 && row < this.getHeight() - 1;
        return isHorizontalWall || isVerticalWall;
    }

    public boolean isAnEmptyFloor(Coordinate location) {
        return this.getTile(location) instanceof Floor;
    }

    /**
     * Determines direction a wall is facing.
     *
     * @param wallLocation the location of the wall, relative to the room; assumes the passed in value in a wall and is
     *                     not a corner
     * @return the direction of the wall is facing
     */
    public CardinalDirection determineWallDirection(Coordinate wallLocation) {
        if (wallLocation.getRow() == 0) { // top wall
            return CardinalDirection.NORTH;
        } else if (wallLocation.getRow() == this.getHeight() - 1) { // bottom wall
            return CardinalDirection.SOUTH;
        } else if (wallLocation.getColumn() == 0) { // left wall
            return CardinalDirection.WEST;
        } else { // right wall
            return CardinalDirection.EAST;
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "tiles=" + tiles +
                '}';
    }
}

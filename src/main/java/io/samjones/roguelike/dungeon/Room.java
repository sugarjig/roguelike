package io.samjones.roguelike.dungeon;

import io.samjones.roguelike.dungeon.tiles.Corridor;
import io.samjones.roguelike.dungeon.tiles.Floor;
import io.samjones.roguelike.dungeon.tiles.Tile;
import io.samjones.roguelike.dungeon.tiles.Wall;

/**
 * Created by sam for roguelike.
 */
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

    @Override
    public String toString() {
        return "Room{" +
                "tiles=" + tiles +
                '}';
    }
}

package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.*;

public class SimpleRoomByRoomGenerator extends RoomByRoomGenerator {
    @Override
    protected Door generateDoor() {
        return new Door(false);
    }

    @Override
    protected int[] chooseWall(Dungeon.Room room) {
        return new int[] {1, 2};
    }

    @Override
    protected Dungeon.Room generateRoom() {
        Dungeon.Room room = new Dungeon.Room();
        room.addTile(0, 0, new Wall());
        room.addTile(0, 1, new Wall());
        room.addTile(0, 2, new Wall());
        room.addTile(1, 0, new Wall());
        room.addTile(1, 1, new Floor());
        room.addTile(1, 2, new Wall());
        room.addTile(2, 0, new Wall());
        room.addTile(2, 1, new Wall());
        room.addTile(2, 2, new Wall());
        return room;
    }

    @Override
    protected void placeRoom(Dungeon.Room room, Dungeon dungeon) {
        int colOffset = Math.max(0, dungeon.getNumCols() - 1);
        for (int row = 0; row < room.getNumRows(); row++) {
            for (int col = 0; col < room.getNumCols(); col++) {
                if (dungeon.getTile(row, col + colOffset) == null) {
                    Tile tile = room.getTile(row, col);
                    dungeon.addTile(row, col + colOffset, tile);
                }
            }
        }
    }
}

package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.Door;
import io.samjones.roguelike.dungeon.Dungeon;

public abstract class RoomByRoomGenerator {
    public Dungeon generate(int numRooms) {
        Dungeon dungeon = new Dungeon();

        // TODO - place entrance and exit
        Dungeon.Room currentRoom = generateRoom();
        placeRoom(currentRoom, dungeon);
        for (int i = 1; i < numRooms; i++) {
            int[] wallTile = chooseWall(currentRoom);
            Door door = generateDoor();
            currentRoom.addTile(wallTile[0], wallTile[1], door);
            currentRoom = generateRoom();
            placeRoom(currentRoom, dungeon);
        }

        return dungeon;
    }

    protected abstract Door generateDoor();

    protected abstract int[] chooseWall(Dungeon.Room firstRoom);


    protected abstract void placeRoom(Dungeon.Room room, Dungeon dungeon);

    protected abstract Dungeon.Room generateRoom();
}

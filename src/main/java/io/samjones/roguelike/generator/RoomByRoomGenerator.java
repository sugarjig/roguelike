package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Coordinate;
import io.samjones.roguelike.dungeon.Dungeon;

public abstract class RoomByRoomGenerator implements DungeonGenerator {
    protected Dungeon dungeon;

    public Dungeon generate(int numRooms) {
        this.dungeon = new Dungeon();

        // TODO - place entrance and exit
        int placedRooms = 0;
        Dungeon.Room currentRoom = generateRoom();
        boolean roomPlaced = placeRoom(currentRoom, dungeon);
        if (roomPlaced) {
            placedRooms++;
        }
        while (placedRooms < numRooms) {
            if (roomPlaced) {
                Coordinate doorLocation = addDoor(currentRoom);
                addCorridor(doorLocation);
            }
            currentRoom = generateRoom();
            roomPlaced = placeRoom(currentRoom, dungeon);
            if (roomPlaced) {
                placedRooms++;
            }
        }

        return dungeon;
    }

    protected abstract void addCorridor(Coordinate doorLocation);

    protected abstract Coordinate addDoor(Dungeon.Room currentRoom);

    // TODO - condense down to one method (addRoom)
    protected abstract boolean placeRoom(Dungeon.Room room, Dungeon dungeon);

    protected abstract Dungeon.Room generateRoom();
}

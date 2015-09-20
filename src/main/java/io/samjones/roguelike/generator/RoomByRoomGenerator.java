package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Dungeon;

public abstract class RoomByRoomGenerator implements DungeonGenerator {
    protected Dungeon dungeon;

    public Dungeon generate(int numRooms) {
        this.dungeon = new Dungeon();

        // TODO - place entrance and exit
        int placedRooms = 0;
        Dungeon.Room currentRoom = addRoom(null);
        placedRooms++;
        while (placedRooms < numRooms) {
            currentRoom = addRoom(currentRoom);
            placedRooms++;
        }

        return dungeon;
    }

    /**
     * Add a room to the dungeon. The implementer of this method must determine where to put the room and guarantee that
     * it has been placed before returning.
     *
     * @param room the previous room placed; null if the first room in the dungeon
     * @return the resulting room
     */
    protected abstract Dungeon.Room addRoom(Dungeon.Room room);
}

package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Dungeon;
import io.samjones.roguelike.dungeon.Room;
import io.samjones.roguelike.dungeon.RoomType;

/**
 * An implementation of a dungeon generator that creates a dungeon one room at a time.
 */
public abstract class DiggingGenerator implements DungeonGenerator {
    protected Dungeon dungeon;

    public Dungeon generate(int numRooms) throws Exception {
        this.dungeon = new Dungeon();

        Room currentRoom = null;
        for (int i = 0; i < numRooms; i++) {
            RoomType roomType;
            if (i == 0) {
                roomType = RoomType.ENTRANCE;
            } else if (i == numRooms - 1) {
                roomType = RoomType.EXIT;
            } else {
                roomType = RoomType.EMPTY;
            }
            currentRoom = digRoom(currentRoom, roomType);
        }

        return dungeon;
    }

    /**
     * Add a room to the dungeon. The implementer of this method must determine where to put the room and must guarantee
     * that it has been placed before returning.
     *
     * @param room the previous room placed; use null if the first room in the dungeon
     * @return the resulting room
     * @throws Exception if there is a problem placing the room
     */
    protected abstract Room digRoom(Room room, RoomType roomType) throws Exception;
}

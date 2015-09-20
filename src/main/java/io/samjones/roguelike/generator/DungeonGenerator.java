package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Dungeon;

/**
 * Interface for a dungeon generator.
 */
public interface DungeonGenerator {
    /**
     * Generate a dungeon with a specified number of rooms.
     *
     * @param numRooms the number of rooms to generate
     * @return the dungeon
     * @throws Exception if there was a problem placing any of the rooms, usually due to failing too many times to place
     *                   a room
     */
    Dungeon generate(int numRooms) throws Exception;
}

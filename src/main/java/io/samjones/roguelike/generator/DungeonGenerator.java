package io.samjones.roguelike.generator;

import io.samjones.roguelike.dungeon.Dungeon;

public interface DungeonGenerator {
    public Dungeon generate(int numRooms) throws Exception;
}

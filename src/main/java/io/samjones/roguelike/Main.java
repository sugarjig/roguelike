package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.Dungeon;
import io.samjones.roguelike.generator.DungeonGenerator;
import io.samjones.roguelike.generator.RandomRoomDiggingGenerator;
import io.samjones.roguelike.view.DungeonViewer;

public class Main {
    public static final int DEFAULT_NUM_ROOMS = 10;

    public static void main(String[] args) throws Exception {
        DungeonGenerator generator = new RandomRoomDiggingGenerator();
        int numRooms;
        try {
            numRooms = Integer.parseInt(args[0]);
        } catch (Exception e) {
            numRooms = DEFAULT_NUM_ROOMS;
        }
        Dungeon dungeon = generator.generate(numRooms);

        DungeonViewer dungeonViewer = new DungeonViewer(dungeon);
        dungeonViewer.showTerminal();
    }
}

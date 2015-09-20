package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.Dungeon;
import io.samjones.roguelike.generator.DungeonGenerator;
import io.samjones.roguelike.generator.SimpleRoomByRoomGenerator;
import io.samjones.roguelike.view.DungeonViewer;

public class Main {
    public static void main(String[] args) {
        DungeonGenerator generator = new SimpleRoomByRoomGenerator();
        Dungeon dungeon = generator.generate(10);

        DungeonViewer dungeonViewer = new DungeonViewer(dungeon);
        dungeonViewer.showTerminal();
    }
}

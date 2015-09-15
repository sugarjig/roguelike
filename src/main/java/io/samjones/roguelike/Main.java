package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.*;

public class Main {
    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon(5, 5);

        dungeon.addTile(0, 0, new Wall());
        dungeon.addTile(0, 1, new Wall());
        dungeon.addTile(0, 2, new Wall());
        dungeon.addTile(0, 3, new Wall());
        dungeon.addTile(0, 4, new Wall());

        dungeon.addTile(1, 0, new Wall());
        dungeon.addTile(1, 1, new StairsUp());
        dungeon.addTile(1, 2, new Floor());
        dungeon.addTile(1, 3, new Floor());
        dungeon.addTile(1, 4, new Wall());

        dungeon.addTile(2, 0, new Wall());
        dungeon.addTile(2, 1, new Floor());
        dungeon.addTile(2, 2, new Floor());
        dungeon.addTile(2, 3, new Floor());
        dungeon.addTile(2, 4, new Wall());

        dungeon.addTile(3, 0, new Wall());
        dungeon.addTile(3, 1, new Floor());
        dungeon.addTile(3, 2, new Floor());
        dungeon.addTile(3, 3, new StairsDown());
        dungeon.addTile(3, 4, new Wall());

        dungeon.addTile(4, 0, new Wall());
        dungeon.addTile(4, 1, new Wall());
        dungeon.addTile(4, 2, new Wall());
        dungeon.addTile(4, 3, new Wall());
        dungeon.addTile(4, 4, new Wall());

        DungeonPrinter.print(dungeon);
    }
}

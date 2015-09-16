package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.*;

public class Main {
    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon();

        dungeon.addTile(5, 5, new Wall());
        dungeon.addTile(5, 6, new Wall());
        dungeon.addTile(5, 7, new Wall());
        dungeon.addTile(5, 8, new Wall());
        dungeon.addTile(5, 9, new Wall());

        dungeon.addTile(6, 5, new Wall());
        dungeon.addTile(6, 6, new StairsUp());
        dungeon.addTile(6, 7, new Floor());
        dungeon.addTile(6, 8, new Floor());
        dungeon.addTile(6, 9, new Wall());

        dungeon.addTile(7, 5, new Wall());
        dungeon.addTile(7, 6, new Floor());
        dungeon.addTile(7, 7, new Floor());
        dungeon.addTile(7, 8, new Floor());
        dungeon.addTile(7, 9, new Wall());

        dungeon.addTile(8, 5, new Wall());
        dungeon.addTile(8, 6, new Floor());
        dungeon.addTile(8, 7, new Floor());
        dungeon.addTile(8, 8, new StairsDown());
        dungeon.addTile(8, 9, new Wall());

        dungeon.addTile(9, 5, new Wall());
        dungeon.addTile(9, 6, new Wall());
        dungeon.addTile(9, 7, new Wall());
        dungeon.addTile(9, 8, new Wall());
        dungeon.addTile(9, 9, new Wall());

        DungeonPrinter.print(dungeon);
    }
}

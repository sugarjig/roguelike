package io.samjones.roguelike.dungeon.tiles;

public class Chest implements Tile {
    Item item;

    public Chest(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public static class Item {
        private String name;

        public Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

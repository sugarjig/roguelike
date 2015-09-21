package io.samjones.roguelike.dungeon.tiles;

/**
 * A chest tile. Can contain an item.
 */
public class Chest implements Tile {
    Item item;

    /**
     * Constructs a new chest.
     *
     * @param item an item to put in the chest; can be null
     */
    public Chest(Item item) {
        this.item = item;
    }

    /**
     * Gets the item in the chest.
     *
     * @return the item in the chest; null if no item
     */
    public Item getItem() {
        return item;
    }

    /**
     * An item that can be put in a chest.
     */
    public static class Item {
        private String name;

        /**
         * Constructs a new item.
         *
         * @param name a name for the item
         */
        public Item(String name) {
            this.name = name;
        }

        /**
         * Gets the name of the item
         *
         * @return the name of the item
         */
        public String getName() {
            return name;
        }
    }
}

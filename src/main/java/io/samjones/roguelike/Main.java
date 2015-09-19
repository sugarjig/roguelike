package io.samjones.roguelike;

import com.googlecode.blacken.grid.Grid;

public class Main {
    public static final int EMPTY_FLOOR = 0x20;
    public static final int WALL = 0x23;
    public static final int NUM_ROWS = 100;
    public static final int NUM_COLS = 100;

    public static void main(String[] args) {
        Grid<Integer> grid = new Grid<>(EMPTY_FLOOR, NUM_ROWS, NUM_COLS);

        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                if (x == NUM_COLS - 1) {
                    grid.set(y, x, WALL);
                } else if (y == NUM_ROWS - 1) {
                    grid.set(y, x, WALL);
                } else if (x == 0 || y == 0) {
                    grid.set(y, x, WALL);
                }
            }
        }

        DungeonViewer dungeonViewer = new DungeonViewer(grid);
        dungeonViewer.show();
    }
}

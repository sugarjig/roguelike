package io.samjones.roguelike;

import com.googlecode.blacken.grid.Grid;
import io.samjones.roguelike.view.DungeonViewer;
import io.samjones.roguelike.view.TileView;

public class Main {
    public static final int NUM_ROWS = 23;
    public static final int NUM_COLS = 80;

    public static void main(String[] args) {
        Grid<Integer> grid = new Grid<>(TileView.FLOOR, NUM_ROWS, NUM_COLS);

        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                if (x == NUM_COLS - 1) {
                    grid.set(y, x, TileView.WALL);
                } else if (y == NUM_ROWS - 1) {
                    grid.set(y, x, TileView.WALL);
                } else if (x == 0 || y == 0) {
                    grid.set(y, x, TileView.WALL);
                }
            }
        }

        DungeonViewer dungeonViewer = new DungeonViewer(grid);
        dungeonViewer.show();
    }
}

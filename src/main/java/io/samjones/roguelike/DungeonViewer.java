package io.samjones.roguelike;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.CursesLikeAPI;
import com.googlecode.blacken.terminal.TerminalInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DungeonViewer {
    public static final int TERMINAL_HEIGHT = 25;
    public static final int TERMINAL_WIDTH = 80;
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonViewer.class);
    private CursesLikeAPI cursesTerminal;
    private Grid<Integer> grid;
    private Point offset = new Point(0, 0);

    public DungeonViewer(Grid<Integer> grid) {
        TerminalInterface terminal = new SwingTerminal();
        terminal.init("Roguelike Dungeon Viewer", TERMINAL_HEIGHT, TERMINAL_WIDTH);
        this.cursesTerminal = new CursesLikeAPI(terminal);

        ColorPalette colorPalette = new ColorPalette();
        colorPalette.addAll(ColorNames.XTERM_256_COLORS, false);
        colorPalette.putMapping(ColorNames.SVG_COLORS);
        this.cursesTerminal.setPalette(colorPalette);

        this.grid = grid;
    }

    public void show() {
        boolean quit = false;
        while (!quit) {
            showGrid(grid);

            // get user input
            int ch = cursesTerminal.getch();
            switch (ch) {
                case BlackenKeys.RESIZE_EVENT:
                    cursesTerminal.clear();
                    break;
                case BlackenKeys.KEY_ESCAPE:
                    quit = true;
                    break;
                case BlackenKeys.KEY_UP:
                    moveScreen(-1, 0);
                    break;
                case BlackenKeys.KEY_DOWN:
                    moveScreen(1, 0);
                    break;
                case BlackenKeys.KEY_LEFT:
                    moveScreen(0, -1);
                    break;
                case BlackenKeys.KEY_RIGHT:
                    moveScreen(0, 1);
                    break;
            }
        }

        cursesTerminal.quit();
    }

    private void showGrid(Grid<Integer> grid) {
        for (int x = 0; x < cursesTerminal.getWidth(); x++) {
            for (int y = 0; y < cursesTerminal.getHeight(); y++) {
                int col = y + offset.getY();
                int row = x + offset.getX();
                if (grid.contains(col, row)) {
                    int cell = grid.get(col, row);
                    cursesTerminal.set(y, x, new String(Character.toChars(cell)), null, null, null, null);
                }
            }
        }
    }

    private void moveScreen(int y, int x) {
        this.offset = new Point(offset.getY() + y, offset.getX() + x);
        LOGGER.debug("new screen offset: " + this.offset);
        this.cursesTerminal.clear();
    }
}

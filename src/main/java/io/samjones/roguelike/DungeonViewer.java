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

/**
 * Created by sam for roguelike.
 */
public class DungeonViewer {
    public static final int TERMINAL_HEIGHT = 25;
    public static final int TERMINAL_WIDTH = 80;
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonViewer.class);
    private CursesLikeAPI cursesTerminal;
    private Point cursorLocation = new Point(0, 0);

    public DungeonViewer() {
        TerminalInterface terminal = new SwingTerminal();
        terminal.init("Roguelike Dungeon Viewer", TERMINAL_HEIGHT, TERMINAL_WIDTH);
        this.cursesTerminal = new CursesLikeAPI(terminal);

        ColorPalette colorPalette = new ColorPalette();
        colorPalette.addAll(ColorNames.XTERM_256_COLORS, false);
        colorPalette.putMapping(ColorNames.SVG_COLORS);
        cursesTerminal.setPalette(colorPalette);
    }

    public void show(Grid<Integer> grid) {
        boolean quit = false;
        while (!quit) {
            for (int x = 0; x < cursesTerminal.getWidth(); x++) {
                for (int y = 0; y < cursesTerminal.getHeight(); y++) {
                    if (grid.contains(y, x)) {
                        int cell = grid.get(y, x);
                        cursesTerminal.set(y, x, new String(Character.toChars(cell)), null, null, null, null);
                    }
                }
            }

            int ch = cursesTerminal.getch();
            switch (ch) {
                case BlackenKeys.RESIZE_EVENT:
                    cursesTerminal.clear();
                    break;
                case BlackenKeys.KEY_ESCAPE:
                    quit = true;
                    break;
                case BlackenKeys.KEY_UP:
                    moveCursor(-1, 0);
                    break;
                case BlackenKeys.KEY_DOWN:
                    moveCursor(1, 0);
                    break;
                case BlackenKeys.KEY_LEFT:
                    moveCursor(0, -1);
                    break;
                case BlackenKeys.KEY_RIGHT:
                    moveCursor(0, 1);
                    break;
            }
        }

        cursesTerminal.quit();
    }

    private void moveCursor(int y, int x) {
        this.cursorLocation = new Point(cursorLocation.getY() + y, cursorLocation.getX() + x);
        this.cursesTerminal.move(this.cursorLocation.getY(), this.cursorLocation.getX());
        LOGGER.debug("new cursor location: " + this.cursorLocation);
    }
}

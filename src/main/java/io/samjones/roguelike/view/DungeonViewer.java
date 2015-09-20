package io.samjones.roguelike.view;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.CursesLikeAPI;
import com.googlecode.blacken.terminal.TerminalInterface;
import io.samjones.roguelike.dungeon.Coordinate;
import io.samjones.roguelike.dungeon.Dungeon;
import io.samjones.roguelike.dungeon.tiles.*;

public class DungeonViewer {
    public static final int TERMINAL_HEIGHT = 25;
    public static final int TERMINAL_WIDTH = 80;
    private CursesLikeAPI cursesTerminal;
    private Grid<Integer> grid;
    private Point offset = new Point(0, 0);

    public DungeonViewer(Dungeon dungeon) {
        TerminalInterface terminal = new SwingTerminal();
        terminal.init("Roguelike Dungeon Viewer", TERMINAL_HEIGHT, TERMINAL_WIDTH);
        this.cursesTerminal = new CursesLikeAPI(terminal);

        ColorPalette colorPalette = new ColorPalette();
        colorPalette.addAll(ColorNames.XTERM_256_COLORS, false);
        colorPalette.putMapping(ColorNames.SVG_COLORS);
        this.cursesTerminal.setPalette(colorPalette);

        this.grid = translateDungeon(dungeon);
    }

    private Grid<Integer> translateDungeon(Dungeon dungeon) {
        Grid<Integer> grid = new Grid<>(TileView.NULL_TILE, dungeon.getHeight(), dungeon.getWidth());
        for (int row = 0; row < grid.getHeight(); row++) {
            for (int col = 0; col < grid.getWidth(); col++) {
                Tile tile = dungeon.getTile(new Coordinate(row, col));
                grid.set(row, col, translateTile(tile));
            }
        }
        return grid;
    }

    private int translateTile(Tile tile) {
        if (tile instanceof Floor) {
            return TileView.FLOOR;
        } else if (tile instanceof Door) {
            return TileView.DOOR;
        } else if (tile instanceof StairsUp) {
            return TileView.STAIRS_UP;
        } else if (tile instanceof StairsDown) {
            return TileView.STAIRS_DOWN;
        } else if (tile instanceof Wall) {
            Wall wall = (Wall) tile;
            WallType wallType = wall.getWallType();
            if (wallType == WallType.HORIZONTAL) {
                return TileView.WALL_HORIZONTAL;
            } else if (wallType == WallType.VERTICAL) {
                return TileView.WALL_VERTICAL;
            } else if (wallType == WallType.UPPER_LEFT) {
                return TileView.WALL_UPPER_LEFT;
            } else if (wallType == WallType.UPPER_RIGHT) {
                return TileView.WALL_UPPER_RIGHT;
            } else if (wallType == WallType.LOWER_LEFT) {
                return TileView.WALL_LOWER_LEFT;
            } else { // Wall.WallType.LOWER_RIGHT
                return TileView.WALL_LOWER_RIGHT;
            }
        } else if (tile instanceof Corridor) {
            return TileView.CORRIDOR;
        } else if (tile instanceof Chest) {
            return TileView.CHEST;
        } else if (tile instanceof Monster) {
            Monster monster = (Monster) tile;
            return monster.isBoss() ? TileView.BOSS : TileView.MONSTER;
        } else if (tile instanceof Trap) {
            return TileView.TRAP;
        } else {
            return TileView.NULL_TILE;
        }
    }

    public void showTerminal() {
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
        int xOffset = offset.getX() + x;
        int yOffset = offset.getY() + y;
        int maxXOffset = Math.max(0, this.grid.getWidth() - this.cursesTerminal.getWidth());
        int maxYOffset = Math.max(0, this.grid.getHeight() - this.cursesTerminal.getHeight());
        if (xOffset >= 0 && xOffset <= maxXOffset && yOffset >= 0 && yOffset <= maxYOffset) {
            this.offset = new Point(yOffset, xOffset);
            this.cursesTerminal.clear();
        }
    }
}

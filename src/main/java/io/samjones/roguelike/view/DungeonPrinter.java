package io.samjones.roguelike.view;

import io.samjones.roguelike.dungeon.*;

import java.util.stream.IntStream;

/**
 * Prints a grid-based dungeon, where each tile is represented by a character.
 */
public class DungeonPrinter {
    public static final char CORNER_BOUND = '+';
    public static final char HORIZONTAL_BOUND = '-';
    public static final char VERTICAL_BOUND = '|';
    public static final char NULL_TILE = ' ';

    public static void print(Dungeon dungeon) {
        TilePrinter tilePrinter = new TilePrinter();

        printHorizontalBounds(dungeon.getNumCols());
        // TODO - get an Iterable of rows
        IntStream.range(0, dungeon.getNumRows()).forEachOrdered(row -> {
            System.out.print(VERTICAL_BOUND);
            // print a row
            IntStream.range(0, dungeon.getNumCols()).forEachOrdered(col -> {
                // print a tile in a row
                Tile tile = dungeon.getTile(row, col);
                if (tile == null) {
                    System.out.print(NULL_TILE);
                } else {
                    tile.accept(tilePrinter);
                }
            });
            System.out.print(VERTICAL_BOUND);
            System.out.println();
        });
        printHorizontalBounds(dungeon.getNumCols());
    }

    private static void printHorizontalBounds(int numCols) {
        System.out.print(CORNER_BOUND);
        IntStream.range(0, numCols).forEachOrdered(i -> System.out.print(HORIZONTAL_BOUND));
        System.out.print(CORNER_BOUND);
        System.out.println();
    }

    /**
     * A class that contains methods to print the various tiles in a dungeon. Each tile corresponds to a unique
     * character. Uses a variant of the Visitor pattern. The goal is to keep the representation of a dungeon separate
     * from its presentation.
     */
    public static class TilePrinter implements TileVisitor {
        @Override
        public void visit(Floor floor) {
            System.out.print(TileView.FLOOR);
        }

        @Override
        public void visit(Wall wall) {
            System.out.print(TileView.WALL);
        }

        @Override
        public void visit(Door door) {
            System.out.print(TileView.DOOR);
        }

        @Override
        public void visit(StairsUp stairsUp) {
            System.out.print(TileView.STAIRS_UP);
        }

        @Override
        public void visit(StairsDown stairsDown) {
            System.out.print(TileView.STAIRS_DOWN);
        }
    }
}

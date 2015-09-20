package io.samjones.roguelike.dungeon;


public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(this.getRow() + coordinate.getRow(), this.getColumn() + coordinate.getColumn());
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}

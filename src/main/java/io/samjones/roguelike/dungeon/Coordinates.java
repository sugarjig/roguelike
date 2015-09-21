package io.samjones.roguelike.dungeon;

/**
 * A set of coordinates in a grid-based dungeon. Represents a 0-based row and column.
 */
public class Coordinates {
    private int row;
    private int column;

    /**
     * Constructs a new set of coordinates.
     *
     * @param row    the row
     * @param column the column
     */
    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Gets the 0-based row component of the coordinates.
     *
     * @return the row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the 0-based column component of the coordinates.
     *
     * @return the column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Adds one set of coordinates to this one.
     *
     * @param coordinates the coordinates to add
     * @return the resulting coordinates
     */
    public Coordinates add(Coordinates coordinates) {
        return new Coordinates(this.getRow() + coordinates.getRow(), this.getColumn() + coordinates.getColumn());
    }

    /**
     * Calculates the coordinates of an adjacent coordinate, given a direction.
     *
     * @param direction the direction the adjacent coordinate is in
     * @return the coordinates of the adjacent coordinate
     */
    public Coordinates calculateNeighborCoordinate(CardinalDirection direction) {
        if (direction == CardinalDirection.NORTH) {
            return new Coordinates(this.getRow() - 1, this.getColumn());
        } else if (direction == CardinalDirection.SOUTH) {
            return new Coordinates(this.getRow() + 1, this.getColumn());
        } else if (direction == CardinalDirection.WEST) {
            return new Coordinates(this.getRow(), this.getColumn() - 1);
        } else { // CorridorDirection.EAST
            return new Coordinates(this.getRow(), this.getColumn() + 1);
        }
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}

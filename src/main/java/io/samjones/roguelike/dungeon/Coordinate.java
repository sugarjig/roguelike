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

    /**
     * Calculates the coordinates of an adjacent coordinate, given a direction.
     *
     * @param direction the direction the adjacent coordinate is in
     * @return the coordinates of the adjacent coordinate
     */
    public Coordinate calculateNeighborCoordinate(CardinalDirection direction) {
        if (direction == CardinalDirection.NORTH) {
            return new Coordinate(this.getRow() - 1, this.getColumn());
        } else if (direction == CardinalDirection.SOUTH) {
            return new Coordinate(this.getRow() + 1, this.getColumn());
        } else if (direction == CardinalDirection.WEST) {
            return new Coordinate(this.getRow(), this.getColumn() - 1);
        } else { // CorridorDirection.EAST
            return new Coordinate(this.getRow(), this.getColumn() + 1);
        }
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}

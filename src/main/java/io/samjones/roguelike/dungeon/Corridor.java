package io.samjones.roguelike.dungeon;

public class Corridor implements Tile {
    @Override
    public void accept(TileVisitor tileVisitor) {
        tileVisitor.visit(this);
    }
}

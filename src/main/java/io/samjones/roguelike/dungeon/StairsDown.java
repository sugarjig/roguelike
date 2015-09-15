package io.samjones.roguelike.dungeon;

public class StairsDown implements Tile {
    @Override
    public void accept(TileVisitor tileVisitor) {
        tileVisitor.visit(this);
    }
}

package io.samjones.roguelike.dungeon;

public class Floor implements Tile {
    @Override
    public void accept(TileVisitor tileVisitor) {
        tileVisitor.visit(this);
    }
}

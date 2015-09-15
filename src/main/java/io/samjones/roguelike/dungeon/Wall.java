package io.samjones.roguelike.dungeon;

public class Wall implements Tile {
    @Override
    public void accept(TileVisitor tileVisitor) {
        tileVisitor.visit(this);
    }
}

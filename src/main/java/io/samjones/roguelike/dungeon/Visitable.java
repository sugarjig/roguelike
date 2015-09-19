package io.samjones.roguelike.dungeon;

public interface Visitable {
    void accept(TileVisitor tileVisitor);
}

package io.samjones.roguelike;

import io.samjones.roguelike.dungeon.TileVisitor;

public interface Visitable {
    void accept(TileVisitor tileVisitor);
}

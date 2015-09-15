package io.samjones.roguelike.dungeon;

public interface TileVisitor {
    void visit(Floor floor);

    void visit(Wall wall);

    void visit(Door door);

    void visit(StairsUp stairsUp);

    void visit(StairsDown stairsDown);
}

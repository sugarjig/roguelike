package io.samjones.roguelike.view;

public interface TileView {
    int FLOOR = '.';
    int WALL = 0x2591;
    int DOOR = '=';
    int STAIRS_UP = '^';
    int STAIRS_DOWN = 'v';
    int CORRIDOR = '#';
    int NULL_TILE = ' ';
    // TODO - use box drawing unicode
}

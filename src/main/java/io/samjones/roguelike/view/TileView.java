package io.samjones.roguelike.view;

public interface TileView {
    int FLOOR = '.';
    int WALL = 0x2591;
    int DOOR = '=';
    int STAIRS_UP = 0x21d1;
    int STAIRS_DOWN = 0x21d3;
    int CORRIDOR = '#';
    int NULL_TILE = ' ';
    // TODO - use box drawing unicode
}

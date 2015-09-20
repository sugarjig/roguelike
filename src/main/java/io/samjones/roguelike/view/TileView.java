package io.samjones.roguelike.view;

public interface TileView {
    int FLOOR = '.';
    int WALL = '#';
    int DOOR = '=';
    int STAIRS_UP = '^';
    int STAIRS_DOWN = 'v';
    int CORRIDOR = 0x2591;
    int NULL_TILE = ' ';
}

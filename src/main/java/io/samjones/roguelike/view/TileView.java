package io.samjones.roguelike.view;

public interface TileView {
    int FLOOR = ' ';
    int WALL_HORIZONTAL = 0x2550;
    int WALL_VERTICAL = 0x2551;
    int WALL_UPPER_LEFT = 0x2554;
    int WALL_UPPER_RIGHT = 0x2557;
    int WALL_LOWER_LEFT = 0x255a;
    int WALL_LOWER_RIGHT = 0x255d;
    int DOOR = 0x256c;
    int STAIRS_UP = 0x25b3;
    int STAIRS_DOWN = 0x25bd;
    int CORRIDOR = 0x2591;
    int CHEST = 0x2617;
    int MONSTER = 0x2620;
    int BOSS = 0x265b;
    int NULL_TILE = 0x2593;
}

# A Simple Roguelike Dungeon Generator

This is a VERY simple Roguelike dungeon generator. Right now it generates a specified number of rooms. The generator
will create an entrance and exit, and there will be no unreachable rooms.

The dungeon is displayed using [Blacken's](https://code.google.com/p/blacken/) Curses-like Swing terminal. You can use
the arrow keys to move the view around, and press escape to quit.

To run, you must have Java 1.8 installed on your system. `cd` to the project root and execute:

```
./gradlew build
java -jar build/libs/roguelike-1.0-SNAPSHOT.jar <number of rooms>
```

replacing `<number of rooms>` with however many rooms you want to generate. If you don't specify anything, the default
is 10. Try not to do too many; the code written here probably won't scale well.

Enjoy!

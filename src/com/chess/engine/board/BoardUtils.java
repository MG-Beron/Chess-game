package com.chess.engine.board;

public class BoardUtils {

    public static boolean[] FIRST_COLUMN = initColumn(0);
    public static boolean[] SECOND_COLUMN = initColumn(1);
    public static boolean[] SEVENTH_COLUMN = initColumn(6);
    public static boolean[] EIGHT_COLUMN = initColumn(7);
    
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);

        return column;
    }
}

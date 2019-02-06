package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

/**
 * The board utility class
 */
public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    /**
     * If the coordinate is between 0 and 63
     * @param coordinate An integer coordinate number
     * @return True in valid coordinate, false otherwise
     */
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= START_TILE_INDEX && coordinate < NUM_TILES;
    }

    /**
     * @param position The given position
     * @return The number that correspondence to the position
     */
    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    /**
     * @param coordinate Integer coordinate between 0 and 63
     * @return The algebraic notation E.G "a8", "b3"
     * @see <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">Algebraic notation</>
     */
    public static String getPositionAtCoordinate(int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }

    /**
     * If the game is in check the board is threatened and should stop the check
     * @param board The board
     * @return True if the game is in check
     */
    public static boolean isThreatenedBoardImmediate(Board board) {
        return board.whitePlayer().isInCheck() || board.blackPlayer().isInCheck();
    }

    /**
     * If the current player is in checkmate or in stalemate the game is over
     * @param board The board
     * @return True if the game is over, false otherwise
     */
    public static boolean isEndGame(Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }

    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[]  initRow(int rowNumber) {
        boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<String, Integer>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return positionToCoordinate;
    }

    private static String[] initializeAlgebraicNotation() {
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }
}
package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

/**
 * Utility class holding all possible pieces in the board
 * Using Immutable Table<V, V, V> from the guava library
 */
public class PieceUtils {

    private final static Table<Alliance, Integer, Queen> ALL_POSSIBLE_QUEENS = createAllPossibleMovedQueens();
    private final static Table<Alliance, Integer, Rook> ALL_POSSIBLE_ROOKS = createAllPossibleMovedRooks();
    private final static Table<Alliance, Integer, Knight> ALL_POSSIBLE_KNIGHTS = createAllPossibleMovedKnights();
    private final static Table<Alliance, Integer, Bishop> ALL_POSSIBLE_BISHOPS = createAllPossibleMovedBishops();
    private final static Table<Alliance, Integer, Pawn> ALL_POSSIBLE_PAWNS = createAllPossibleMovedPawns();

    /**
     * @param move THe pawn move
     * @return A possible pawn piece
     */
    public static Pawn getMovedPawn(Move move) {
        return ALL_POSSIBLE_PAWNS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    /**
     * @param move The knight move
     * @return A possible knight piece
     */
    public static Knight getMovedKnight(Move move) {
        return ALL_POSSIBLE_KNIGHTS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    /**
     * @param move The knight move
     * @return A possible bishop piece
     */
    public static Bishop getMovedBishop(Move move) {
        return ALL_POSSIBLE_BISHOPS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    /**
     * @param move The rook move
     * @return A possible rook piece
     */
    public static Rook getMovedRook(Move move) {
        return ALL_POSSIBLE_ROOKS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    /**
     * @param move The queen move
     * @return A possible queen piece
     */
    public static Queen getMovedQueen(Move move) {
        return ALL_POSSIBLE_QUEENS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    private static Table<Alliance, Integer, Pawn> createAllPossibleMovedPawns() {
        ImmutableTable.Builder<Alliance, Integer, Pawn> pieces = ImmutableTable.builder();
        for (Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(alliance, i, new Pawn(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Knight> createAllPossibleMovedKnights() {
        ImmutableTable.Builder<Alliance, Integer, Knight> pieces = ImmutableTable.builder();
        for (Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(alliance, i, new Knight(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Bishop> createAllPossibleMovedBishops() {
        ImmutableTable.Builder<Alliance, Integer, Bishop> pieces = ImmutableTable.builder();
        for (Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(alliance, i, new Bishop(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Rook> createAllPossibleMovedRooks() {
        ImmutableTable.Builder<Alliance, Integer, Rook> pieces = ImmutableTable.builder();
        for (Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(alliance, i, new Rook(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Queen> createAllPossibleMovedQueens() {
        ImmutableTable.Builder<Alliance, Integer, Queen> pieces = ImmutableTable.builder();
        for (Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pieces.put(alliance, i, new Queen(alliance, i, false));
            }
        }
        return pieces.build();
    }
}
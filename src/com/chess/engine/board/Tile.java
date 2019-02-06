package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * The tile class
 * holding empty tile and occupied tile classes
 */
public abstract class Tile {

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
    private static final Table<Integer, Piece, OccupiedTile> OCCUPIED_TILES = createAllPossibleOccupiedTiles();

    protected int tileCoordinate;

    private Tile(int coordinate) {
        this.tileCoordinate = coordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static Tile createTile(int coordinate, Piece piece) {

        if (piece == null) {
            return EMPTY_TILES.get(coordinate);
        }

        OccupiedTile cachedOccupiedTile = OCCUPIED_TILES.get(coordinate, piece);

        if (cachedOccupiedTile != null) {
            return cachedOccupiedTile;
        }

        return new OccupiedTile(coordinate, piece);
    }

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        Map<Integer, EmptyTile> emptyTileMap = new HashMap<Integer, EmptyTile>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return emptyTileMap;
    }

    /**
     * The cached occupied tiles
     *
     * @return a Table from the guava library
     */
    private static Table<Integer, Piece, OccupiedTile> createAllPossibleOccupiedTiles() {
        Table<Integer, Piece, OccupiedTile> occupiedTileTable = HashBasedTable.create();

        for ( Alliance alliance : Alliance.values()) {
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                Knight whiteKnightFirstMove = new Knight(alliance, i, true);
                Knight whiteKnightMoved = new Knight(alliance, i, false);
                occupiedTileTable.put(i, whiteKnightFirstMove, new OccupiedTile(i, whiteKnightFirstMove));
                occupiedTileTable.put(i, whiteKnightMoved, new OccupiedTile(i, whiteKnightMoved));
            }

            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                Bishop whiteBishopFirstMove = new Bishop(alliance, i, true);
                Bishop whiteBishopMoved = new Bishop(alliance, i, false);
                occupiedTileTable.put(i, whiteBishopFirstMove, new OccupiedTile(i, whiteBishopFirstMove));
                occupiedTileTable.put(i, whiteBishopMoved, new OccupiedTile(i, whiteBishopMoved));
            }

            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                Rook whiteRookFirstMove = new Rook(alliance, i, true);
                Rook whiteRookMoved = new Rook(alliance, i, false);
                occupiedTileTable.put(i, whiteRookFirstMove, new OccupiedTile(i, whiteRookFirstMove));
                occupiedTileTable.put(i, whiteRookMoved, new OccupiedTile(i, whiteRookMoved));
            }

            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                Queen whiteQueenFirstMove = new Queen(alliance, i, true);
                Queen whiteQueenMoved = new Queen(alliance, i, false);
                occupiedTileTable.put(i, whiteQueenFirstMove, new OccupiedTile(i, whiteQueenFirstMove));
                occupiedTileTable.put(i, whiteQueenMoved, new OccupiedTile(i, whiteQueenMoved));
            }

            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                Pawn whitePawnFirstMove = new Pawn(alliance, i, true);
                Pawn whitePawnMoved = new Pawn(alliance, i, false);
                occupiedTileTable.put(i, whitePawnFirstMove, new OccupiedTile(i, whitePawnFirstMove));
                occupiedTileTable.put(i, whitePawnMoved, new OccupiedTile(i, whitePawnMoved));
            }

            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                King whiteKingFirstMove = new King(alliance, i, true, true);
                King whiteKingMoved = new King(alliance, i, false, false, false, false);
                King whiteKingMovedCastled = new King(alliance, i, false, true, false, false);
                occupiedTileTable.put(i, whiteKingFirstMove, new OccupiedTile(i, whiteKingFirstMove));
                occupiedTileTable.put(i, whiteKingMoved, new OccupiedTile(i, whiteKingMoved));
                occupiedTileTable.put(i, whiteKingMovedCastled, new OccupiedTile(i, whiteKingMovedCastled));
            }
        }

        return occupiedTileTable;
    }

    public static class EmptyTile extends Tile {

        private EmptyTile(int coordinate) {
            super(coordinate);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        public Piece getPiece() {
            return null;
        }

    }

    public static class OccupiedTile extends Tile {

        private Piece pieceOnTile;

        private OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString() {
            return this.pieceOnTile.getPieceAlliance().isWhite() ?
                    this.pieceOnTile.toString() :
                    this.pieceOnTile.toString().toLowerCase();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }

}
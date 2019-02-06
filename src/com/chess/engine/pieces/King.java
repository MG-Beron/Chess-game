package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The king class with two constructors and move coordinates array
 * -9 up left
 * -8 up
 * -7 up right
 * -1 left
 * 1 right
 * 7 down left
 * 8 down
 * 9 down right
 *
 * @see <a href="https://en.wikipedia.org/wiki/King_(chess)">King piece</a>
 */
public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};
    private boolean isCastled;
    private boolean kingSideCastleCapable;
    private boolean queenSideCastleCapable;

    public King(Alliance alliance, int piecePosition, boolean kingSideCastleCapable, boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(Alliance alliance, int piecePosition, boolean isFirstMove, boolean isCastled,
                boolean kingSideCastleCapable, boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            int destinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
                Tile candidateDestinationTile = board.getTile(destinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    Move move = new MajorMove(board, this, destinationCoordinate);
                    legalMoves.add(move);
                } else if (candidateDestinationTile.isTileOccupied()) {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAtDestinationAlliance) {
                        Move attackMove = new MajorAttackMove(board, this, destinationCoordinate, pieceAtDestination);
                        legalMoves.add(attackMove);
                    }
                }
            }
        }

        return legalMoves;
    }

    /**
     * @return "K"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    @Override
    public King movePiece(Move move) {
        return new King(this.pieceAlliance, move.getDestinationCoordinate(), false, move.isCastlingMove(), false,
                false);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof King)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final King king = (King) other;
        return isCastled == king.isCastled;
    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + (isCastled ? 1 : 0);
    }

    /**
     * Checks the case when the king is located in the first column and
     * is trying to move on left up -9 or left -1 or left down 7
     * When the king is in the first column of the board it should not move left
     * In this case the -9, -1 and 7 move numbers are illegal
     * FIRST_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param currentPosition  the current location of the king
     * @param destinationOffset the destination offset of the king
     * @return true if the king move is illegal and false otherwise
     */
    private static boolean isFirstColumnExclusion(int currentPosition, int destinationOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (destinationOffset == -9 || destinationOffset == -1 || destinationOffset == 7);
    }

    /**
     * Checks the case when the king is located in the eighth column and
     * is trying to move on right up -7 or right 1 or right down 9.
     * When the king is in the edge of the board it should not move right
     * In this case the -7, 1, 9 move numbers are illegal
     * EIGHTH_COLUMN is a boolean array that returns true for the eighth column coordinates
     *
     * @param currentPosition   the current location of the king
     * @param destinationOffset the destination offset of the king
     * @return true if the king move is illegal and false otherwise
     */
    private boolean isEighthColumnExclusion(int currentPosition, int destinationOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (destinationOffset == -7 || destinationOffset == 1 || destinationOffset == 9);
    }
}
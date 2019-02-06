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
 * The knight class with two constructors and move coordinates
 * -17 two up left
 * -15 two up right
 * -10 up left
 * -6 up right
 * 6 down left
 * 10 down right
 * 15 two down left
 * 17 two down right
 *
 * @See <a href="https://en.wikipedia.org/wiki/Knight_(chess)">Knight piece</a>
 */
public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(Alliance alliance, int piecePosition) {
        super(PieceType.KNIGHT, alliance, piecePosition, true);
    }

    public Knight(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.KNIGHT, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            int destinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
                Tile destinationTile = board.getTile(destinationCoordinate);
                if (!destinationTile.isTileOccupied()) {
                    Move move = new MajorMove(board, this, destinationCoordinate);
                    legalMoves.add(move);
                } else if (destinationTile.isTileOccupied()){
                    Piece pieceAtDestination = destinationTile.getPiece();
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

    @Override
    public Knight movePiece(Move move) {
        return PieceUtils.getMovedKnight(move);
    }

    /**
     * @return "N"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    /**
     * Checks if the knight is located in the first column and is trying to move
     * -17 two up left or -10 up left or 6 down left or 15 two down left
     * When the knight is in the first column it should not move left
     * in this case -17, -10, 6 or 15 move numbers are illegal
     * FIRST_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param currentPosition the current location of the knight
     * @param candidateOffset the candidate offset move number
     * @return true if the knight move is illegal and false otherwise
     */
    private boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
    }

    /**
     * Checks if the knight is located in the second column and is trying to move
     * -10 up left or 6 down left
     * When the knight is in the second column it should not move two left
     * in this case -10 or 6 move numbers are illegal
     * SECOND_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param currentPosition the current location of the knight
     * @param candidateOffset the candidate offset move number
     * @return true if the knight move is illegal and false otherwise
     */
    private boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] &&
                (candidateOffset == -10 || candidateOffset == 6);
    }

    /**
     * Checks if the knight is located in the seventh column and is trying to move
     * -6 up right or 10 down right
     * When the knight is in the seventh column it should not move two right
     * in this case -6 or 10 move numbers are illegal
     * SEVENTH_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param currentPosition the current location of the knight
     * @param candidateOffset the candidate offset move number
     * @return true if the knight move is illegal and false otherwise
     */
    private boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] &&
                (candidateOffset == -6 || candidateOffset == 10);
    }

    /**
     * Checks if the knight is located in the eighth column and is trying to move
     * -15 two up right or -6 up right or 10 down right or 17 two down right
     * When the knight is in the eighth column it should not move two right
     * in this case -6 or 10 move numbers are illegal
     * EIGHTH_COLUMN is a boolean array that returns true for the eighth column coordinates
     *
     * @param currentPosition the current location of the knight
     * @param candidateOffset the candidate offset move number
     * @return true if the knight move is illegal and false otherwise
     */
    private boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
    }
}
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
 * The bishop class with two constructors and move coordinates array
 * -9 up left
 * -7 up right
 * 7 down left
 * 9 down right
 * Calculating all legal move with calculateLegalMoves method
 *
 * @see <a href="https://en.wikipedia.org/wiki/Bishop_(chess)">Bishop piece</a>
 */
public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    public Bishop(Alliance alliance, int piecePosition) {
        super(PieceType.BISHOP, alliance, piecePosition, true);
    }

    public Bishop(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int destinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
                if (isFirstColumnExclusion(currentCandidateOffset, destinationCoordinate) ||
                        isEighthColumnExclusion(currentCandidateOffset, destinationCoordinate)) {
                    break;
                }
                destinationCoordinate += currentCandidateOffset;
                if (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
                    Tile destinationTile = board.getTile(destinationCoordinate);
                    if (!destinationTile.isTileOccupied()) {
                        Move move = new MajorMove(board, this, destinationCoordinate);
                        legalMoves.add(move);
                    } else if (destinationTile.isTileOccupied()) {
                        Piece pieceAtDestination = destinationTile.getPiece();
                        Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            Move attackMove = new MajorAttackMove(board, this, destinationCoordinate,
                                    pieceAtDestination);
                            legalMoves.add(attackMove);
                        }
                        // stops searching for legal moves when occupied tile is reached
                        break;
                    }
                }
            }
        }

        return legalMoves;
    }

    @Override
    public Bishop movePiece(Move move) {
        return PieceUtils.getMovedBishop(move);
    }

    /**
     * @return "B"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    /**
     * Checks the case when the bishop move is located in the first column and
     * is trying to move on left up -9 or left down 7
     * When the bishop move is in the first column of the board it should not move left
     * In this case the -9 and 7 move numbers are illegal
     * FIRST_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param currentCandidate      the current candidate offset of the bishop
     * @param destinationCoordinate the location of the bishop move
     * @return true if the bishop move is illegal and false otherwise
     */
    private boolean isFirstColumnExclusion(int currentCandidate, int destinationCoordinate) {
        return (BoardUtils.FIRST_COLUMN[destinationCoordinate] &&
                (currentCandidate == -9 || currentCandidate == 7));
    }

    /**
     * Checks the case when the bishop move is located in the eighth column and
     * is trying to move on right up -7 or right down 9.
     * When the bishop move is in the edge of the board it should not move right
     * In this case the -7 and 9 move numbers are illegal
     * EIGHTH_COLUMN is a boolean array that returns true for the eighth column coordinates
     *
     * @param currentCandidate      the current candidate offset of the bishop
     * @param destinationCoordinate the location of the bishop move
     * @return true if the bishop move is illegal and false otherwise
     */
    private boolean isEighthColumnExclusion(int currentCandidate, int destinationCoordinate) {
        return BoardUtils.EIGHTH_COLUMN[destinationCoordinate] &&
                (currentCandidate == -7 || currentCandidate == 9);
    }
}
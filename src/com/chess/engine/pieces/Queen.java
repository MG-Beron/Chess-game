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
 * The queen class which is mixture by bishop and rook class
 *
 * @see <a href="https://en.wikipedia.org/wiki/Queen_(chess)">Queen piece</a>
 */
public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(Alliance alliance, int piecePosition) {
        super(PieceType.QUEEN, alliance, piecePosition, true);
    }

    public Queen(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.QUEEN, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        int candidateDestinationCoordinate;
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            candidateDestinationCoordinate = this.piecePosition;
            while (true) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                        isEightColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    break;
                } else {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        Move move = new MajorMove(board, this, candidateDestinationCoordinate);
                        legalMoves.add(move);
                    } else {
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                            Move move = new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination);
                            legalMoves.add(move);
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Queen movePiece(final Move move) {
        return PieceUtils.getMovedQueen(move);
    }

    /**
     * @return "Q"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    /**
     * Checks the case when the queen move is located in the first column and
     * is trying to move on left up -9, left -1 or left down 7
     * When the queen move is in the first column of the board it should not move left
     * In this case the -9, -1, and 7 move numbers are illegal
     * FIRST_COLUMN is a boolean array that returns true for the first column coordinates
     *
     * @param candidateOffset the candidate offset of the queen
     * @param currentPosition the location of the queen move
     * @return true if the queen move is illegal and false otherwise
     */
    private static boolean isFirstColumnExclusion(int candidateOffset, int currentPosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    /**
     * Checks the case when the queen move is located in the eighth column and
     * is trying to move on right up -7, right 1 or right down 9
     * When the queen move is in the eighth column of the board it should not move right
     * In this case the -7, 1, and 9 move numbers are illegal
     * EIGHTH_COLUMN is a boolean array that returns true for the eighth column coordinates
     *
     * @param candidateOffset the candidate offset of the queen
     * @param currentPosition the location of the queen move
     * @return true if the queen move is illegal and false otherwise
     */
    private static boolean isEightColumnExclusion(int candidateOffset, int currentPosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
                (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

}
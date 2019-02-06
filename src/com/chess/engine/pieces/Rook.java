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
 * -8 up
 * -1 left
 * 1 right
 * 8 down
 * Calculating all legal move with calculateLegalMoves method
 *
 * @see <a href="https://en.wikipedia.org/wiki/Rook_(chess)">Rook piece</a>
 */
public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-8, -1, 1, 8};

    public Rook(Alliance alliance, int piecePosition) {
        super(PieceType.ROOK, alliance, piecePosition, true);
    }

    public Rook(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.ROOK, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (int currentOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isColumnExclusion(currentOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        Move move = new MajorMove(board, this, candidateDestinationCoordinate);
                        legalMoves.add(move);
                    } else {
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAtDestinationAlliance) {
                            Move attackMove = new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination);
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
    public Rook movePiece(Move move) {
        return PieceUtils.getMovedRook(move);
    }

    /**
     * @return "R"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    /**
     * Checks if the rook moves has reached the first or the eighth column
     * and is trying to move left -1 or right 1
     * If so the rook should not be able to move on left or right
     *
     * @param currentCandidate               The current candidate offset
     * @param candidateDestinationCoordinate The location of the candidate move
     * @return True if the rook move is illegal and false otherwise
     */
    private static boolean isColumnExclusion(int currentCandidate, int candidateDestinationCoordinate) {
        return (BoardUtils.FIRST_COLUMN[candidateDestinationCoordinate] && currentCandidate == -1) ||
                (BoardUtils.EIGHTH_COLUMN[candidateDestinationCoordinate] && currentCandidate == 1);
    }

}
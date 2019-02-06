package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The pawn class
 * 8 up move
 * 16 pawn jump
 * 7 left attack move
 * 9 right attack move
 * Checks for enpassant moves
 * @See <a href="https://en.wikipedia.org/wiki/Pawn_(chess)">Pawn piece</a>
 */
public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(Alliance alliance, int piecePosition) {
        super(PieceType.PAWN, alliance, piecePosition, true);
    }

    public Pawn(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.PAWN, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (int candidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int destinationCoordinate =
                    this.piecePosition + (this.pieceAlliance.getDirection() * candidateOffset);
            if (!BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
                continue;
            }
            // One up pawn move or pawn jump move or up left attack move or up right attack move and EnPassant
            if (candidateOffset == 8 && !board.getTile(destinationCoordinate).isTileOccupied()) {
                if (!this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                    Move move = new PawnMove(board, this, destinationCoordinate);
                    legalMoves.add(move);
                } else if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                    Move pawnPromotionMove = new PawnPromotion(new PawnMove(board, this, destinationCoordinate));
                    legalMoves.add(pawnPromotionMove);
                }
            } else if (candidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                            (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(destinationCoordinate).isTileOccupied() &&
                        !board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()) {
                    Move move = new PawnJump(board, this, destinationCoordinate);
                    legalMoves.add(move);
                }
            } else if (candidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(destinationCoordinate).isTileOccupied()) {
                    Piece pieceOnCandidate = board.getTile(destinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (!this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnAttackMove(board, this, destinationCoordinate, pieceOnCandidate);
                            legalMoves.add(move);
                        } else if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnPromotion(new PawnAttackMove(board, this, destinationCoordinate,
                                    pieceOnCandidate));
                            legalMoves.add(move);
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                    Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (!this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnEnPassantAttack(board, this, destinationCoordinate, pieceOnCandidate);
                            legalMoves.add(move);
                        } else if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnPromotion(new PawnEnPassantAttack(board, this, destinationCoordinate,
                                    pieceOnCandidate));
                            legalMoves.add(move);
                        }
                    }
                }
            } else if (candidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(destinationCoordinate).isTileOccupied()) {
                    if (this.pieceAlliance != board.getTile(destinationCoordinate).getPiece().getPieceAlliance()) {
                        if (!this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnAttackMove(board, this, destinationCoordinate, board.getTile
                                    (destinationCoordinate).getPiece());
                            legalMoves.add(move);
                        } else if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnAttackMove(board, this, destinationCoordinate, board.getTile
                                    (destinationCoordinate).getPiece());
                            legalMoves.add(new PawnPromotion(move));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                    Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (!this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnEnPassantAttack(board, this, destinationCoordinate, pieceOnCandidate);
                            legalMoves.add(move);
                        } else if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
                            Move move = new PawnPromotion(new PawnEnPassantAttack(board, this, destinationCoordinate, pieceOnCandidate));
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }

        return legalMoves;
    }

    @Override
    public Pawn movePiece(Move move) {
        return PieceUtils.getMovedPawn(move);
    }

    /**
     * This method determinate what piece to be promoted
     *
     * @return A queen piece
     * @see <a href="https://en.wikipedia.org/wiki/Promotion_(chess)">Pawn promotion</a>
     */
    public Piece getPromotionPiece() {
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }

    /**
     * @return "P"
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }
}
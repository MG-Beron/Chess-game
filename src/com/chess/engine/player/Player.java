package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveStatus;
import com.chess.engine.board.MoveTransition;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected Board board;
    protected King playerKing;
    protected Collection<Move> legalMoves;
    protected boolean isInCheck;

    /**
     * @param board The game board
     * @param playerLegals the current player legal moves
     * @param opponentLegals the opponent player legal moves
     */
    Player(Board board, Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(playerLegals, calculateKingCastles(playerLegals, opponentLegals)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
    }

    /**
     * @param move The move
     * @return True if the move is legal, false otherwise
     */
    public boolean isMoveLegal(Move move) {
        if (move instanceof Move.NullMove) {
            return false;
        }
        return !(move.isCastlingMove() && isInCheck()) && this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    /**
     * Iterates over active pieces and searches for the king
     * @return King piece
     */
    private King establishKing() {
        for(Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! " + this.getAlliance()+ " king could not be established!");
    }

    /**
     * Iterates over the player legal moves and is checking if the move status is done
     * If so this means that the player has escape move
     * @return True if the player has escape moves false otherwise
     */
    private boolean hasEscapeMoves() {
        for(Move move : getLegalMoves()) {
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    /**
     * Iterates over the moves and is looking for attacks on the given tile
     * @param tile The tile
     * @param moves the moves
     * @return Collection of attacks on tile
     */
    static Collection<Move> calculateAttacksOnTile(int tile, Collection<Move> moves) {
        List<Move> attackMoves = new ArrayList<Move>();
        for (Move move : moves) {
            if (tile == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }

        return attackMoves;
    }

    /**
     * Makes move by creating MoveTransition class and MoveStatus
     * ILLEGAL_MOVE, LEAVES_PLAYER_IN_CHESS or DONE
     * @param move The move
     * @return MoveTransition class
     */
    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        Board transitionedBoard = move.execute();
        Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
                transitionedBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionedBoard.currentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

}
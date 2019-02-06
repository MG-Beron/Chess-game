package com.chess.engine.board;

/**
 * The move transition class
 */
public class MoveTransition {

    private final Board fromBoard;
    private final Board toBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board fromBoard, Board toBoard, Move move, MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public Board getToBoard() {
        return this.toBoard;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
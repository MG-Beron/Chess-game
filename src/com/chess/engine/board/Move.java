package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;
import com.chess.engine.pieces.Rook;

/**
 * The move class holds static move classes and move factory
 * MajorMove extends Move
 * AttackMove extends Move
 * PawnMove extends Move
 * PawnJump extends Move
 * NullMove extends Move
 * CastleMove extends Move
 * KingSideCastleMove extends CastleMove
 * QueenSideCastleMove extends CastleMove
 * PawnPromotion extends PawnMove
 * PawnAttackMove extends AttackMove
 * PawnEnPassantAttack extends PawnAttackMove
 * MajorAttackMove extends MajorMove
 *
 * MoveFactory
 */
public abstract class Move {

    protected Board board;
    protected int destinationCoordinate;
    protected Piece movedPiece;
    protected boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(Board board, Piece pieceMoved, int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = pieceMoved;
        this.isFirstMove = pieceMoved.isFirstMove();
    }

    private Move(Board board, int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public Board getBoard() {
        return this.board;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    /**
     * Execute method for executing the moves
     *
     * @return The built board
     */
    public Board execute() {
        Board.Builder builder = new Builder();
        for (Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        builder.setMoveTransition(this);
        return builder.build();
    }

    /**
     * @return Used for printing the moves on the Game History Panel
     */
    public String disambiguationFile() {
        for (Move move : this.board.currentPlayer().getLegalMoves()) {
            if (move.getDestinationCoordinate() == this.destinationCoordinate && !this.equals(move) &&
                    this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) {
                return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }

    public static class PawnPromotion extends PawnMove {

        Move decoratedMove;
        Pawn promotedPawn;

        public PawnPromotion(Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

        @Override
        public Board execute() {
            Board pawnMovedBoard = this.decoratedMove.execute();
            Board.Builder builder = new Builder();
            for (Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()) + "-" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate) + "=" + PieceType.QUEEN;
        }

    }

    public static class MajorMove extends Move {

        public MajorMove(Board board, Piece pieceMoved, int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        /**
         * @return The move print e.g e4, d2
         */
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + disambiguationFile() +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class MajorAttackMove extends AttackMove {

        public MajorAttackMove(Board board, Piece pieceMoved, int destinationCoordinate, Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);

        }

        /**
         * @return The move print e.g. Qxd5
         */
        @Override
        public String toString() {
            return movedPiece.getPieceType() + disambiguationFile() + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnMove extends Move {

        public PawnMove(Board board, Piece pieceMoved, int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece pieceMoved, int destinationCoordinate, Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnEnPassantAttack extends PawnAttackMove {

        public PawnEnPassantAttack(Board board, Piece pieceMoved, int destinationCoordinate, Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnEnPassantAttack && super.equals(other);
        }

        @Override
        public Board execute() {
            Board.Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }
    }

    public static class PawnJump extends Move {

        public PawnJump(Board board, Pawn pieceMoved, int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }

        @Override
        public Board execute() {
            Board.Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    static abstract class CastleMove extends Move {

        protected Rook castleRook;
        protected int castleRookStart;
        protected int castleRookDestination;

        CastleMove(Board board, Piece pieceMoved, int destinationCoordinate, Rook castleRook, int castleRookStart,
                   int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            Board.Builder builder = new Builder();
            for (Piece piece : this.board.getAllPieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination, false));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }

    }

    public static class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece pieceMoved, int destinationCoordinate, Rook castleRook, int
                castleRookStart, int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove
                    .getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(Board board, Piece pieceMoved, int destinationCoordinate, Rook castleRook, int
                castleRookStart, int rookCastleDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart, rookCastleDestination);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove
                    .getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

    }

    public static abstract class AttackMove extends Move {

        private Piece attackedPiece;

        AttackMove(Board board, Piece pieceMoved, int destinationCoordinate, Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate);
            this.attackedPiece = pieceAttacked;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals( Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }
    }

    public static class NullMove extends Move {

        private NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate) {
            for (Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
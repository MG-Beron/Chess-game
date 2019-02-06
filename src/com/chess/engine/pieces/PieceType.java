package com.chess.engine.pieces;

/*
Piece types can be
PAWN with value 100 and name P
KNIGHT with value 320 and name N
BISHOP with value 330 and name B
ROOK with value 500 and name R
QUEEN with value 900 and name Q
KING with value 10000 and name K
The value of pieces is used to sort them in the TakenPiecePanel
The isRook and isKing methods are used for castling
toString method is used to print the pieces on the console
 */
public enum PieceType {

    PAWN(100, "P") {
        @Override
        public boolean isRook() {
            return false;
        }

        @Override
        public boolean isKing() {
            return false;
        }
    },
    KNIGHT(320, "N") {
        @Override
        public boolean isRook() {
            return false;
        }

        @Override
        public boolean isKing() {
            return false;
        }
    },
    BISHOP(330, "B") {
        @Override
        public boolean isRook() {
            return false;
        }

        @Override
        public boolean isKing() {
            return false;
        }
    },
    ROOK(500, "R") {
        @Override
        public boolean isRook() {
            return true;
        }

        @Override
        public boolean isKing() {
            return false;
        }
    },
    QUEEN(900, "Q") {
        @Override
        public boolean isRook() {
            return false;
        }

        @Override
        public boolean isKing() {
            return false;
        }
    },
    KING(10000, "K") {
        @Override
        public boolean isRook() {
            return false;
        }

        @Override
        public boolean isKing() {
            return true;
        }
    };

    private int value;
    private String pieceName;

    public int getPieceValue() {
        return this.value;
    }

    PieceType(int value, String pieceName) {
        this.value = value;
        this.pieceName = pieceName;
    }

    public abstract boolean isRook();

    public abstract boolean isKing();

    @Override
    public String toString() {
        return this.pieceName;
    }
}

package com.chess.engine;

import com.chess.engine.board.Board;

public class Main {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        
        System.out.println(board);
    }
}
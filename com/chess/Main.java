package com.chess;

import com.chess.engine.classic.board.Board;
import com.chess.gui.Table;

public class Main {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
        
        Table table = new Table();
    }
}
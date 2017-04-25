package com.chess.engine.classic.board;

import com.chess.gui.Table.TilePanel;
import com.google.common.collect.Lists;

import java.util.List;

/*
Board direction enum can be NORMAL or FLIPPED
NORMAL traverse the boardTiles in normal way and returns FLIPPED as opposite
FLIPPED traverse the boardTiles in reversed way and returns NORMAL as opposite
 */
public enum BoardDirection {
    NORMAL {
        @Override
        public List<TilePanel> traverse(List<TilePanel> boardTiles) {
            return boardTiles;
        }

        @Override
        public BoardDirection opposite() {
            return FLIPPED;
        }

    },
    FLIPPED {
        @Override
        public List<TilePanel> traverse(List<TilePanel> boardTiles) {
            return Lists.reverse(boardTiles);
        }

        @Override
        public BoardDirection opposite() {
            return NORMAL;
        }
    };

    public abstract List<TilePanel> traverse(List<TilePanel> boardTiles);

    public abstract BoardDirection opposite();
}
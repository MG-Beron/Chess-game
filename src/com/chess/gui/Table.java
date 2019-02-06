package com.chess.gui;

import com.chess.engine.board.*;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The table GUI class
 */
public class Table {

    private static final String PIECES_PATH = "src/pieces/fancy/";
    private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");

    private GameHistoryPanel gameHistoryPanel;
    private TakenPiecesPanel takenPiecesPanel;
    private BoardPanel boardPanel;

    private Board chessBoard;
    private MoveLog moveLog;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    public Table() {
        JFrame gameFrame = new JFrame("The Chess game");
        gameFrame.setLayout(new BorderLayout());

        JMenuBar tableMenuBar = createTableMenuBar();
        gameFrame.setJMenuBar(tableMenuBar);

        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;

        gameFrame.setSize(600, 600);
        gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    /**
     * Create the file menu on the top bar
     *
     * @return JMenu menu
     */
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    /**
     * Create preferences menu on the top bar
     *
     * @return JMenu menu
     */
    private JMenu createPreferencesMenu() {
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();
        JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });

        preferencesMenu.add(legalMoveHighlighterCheckBox);

        return preferencesMenu;
    }

    private class BoardPanel extends JPanel {

        List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<TilePanel>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(new Dimension(400, 350));
            validate();
        }

        /**
         * Drawing the board
         *
         * @param board The board
         */
        private void drawBoard(Board board) {
            removeAll();
            for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    /**
     * The Move Log class used for GameHistoryPanel
     */
    static class MoveLog {

        private List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<Move>();
        }

        List<Move> getMoves() {
            return this.moves;
        }

        void addMove(Move move) {
            this.moves.add(move);
        }

        int size() {
            return this.moves.size();
        }
    }

    /**
     * The Tile Panel hooked with the MouseListener
     */
    public class TilePanel extends JPanel {

        private int tileId;

        TilePanel(BoardPanel boardPanel, int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(new Dimension(10, 10));
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            if (sourceTile == null) {
                                sourceTile = chessBoard.getTile(tileId);
                                humanMovedPiece = sourceTile.getPiece();
                                if (humanMovedPiece == null) {
                                    sourceTile = null;
                                }
                            } else {
                                destinationTile = chessBoard.getTile(tileId);
                                Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
                                        destinationTile.getTileCoordinate());
                                MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                if (transition.getMoveStatus().isDone()) {
                                    chessBoard = transition.getToBoard();
                                    moveLog.addMove(move);
                                    System.out.println(chessBoard);
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;
                            }
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // ignored
                }
            });
            validate();
        }

        /**
         * Drawing the tiles
         *
         * @param board The board
         */
        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    //System.out.println(getPath(board));
                    BufferedImage image = ImageIO.read(new File(getPath(board)));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * Highlighting the legal moves
         *
         * @param board The board
         */
        private void highlightLegals(Board board) {
            if (highlightLegalMoves) {
                for (Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("src/misc/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * Gets the legal moves for each piece
         *
         * @param board The board
         * @return Iterable of move
         */
        private Iterable<Move> pieceLegalMoves(Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }

            return Collections.emptyList();
        }

        /**
         * Assigning the tile chess color
         */
        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId] ||
                    BoardUtils.EIGHTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }

        private String getPath(Board board) {
            return PIECES_PATH
                    + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1)
                    + board.getTile(this.tileId).getPiece().toString() + ".gif";
        }
    }
}

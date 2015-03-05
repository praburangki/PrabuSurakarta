/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.gui;

import ch06.logic.Game;
import ch06.logic.Move;
import ch06.logic.MoveValidator;
import ch06.logic.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Prabu Rangki
 */
public class Gui extends JPanel {

    private static final int BOARD_START_X = 369;
    private static final int BOARD_START_Y = 119;

    private static final int SQUARE_WIDTH = 44;
    private static final int SQUARE_HEIGHT = 44;

    private static final int PIECE_WIDTH = 25;
    private static final int PIECE_HEIGHT = 25;

    private static final int PIECES_START_X = BOARD_START_X + (int) (SQUARE_WIDTH / 2.0 - PIECE_WIDTH / 2.0);
    private static final int PIECES_START_Y = BOARD_START_Y + (int) (SQUARE_HEIGHT / 2.0 - PIECE_HEIGHT / 2.0);

    private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int) (PIECE_WIDTH / 2.0);
    private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int) (PIECE_HEIGHT / 2.0);

    private Image imgBackground;
    private JLabel lblGameState;

    private Game game;
    private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();

    private GuiPiece dragPiece;

    private Move lastMove;

    public Gui() {
        setLayout(null);

        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        imgBackground = new ImageIcon(urlBackgroundImg).getImage();

        game = new Game();

        for (Piece piece : game.getPieces()) {
            createAndAddGuiPiece(piece);
        }

        PieceDragAndDropListener listener = new PieceDragAndDropListener(guiPieces, this);
        addMouseListener(listener);
        addMouseMotionListener(listener);

        String labelTxt = getGameStateAsText();
        lblGameState = new JLabel(labelTxt);
        lblGameState.setBounds(0, 30, 80, 30);
        lblGameState.setForeground(Color.white);
        this.add(lblGameState);

        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setResizable(false);
        f.setSize(imgBackground.getWidth(null), imgBackground.getHeight(null));
    }

    private String getGameStateAsText() {
        String state = "unknown";
        switch (game.getGameState()) {
            case Game.GAME_STATE_BLACK:
                state = "black";
                break;
            case Game.GAME_STATE_END:
                state = "end";
                break;
            case Game.GAME_STATE_WHITE:
                state = "white";
                break;
        }
        return state;
    }

    private void createAndAddGuiPiece(Piece piece) {
        Image img = getImageForPiece(piece.getColor());
        GuiPiece guiPiece = new GuiPiece(img, piece);
        guiPieces.add(guiPiece);
    }

    private Image getImageForPiece(int color) {
        String filename = "";
        filename += (color == Piece.COLOR_WHITE ? "w" : "b");

        filename += ".png";

        URL urlPieceImg = getClass().getResource("/img/" + filename);

        return new ImageIcon(urlPieceImg).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(imgBackground, 0, 0, null);
        for (GuiPiece guiPiece : guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImg(), guiPiece.getX(), guiPiece.getY(), null);
            }
        }

        // draw last move, if user is not dragging game piece
        if (!isUserDraggingPiece() && lastMove != null) {
            int highlightSourceX = convertColumnToX(lastMove.sourceColumn);
            int highlightSourceY = convertRowToY(lastMove.sourceRow);
            int highlightTargetX = convertColumnToX(lastMove.targetColumn);
            int highlightTargetY = convertRowToY(lastMove.targetRow);

            g.setColor(Color.BLACK);
            g.drawOval(highlightSourceX + 1, highlightSourceY + 1, 25, 25);
            g.drawOval(highlightTargetX + 1, highlightTargetY + 1, 25, 25);

            g.setColor(Color.RED);
            g.drawOval(highlightSourceX, highlightSourceY, 25, 25);
            g.drawOval(highlightTargetX, highlightTargetY, 25, 25);
            //g.drawLine(highlightSourceX + SQUARE_WIDTH / 2, highlightSourceY + SQUARE_HEIGHT / 2, 
            //      highlightTargetX + SQUARE_WIDTH / 2, highlightTargetY + SQUARE_HEIGHT / 2);
        }

        if (isUserDraggingPiece()) {

            MoveValidator moveValidator = game.getMoveValidator();
            // iterate the complete board to check if target locations are valid
            for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_F; column++) {
                for (int row = Piece.ROW_1; row <= Piece.ROW_6; row++) {
                    int sourceRow = dragPiece.getPiece().getRow();
                    int sourceColumn = dragPiece.getPiece().getColumn();

                    // check if target location is valid
                    if (moveValidator.isMoveValid(new Move(sourceRow, sourceColumn, row, column))) {

                        int highlightX = convertColumnToX(column);
                        int highlightY = convertRowToY(row);

                        // draw a black drop shadow by drawing a black rectangle with an offset of 1 pixel
                        g.setColor(Color.BLACK);
                        g.drawOval(highlightX + 1, highlightY + 1, 25, 25);
                        // draw the highlight
                        g.setColor(Color.GREEN);
                        g.drawOval(highlightX, highlightY, 25, 25);
                    }
                }
            }
        }

        lblGameState.setText(this.getGameStateAsText());
    }

    /**
     * @return true if user is currently dragging a game piece
     */
    private boolean isUserDraggingPiece() {
        return dragPiece != null;
    }

    public static void main(String[] args) {
        new Gui();
    }

    public int getGameState() {
        return game.getGameState();
    }

    /**
     * convert logical column into x coordinate
     *
     * @param column
     * @return x coordinate for column
     */
    public static int convertColumnToX(int column) {
        return PIECES_START_X + SQUARE_WIDTH * column;
    }

    /**
     * convert logical row into y coordinate
     *
     * @param row
     * @return y coordinate for row
     */
    public static int convertRowToY(int row) {
        return PIECES_START_Y + SQUARE_HEIGHT * row;
    }

    /**
     * convert x coordinate into logical column
     *
     * @param x
     * @return logical column for x coordinate
     */
    public static int convertXToColumn(int x) {
        return (x - DRAG_TARGET_SQUARE_START_X) / SQUARE_WIDTH;
    }

    /**
     * convert y coordinate into logical row
     *
     * @param y
     * @return logical row for y coordinate
     */
    public static int convertYToRow(int y) {
        return (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
    }

    /**
     * change location of given piece, if the location is valid. If the location
     * is not valid, move the piece back to its original position.
     *
     * @param dragPiece
     * @param x
     * @param y
     */
    public void setNewPieceLocation(GuiPiece dragPiece, int x, int y) {
        int targetRow = Gui.convertYToRow(y);
        int targetColumn = Gui.convertXToColumn(x);

        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_6 || targetColumn < Piece.COLUMN_A
                || targetColumn > Piece.COLUMN_F) {
            dragPiece.resetToUnderlyingPiecePosition();
        } else {
            System.out.println("moving piece to " + targetRow + "/" + targetColumn);
            Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow, targetColumn);
            boolean wasMoveSuccessfull = game.movePiece(move);

            if (wasMoveSuccessfull) {
                lastMove = move;
            }
            dragPiece.resetToUnderlyingPiecePosition();
        }
    }

    /**
     * @param guiPiece - set the gui piece that the user is current dragging
     */
    public void setDragPiece(GuiPiece guiPiece) {
        this.dragPiece = guiPiece;
    }

    /**
     * @return the gui piece that the user is currently dragging
     */
    public GuiPiece getDragPiece() {
        return this.dragPiece;
    }
}

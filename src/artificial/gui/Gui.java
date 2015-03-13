/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.gui;

import artificial.logic.Game;
import artificial.logic.IPlayerHandler;
import artificial.logic.Move;
import artificial.logic.MoveValidator;
import artificial.logic.Piece;
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
 * @author praburangki
 */
public class Gui extends JPanel implements IPlayerHandler {

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
    private List<GuiPiece> guiPieces = new ArrayList<>();

    private GuiPiece dragPiece;
    private Move lastMove;
    private Move currentMove;

    private boolean draggingGamePiecesEnabled;

    public Gui(Game game) {
        this.setLayout(null);

        // background
        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();

        this.game = game;

        for (Piece piece : this.game.getPieces()) {
            createAndAddGuiPiece(piece);
        }

        // add listeners to enable drag and drop
        //
        PiecesDragAndDropListener listener = new PiecesDragAndDropListener(this.guiPieces,
                this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);

        // label to display game state
        String labelText = this.getGameStateAsText();
        this.lblGameState = new JLabel(labelText);
        lblGameState.setBounds(0, 30, 80, 30);
        lblGameState.setForeground(Color.WHITE);
        this.add(lblGameState);

        // create application frame and set visible
        //
        JFrame f = new JFrame();
        f.setSize(80, 80);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setSize(imgBackground.getWidth(null), imgBackground.getHeight(null));
    }

    /**
     * @return textual description of current game state
     */
    private String getGameStateAsText() {
        String state = "unknown";
        switch (this.game.getGameState()) {
            case Game.GAME_STATE_BLACK:
                state = "black";
                break;
            case Game.GAME_STATE_END_WHITE_WON:
                state = "white won";
                break;
            case Game.GAME_STATE_END_BLACK_WON:
                state = "black won";
                break;
            case Game.GAME_STATE_WHITE:
                state = "white";
                break;
        }
        return state;
    }

    /**
     * create a game piece
     *
     * @param color color constant
     * @param x x position of upper left corner
     * @param y y position of upper left corner
     */
    private void createAndAddGuiPiece(Piece piece) {
        Image img = this.getImageForPiece(piece.getColor());
        GuiPiece guiPiece = new GuiPiece(img, piece);
        this.guiPieces.add(guiPiece);
    }

    /**
     * load image for given color. This method translates the color information
     * into a filename and loads that particular file.
     *
     * @param color color constant
     * @return image
     */
    private Image getImageForPiece(int color) {
        String filename = "";
        filename += (color == Piece.COLOR_WHITE ? "w" : "b");

        filename += ".png";

        URL urlPieceImg = getClass().getResource("/img/" + filename);

        return new ImageIcon(urlPieceImg).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {

        // draw background
        g.drawImage(this.imgBackground, 0, 0, null);

        // draw pieces
        for (GuiPiece guiPiece : this.guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImg(), guiPiece.getX(), guiPiece.getY(), null);
            }
        }

        // draw last move, if user is not dragging game piece
        if (!isUserDraggingPiece() && this.lastMove != null) {
            int highlightSourceX = convertColumnToX(this.lastMove.sourceColumn);
            int highlightSourceY = convertRowToY(this.lastMove.sourceRow);
            int highlightTargetX = convertColumnToX(this.lastMove.targetColumn);
            int highlightTargetY = convertRowToY(this.lastMove.targetRow);

            g.setColor(Color.YELLOW);
            g.drawRoundRect(highlightSourceX + 4, highlightSourceY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
            g.drawRoundRect(highlightTargetX + 4, highlightTargetY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
            //g.drawLine(highlightSourceX+SQUARE_WIDTH/2, highlightSourceY+SQUARE_HEIGHT/2
            //		, highlightTargetX+SQUARE_WIDTH/2, highlightTargetY+SQUARE_HEIGHT/2);
        }

        // draw valid target locations, if user is dragging a game piece
        if (isUserDraggingPiece()) {

            MoveValidator moveValidator = this.game.getMoveValidator();

            // iterate the complete board to check if target locations are valid
            for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_F; column++) {
                for (int row = Piece.ROW_1; row <= Piece.ROW_6; row++) {
                    int sourceRow = this.dragPiece.getPiece().getRow();
                    int sourceColumn = this.dragPiece.getPiece().getColumn();

                    // check if target location is valid
                    if (moveValidator.isMoveValid(new Move(sourceRow, sourceColumn, row, column), false)) {

                        int highlightX = convertColumnToX(column);
                        int highlightY = convertRowToY(row);

                        // draw a black drop shadow by drawing a black rectangle with an offset of 1 pixel
                        g.setColor(Color.BLACK);
                        g.drawRoundRect(highlightX + 5, highlightY + 5, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
                        // draw the highlight
                        g.setColor(Color.GREEN);
                        g.drawRoundRect(highlightX + 4, highlightY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
                    }
                }
            }
        }

        // draw game state label
        this.lblGameState.setText(this.getGameStateAsText());
    }

    /**
     * check if the user is currently dragging a game piece
     *
     * @return true - if the user is currently dragging a game piece
     */
    private boolean isUserDraggingPiece() {
        return this.dragPiece != null;
    }

    /**
     * @return current game state
     */
    public int getGameState() {
        return this.game.getGameState();
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
        return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_6 - row);
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
        return Piece.ROW_6 - (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
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

        Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow, targetColumn);
        if (this.game.getMoveValidator().isMoveValid(move, true)) {
            this.currentMove = move;
        } else {
            dragPiece.resetToUnderlyingPiecePosition();
        }
    }

    /**
     * set the game piece that is currently dragged by the user
     *
     * @param guiPiece
     */
    public void setDragPiece(GuiPiece guiPiece) {
        this.dragPiece = guiPiece;
    }

    /**
     * return the gui piece that the user is currently dragging
     *
     * @return the gui piece that the user is currently dragging
     */
    public GuiPiece getDragPiece() {
        return this.dragPiece;
    }

    @Override
    public Move getMove() {
        this.draggingGamePiecesEnabled = true;
        Move moveForExecution = this.currentMove;
        this.currentMove = null;
        return moveForExecution;
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        // adjust GUI piece
        GuiPiece guiPiece = this.getGuiPieceAt(move.targetRow, move.targetColumn);
        if (guiPiece == null) {
            throw new IllegalStateException("no guiPiece at " + move.targetRow + "/" + move.targetColumn);
        }
        guiPiece.resetToUnderlyingPiecePosition();

        // remember last move
        this.lastMove = move;

        // disable dragging until asked by ChessGame for the next move
        this.draggingGamePiecesEnabled = false;

        // repaint the new state
        this.repaint();

    }

    /**
     * @return true - if the user is allowed to drag game pieces
     */
    public boolean isDraggingGamePiecesEnabled() {
        return draggingGamePiecesEnabled;
    }

    /**
     * get non-captured the gui piece at the specified position
     *
     * @param row
     * @param column
     * @return the gui piece at the specified position, null if there is no
     * piece
     */
    private GuiPiece getGuiPieceAt(int row, int column) {
        for (GuiPiece guiPiece : this.guiPieces) {
            if (guiPiece.getPiece().getRow() == row
                    && guiPiece.getPiece().getColumn() == column
                    && guiPiece.isCaptured() == false) {
                return guiPiece;
            }
        }
        return null;
    }
}

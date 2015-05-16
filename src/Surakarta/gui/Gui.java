package Surakarta.gui;

import Surakarta.logic.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import javax.swing.*;

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
    private JLabel labelGameState;
    private Game game;
    private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();
    private GuiPiece dragPiece;
    private Move lastMove;
    private Move currentMove;
    private boolean draggingGamePiecesEnabled;
    
    public Gui(Game game) {
        setLayout(null);

        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        imgBackground = new ImageIcon(urlBackgroundImg).getImage();

        this.game = game;

        for (Piece piece : game.getPieces()) {
            createAndAddGuiPiece(piece);
        }

        PieceDragAndDropListener listener = new PieceDragAndDropListener(guiPieces, this);
        addMouseListener(listener);
        addMouseMotionListener(listener);

        String labelText = getGameStateAsText();
        labelGameState = new JLabel(labelText);
        labelGameState.setBounds(0, 30, 80, 30);
        labelGameState.setForeground(Color.WHITE);
        this.add(labelGameState);

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
            case Game.GAME_STATE_END_WHITE_WON:
                state = "white won!";
                break;
            case Game.GAME_STATE_END_BLACK_WON:
                state = "black won!";
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
        String fileName = "";
        fileName += (color == Piece.COLOR_WHITE ? "w" : "b");
        fileName += ".png";

        URL urlPieceImg = getClass().getResource("/img/" + fileName);

        return new ImageIcon(urlPieceImg).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(imgBackground, 0, 0, null);
        g.setFont(new Font("Arial", Font.BOLD, 20));
//        int x1 = 360, y1 = 145;
//        for (int i = 0; i < 6; i++) {
//            g.drawString(i + "", x1, y1);
//            y1 += 45;
//        }
//        
//        x1 = 382;
//        y1 = 120;
//        for (int i = 0; i < 6; i++) {
//            g.drawString(i + "", x1, y1);
//            x1 += 45;
//        }

        for (GuiPiece guiPiece : guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImg(), guiPiece.getX(), guiPiece.getY(), null);
            }
        }
        
        if (!isUserDraggingPiece() && lastMove != null) {
            int highlightSourceX = convertColumnToX(lastMove.sourceColumn);
            int highlightSourceY = convertRowToY(lastMove.sourceRow);
            int highlightTargetX = convertColumnToX(lastMove.targetColumn);
            int highlightTargetY = convertRowToY(lastMove.targetRow);

            g.setColor(Color.BLACK);
            g.drawOval(highlightSourceX + 1, highlightSourceY + 1, 25, 25);
            g.drawOval(highlightTargetX + 1, highlightTargetY + 1, 25, 25);
            g.drawString("Move executed : \n(" + lastMove.sourceRow + ", " + lastMove.sourceColumn + ")->(" 
                    + lastMove.targetRow + ", " + lastMove.targetColumn + ")", 0, 150);

            g.setColor(Color.RED);
            g.drawOval(highlightSourceX, highlightSourceY, 25, 25);
            g.drawOval(highlightTargetX, highlightTargetY, 25, 25);
        }

        if (isUserDraggingPiece()) {
            MoveValidator moveValidator = game.getMoveValidator();
            int sourceRow = dragPiece.getPiece().getRow();
            int sourceColumn = dragPiece.getPiece().getColumn();
            Vector v = moveValidator.getLegalMoves(sourceRow, sourceColumn);
            Enumeration e = v.elements();
            
            if(e.hasMoreElements()) {
                do {
                    Move move = (Move) e.nextElement();
                    int highlightX = convertColumnToX(move.targetColumn);
                    int highlightY = convertRowToY(move.targetRow);
                    
                    g.setColor(Color.BLACK);
                    g.drawOval(highlightX + 1, highlightY + 1, 25, 25);
                    g.setColor(Color.GREEN);
                    g.drawOval(highlightX, highlightY, 25, 25);
                } while (e.hasMoreElements());
            }
        }

        labelGameState.setText(this.getGameStateAsText());
    }

    private boolean isUserDraggingPiece() {
        return dragPiece != null;
    }

    public int getGameState() {
        return game.getGameState();
    }

    public static int convertColumnToX(int column) {
        return PIECES_START_X + SQUARE_WIDTH * column;
    }

    public static int convertRowToY(int row) {
        return PIECES_START_Y + SQUARE_HEIGHT * row;
    }

    public static int convertXToColumn(int x) {
        return (x - DRAG_TARGET_SQUARE_START_X) / SQUARE_WIDTH;
    }

    public static int convertYToRow(int y) {
        return (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
    }
    
    public void setNewPieceLocation(GuiPiece dragPiece, int x, int y) {
        int targetRow = Gui.convertYToRow(y);
        int targetColumn = Gui.convertXToColumn(x);
        
        Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), 
                targetRow, targetColumn);
        if(this.game.getMoveValidator().isMoveValid(move)) {
            this.currentMove = move;
        } else {
            dragPiece.resetToUnderlyingPiecePosition();
        }        
    }
    
    public void setDragPiece(GuiPiece dragPiece) {
        this.dragPiece = dragPiece;
    }
    
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
        GuiPiece guiPiece = this.getGuiPieceAt(move.targetRow, move.targetColumn);
        if (guiPiece == null) {
            throw new IllegalStateException("no guiPiece at " + move.targetRow + "/" + move.targetColumn);
        }
        guiPiece.resetToUnderlyingPiecePosition();

        this.lastMove = move;

        this.draggingGamePiecesEnabled = false;

        this.repaint();
    }

    public boolean isDraggingGamePiecesEnabled() {
        return draggingGamePiecesEnabled;
    }
    
    private GuiPiece getGuiPieceAt(int row, int column) {
        for (GuiPiece guiPiece : this.guiPieces) {
            if (guiPiece.getPiece().getRow() == row
                    && guiPiece.getPiece().getColumn() == column
                    && !guiPiece.isCaptured()) {
                return guiPiece;
            }
        }

        return null;
    }
}

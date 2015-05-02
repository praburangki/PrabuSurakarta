package Surakarta.gui;

import Surakarta.logic.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author praburangki
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
    private JLabel labelGameState;

    private Game game;
    private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();
    private GuiPiece dragPiece;					
    private Move lastMove;
    
    public Gui() {
        this.setLayout(null);

        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();

        this.game = new Game();

        for (Piece piece : this.game.getPieces()) {
            createAndAddGuiPiece(piece);
        }

        PieceDragAndDropListener listener = new PieceDragAndDropListener(this.guiPieces, this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);

        String labelText = this.getGameStateAsText();
        this.labelGameState = new JLabel(labelText);
        labelGameState.setBounds(0, 30, 80, 30);
        labelGameState.setForeground(Color.WHITE);
        this.add(labelGameState);

        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setResizable(false);
        f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
        
        System.out.println("Board white");
        for (int i = 0; i < 6; i++) {
            System.out.println(Arrays.toString(game.whites[i]));
        }
        
        System.out.println("\nBoard black");
        for (int i = 0; i < 6; i++) {
            System.out.println(Arrays.toString(game.blacks[i]));
        }
    }

    private String getGameStateAsText() {
        String state = "";
        switch(game.getGameState()) {
            case Game.GAME_STATE_BLACK : state = "black"; break;
            case Game.GAME_STATE_END : state = "end"; break;
            case Game.GAME_STATE_WHITE : state = "white"; break;
        }
        
        return state;
    }

    private void createAndAddGuiPiece(Piece piece) {
        Image img = this.getImageForPiece(piece.getColor());
        GuiPiece guiPiece = new GuiPiece(img, piece);
        this.guiPieces.add(guiPiece);
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
        g.drawImage(this.imgBackground, 0, 0, null);

        for (GuiPiece guiPiece : this.guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImg(), guiPiece.getX(), guiPiece.getY(), null);
            }
        }
        
        this.labelGameState.setText(this.getGameStateAsText());
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
        return this.game.getGameState();
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
        
        if(targetRow < Piece.ROW_1 || targetRow > Piece.ROW_6
                || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_F) {
            dragPiece.resetToUnderlyingPiecePosition();
        } else {
            System.out.println("Moving piece to " + targetRow + "/" + targetColumn);
            Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow, targetColumn);
            boolean wasMoveSuccessfull = game.movePiece(move);
            
            if(wasMoveSuccessfull) lastMove = move;
            
            dragPiece.resetToUnderlyingPiecePosition();
        }
        
        System.out.println("Board white");
        for (int i = 0; i < 6; i++) {
            System.out.println(Arrays.toString(game.whites[i]));
        }
        
        System.out.println("\nBoard black");
        for (int i = 0; i < 6; i++) {
            System.out.println(Arrays.toString(game.blacks[i]));
        }
    }

    public GuiPiece getDragPiece() {
        return dragPiece;
    }

    public void setDragPiece(GuiPiece dragPiece) {
        this.dragPiece = dragPiece;
    }
    
}

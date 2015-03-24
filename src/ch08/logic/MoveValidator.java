/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.logic;

import java.util.Arrays;

/**
 *
 * @author praburangki
 */
public class MoveValidator {
    private Game game;
    private Piece sourcePiece, targetPiece;
    private boolean debug;
    private Map map;

    public MoveValidator(Game game, Map map) {
        this.game = game;
        this.map = map;
    }
    
    public boolean isMoveValid(Move move, boolean debug) {
        int sourceRow = move.sourceRow;
        int sourceColumn = move.sourceColumn;
        int targetRow = move.targetRow;
        int targetColumn = move.targetColumn;
        
        sourcePiece = this.game.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        targetPiece = this.game.getNonCapturedPieceAtLocation(targetRow, targetColumn);
    
        return isValidMove(sourceRow, sourceColumn, targetRow, targetColumn, debug);
    }
    
    private boolean isValidMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn, boolean debug) {
        boolean isValid;
        int color_temp = sourcePiece.getColor();
        isValid = getUnAttackMoves(map, sourceRow, sourceColumn, debug) || 
                getAttackMoves(map, sourceRow, sourceColumn, color_temp);
        
        return isValid;
    }
    
    public static final int[] outx = {1, 1, 1, 1, 1, 1, 0, 1, 2, 3, 4, 5, 4, 4, 4, 4, 4, 4, 5, 4, 3, 2, 1, 0};
    public static final int[] outy = {0, 1, 2, 3, 4, 5, 4, 4, 4, 4, 4, 4, 5, 4, 3, 2, 1, 0, 1, 1, 1, 1, 1, 1};
    public static final int[] inx = {2, 2, 2, 2, 2, 2, 0, 1, 2, 3, 4, 5, 3, 3, 3, 3, 3, 3, 5, 4, 3, 2, 1, 0};
    public static final int[] iny = {0, 1, 2, 3, 4, 5, 3, 3, 3, 3, 3, 3, 5, 4, 3, 2, 1, 0, 2, 2, 2, 2, 2, 2};
    public static final int LINE_LEN = 24;

    public final int[] stepx = {-1, -1, -1, 0, 0, 1, 1, 1};
    public final int[] stepy = {-1, 0, 1, -1, 1, -1, 0, 1};

    private boolean getAttackMoves(Map board, int x, int y, int color) {
        int i, p;
        boolean[] vis = new boolean[LINE_LEN];

        Arrays.fill(vis, false);
        for (i = 0; i < LINE_LEN; i++) {
            if (x == outx[i] && y == outy[i]) {
                p = getOutTarget(board, i, 1, color);
                if (p >= 0 && !vis[p]) {
//                    stack.push(new Move(x, y, outx[p], outy[p]));
                    vis[p] = true;
                    return true;
                }
                p = getOutTarget(board, i, -1, color);
                if (p >= 0 && !vis[p]) {
//                    stack.push(new Move(x, y, outx[p], outy[p]));
                    vis[p] = true;
                    return true;
                }
            }
            if (x == inx[i] && y == iny[i]) {
                p = getInTarget(board, i, 1, color);
                if (p >= 0 && !vis[p]) {
//                    stack.push(new Move(x, y, inx[p], iny[p]));
                    vis[p] = true;
                    return true;
                }
                p = getInTarget(board, i, -1, color);
                if (p >= 0 && !vis[p]) {
//                    stack.push(new Move(x, y, inx[p], iny[p]));
                    vis[p] = true;
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("static-access")
    private int getOutTarget(Map board, int p, int d, int color) {
        int i, end;

        if (d == 1) {
            end = ((p / 6 + 1) * 6 + LINE_LEN) % LINE_LEN;
        } else {
            end = ((p / 6) * 6 - 1 + LINE_LEN) % LINE_LEN;
        }
        for (i = (p + d + LINE_LEN) % LINE_LEN; i != end; i = (i + d + LINE_LEN) % LINE_LEN) {
            if (board.map[outx[i]][outy[i]] != board.NOSTONE) {
                return -1;
            }
        }
        for (; i != p; i = (i + d + LINE_LEN) % LINE_LEN) {
            if (outx[i] == outx[p] && outy[i] == outy[p]) {
                continue;
            }
            if (board.map[outx[i]][outy[i]] == color) {
                return -1;
            }
            if (board.map[outx[i]][outy[i]] == (color ^ 3)) {
                return i;
            }
        }

        return -1;
    }

    @SuppressWarnings("static-access")
    private int getInTarget(Map board, int p, int d, int color) {
        int i, end;

        if (d == 1) {
            end = ((p / 6 + 1) * 6 + LINE_LEN) % LINE_LEN;
        } else {
            end = ((p / 6) * 6 - 1 + LINE_LEN) % LINE_LEN;
        }
        for (i = (p + d + LINE_LEN) % LINE_LEN; i != end; i = (i + d + LINE_LEN) % LINE_LEN) {
            if (board.map[inx[i]][iny[i]] != board.NOSTONE) {
                return -1;
            }
        }
        for (; i != p; i = (i + d + LINE_LEN) % LINE_LEN) {
            if (inx[i] == inx[p] && iny[i] == iny[p]) {
                continue;
            }
            if (board.map[inx[i]][iny[i]] == color) {
                return -1;
            }
            if (board.map[inx[i]][iny[i]] == (color ^ 3)) {
                return i;
            }
        }

        return -1;
    }

    @SuppressWarnings("static-access")
    private boolean getUnAttackMoves(Map board, int x, int y, boolean debug) {
        int i, tx, ty;

        for (i = 0; i < 8; i++) {
            tx = x + stepx[i];
            ty = y + stepy[i];
            if (board.isInMap(tx, ty) && board.map[tx][ty] == board.NOSTONE) {
                return true;
            }
        }
        
        return false;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
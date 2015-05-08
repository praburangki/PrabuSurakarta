package Surakarta.logic;

import java.awt.Point;

/**
 *
 * @author praburangki
 */
public class EvaluateEnemy extends Evaluate {
    private final int[][] blackPosVal = {
        {5, 20, 20, 20, 20, 5},
        {20, 30, 50, 50, 30, 20},
        {20, 50, 40, 40, 50, 20},
        {30, 60, 50, 50, 60, 30},
        {30, 40, 60, 60, 40, 30},
        {5, 20, 20, 20, 20, 5}
    };

    private final int[][] whitePosVal = {
        {5, 20, 20, 20, 20, 5},
        {30, 40, 60, 60, 40, 30},
        {30, 60, 50, 50, 60, 30},
        {20, 50, 40, 40, 50, 20},
        {20, 30, 50, 50, 30, 20},
        {5, 20, 20, 20, 20, 5}
    };
    
    private final int[][] internalLoop = {
        {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5},
        {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4},
        {4, 5}, {4, 4}, {4, 3}, {4, 2}, {4, 1}, {4, 0},
        {5, 1}, {4, 1}, {3, 1}, {2, 1}, {1, 1}, {0, 1}
    };

    private final int[][] externalLoop = {
        {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5},
        {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3},
        {3, 5}, {3, 4}, {3, 3}, {3, 2}, {3, 1}, {3, 0},
        {5, 2}, {4, 2}, {3, 2}, {2, 2}, {1, 2}, {0, 2}
    };

    private final int[][] dir = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };
    
    int blackNum, whiteNum, blackAttacked, whiteAttacked, blackMoveRange,
        whiteMoveRange, blackAttackRange, whiteAttackRange, blackValue, whiteValue;
    int[][] attack, attackRange, moveRange;
    Point[] black, white;
    final int MAXSIZE = 6, NOSTONE = 0, BLACK = 2, WHITE = 1, 
        UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    Game game;

    
    public EvaluateEnemy(Game game) {
        super(game);
        black = new Point[12];
        white = new Point[12];

        for (int i = 0; i < 12; i++) {
            black[i] = new Point();
            white[i] = new Point();
        }

        attackRange = new int[MAXSIZE][MAXSIZE];
        attack = new int[MAXSIZE][MAXSIZE];
        moveRange = new int[MAXSIZE][MAXSIZE];
    }
    
    //<editor-fold defaultstate="collapsed" desc="find position">
    public void findPos() {
        blackNum = 0;
        whiteNum = 0;

        for (int i = 0; i < MAXSIZE; i++) {
            for (int j = 0; j < MAXSIZE; j++) {
                if (game.isBlack(i, j)) {
                    black[blackNum].x = i;
                    black[blackNum].y = j;
                    blackNum++;
                } else if (game.isWhite(i, j)) {
                    white[whiteNum].x = i;
                    white[whiteNum].y = j;
                    whiteNum++;
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="can attack">
    public int canAttack(int x, int y, int dir) {
        int i;
        int flag = -1;
        int count = 0;
        if (dir == UP) {
            for (i = x - 1; i >= 0; i--) {
                if (game.isEmpty(i, y)) count++;
                else break;
            }
            if (count == x) flag = 1;
            else flag = 0;
        } else if (dir == DOWN) {
            for (i = x + 1; i < MAXSIZE; i++) {
                if (game.isEmpty(i, y)) count++;
                else break;
            }
            if (count == MAXSIZE - x - 1) flag = 1;
            else flag = 0;
        } else if (dir == LEFT) {
            for (i = y - 1; i >= 0; i--) {
                if (game.isEmpty(x, i)) count++;
                else break;
            }
            if (count == y) flag = 1;
            else flag = 0;
        } else if (dir == RIGHT) {
            for (i = y + 1; i < MAXSIZE; i++) {
                if (game.isEmpty(x, i)) count++;
                else break;
            }
            if (count == MAXSIZE - y - 1) flag = 1;
            else flag = 0;
        }
        return flag;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="attack range">
    public void findAttackRange() {
        int i;
        int j;
        blackAttackRange = 0;
        whiteAttackRange = 0;
        int clockwise;
        int antiClockwise;

        //<editor-fold defaultstate="collapsed" desc="internal loop">
        //<editor-fold defaultstate="collapsed" desc="loop 0-6">
        for (i = 0; i < 6; i++) {
            clockwise = canAttack(internalLoop[i][0], internalLoop[i][1], RIGHT);
            antiClockwise = canAttack(internalLoop[i][0], internalLoop[i][1], LEFT);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 16 || j == 10 || j == 4) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 13 || j == 19 || j == 1) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 16 || j == 10 || j == 4) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == BLACK)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == WHITE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else
                            attack[internalLoop[j][0]][internalLoop[j][1]] = 2;
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 6-12">
        for (i = 6; i < 12; i++) {

            clockwise = canAttack(internalLoop[i][0], internalLoop[i][1], DOWN);
            antiClockwise = canAttack(internalLoop[i][0], internalLoop[i][1], UP);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 22 || j == 16 || j == 10) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 19 || j == 1 || j == 7) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 22 || j == 16 || j == 10) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == BLACK)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == WHITE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else
                            attack[internalLoop[j][0]][internalLoop[j][1]] = 2;
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 12-18">
        for (i = 12; i < 18; i++) {

            clockwise = canAttack(internalLoop[i][0], internalLoop[i][1], LEFT);
            antiClockwise = canAttack(internalLoop[i][0], internalLoop[i][1], RIGHT);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 4 || j == 22 || j == 16) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 1 || j == 7 || j == 13) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 4 || j == 22 || j == 16) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == BLACK)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == WHITE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else
                            attack[internalLoop[j][0]][internalLoop[j][1]] = 2;
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 18-24">
        for (i = 18; i < 24; i++) {

            clockwise = canAttack(internalLoop[i][0], internalLoop[i][1], UP);
            antiClockwise = canAttack(internalLoop[i][0], internalLoop[i][1], DOWN);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != 1; j = (j + 23) % 24) {
                    if (j == 10 || j == 4 || j == 22) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                            attack[internalLoop[i][0]][internalLoop[i][1]] = WHITE;
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 7 || j == 13 || j == 19) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == BLACK)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == WHITE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else
                            attack[internalLoop[j][0]][internalLoop[j][1]] = 2;
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 10 || j == 14 || j == 22) {
                        continue;
                    }
                    if (game.isBlack(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]++;
                    else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]))
                        attackRange[internalLoop[i][0]][internalLoop[i][1]]--;
                }
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(internalLoop[j][0], internalLoop[j][1])) {
                        if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == BLACK)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isBlack(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = BLACK;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == WHITE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else if (game.isWhite(internalLoop[j][0], internalLoop[j][1]) && attack[internalLoop[j][0]][internalLoop[j][1]] == NOSTONE)
                            attack[internalLoop[j][0]][internalLoop[j][1]] = WHITE;
                        else
                            attack[internalLoop[j][0]][internalLoop[j][1]] = 2;
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="external loop">
        //<editor-fold defaultstate="collapsed" desc="loop 0-6">
        for (i = 0; i < 6; i++) {

            clockwise = canAttack(externalLoop[i][0], externalLoop[i][1], RIGHT);
            antiClockwise = canAttack(externalLoop[i][0], externalLoop[i][1], LEFT);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 15 || j == 9 || j == 3) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 14 || j == 20 || j == 2) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 15 || j == 9 || j == 3) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 6-12">
        for (i = 6; i < 12; i++) {

            clockwise = canAttack(externalLoop[i][0], externalLoop[i][1], DOWN);
            antiClockwise = canAttack(externalLoop[i][0], externalLoop[i][1], UP);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 21 || j == 15 || j == 9) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 20 || j == 2 || j == 8) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 21 || j == 15 || j == 9) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 12-18">
        for (i = 12; i < 18; i++) {

            clockwise = canAttack(externalLoop[i][0], externalLoop[i][1], LEFT);
            antiClockwise = canAttack(externalLoop[i][0], externalLoop[i][1], RIGHT);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 3 || j == 21 || j == 15) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 2 || j == 8 || j == 14) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 3 || j == 21 || j == 15) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loop 18-24">
        for (i = 18; i < 24; i++) {

            clockwise = canAttack(externalLoop[i][0], externalLoop[i][1], UP);
            antiClockwise = canAttack(externalLoop[i][0], externalLoop[i][1], DOWN);

            if (clockwise == 0 && antiClockwise == 1) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 9 || j == 3 || j == 21) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 0) {
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 8 || j == 14 || j == 20) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
            } else if (clockwise == 1 && antiClockwise == 1) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 9 || j == 3 || j == 21) {
                        continue;
                    }
                    if (game.isBlack(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]++;
                    else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]))
                        attackRange[externalLoop[i][0]][externalLoop[i][1]]--;
                }
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (!game.isEmpty(externalLoop[j][0], externalLoop[j][1])) {
                        if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == BLACK)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isBlack(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = BLACK;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == WHITE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else if (game.isWhite(externalLoop[j][0], externalLoop[j][1]) && attack[externalLoop[i][0]][externalLoop[i][1]] == NOSTONE)
                            attack[externalLoop[i][0]][externalLoop[i][1]] = WHITE;
                        else
                            attack[externalLoop[i][0]][externalLoop[i][1]] = 2;                        
                        break;
                    }
                }
            }
        }
        //</editor-fold>
        //</editor-fold>

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if (attackRange[i][j] > 0) blackAttackRange++;
                else if (attackRange[i][j] < 0) whiteAttackRange++;
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="move range">
    public void findMoveRange() {
        int i;
        int j;
        blackMoveRange = 0;
        whiteMoveRange = 0;
        Point p = new Point();
        Point q = new Point();
        for (i = 0; i < blackNum; i++) {

            p.x = black[i].x;
            p.y = black[i].y;
            for (j = 0; j < 8; j++) {
                q.x = p.x + dir[j][0];
                q.y = p.y + dir[j][1];
                if (isInMap(q.x, q.y) && game.isEmpty(q.x, q.y)) {
                    if (attackRange[q.x][q.y] < 0) {
                        if (attack[q.x][q.y] < 0 || attack[q.x][q.y] == 2) {
                            continue;
                        }
                    }
                    moveRange[q.x][q.y] = BLACK;
                }
            }
        }
        for (i = 0; i < whiteNum; i++) {
            p.x = white[i].x;
            p.y = white[i].y;
            for (j = 0; j < 8; j++) {
                q.x = p.x + dir[j][0];
                q.y = p.y + dir[j][1];
                if (isInMap(q.x, q.y) && game.isEmpty(q.x, q.y)) {
                    if (attackRange[q.x][q.y] > 0) {
                        if (attack[q.x][q.y] > 0) {
                            continue;
                        }
                    }
                    if (moveRange[q.x][q.y] == BLACK) {
                        moveRange[q.x][q.y] = 2;
                    } else {
                        moveRange[q.x][q.y] = WHITE;
                    }
                }
            }
        }
        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if (moveRange[i][j] == BLACK) {
                    blackMoveRange++;
                } else if (moveRange[i][j] == WHITE) {
                    whiteMoveRange++;
                } else if (moveRange[i][j] == 2) {
                    blackMoveRange++;
                    whiteMoveRange++;
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="attacked piece">
    public void findAttackedPiece() {
        int i;
        blackAttacked = 0;
        whiteAttacked = 0;
        for (i = 0; i < blackNum; i++) {
            if (attackRange[black[i].x][black[i].y] < 0) {
                blackAttacked++;
            }
        }
        for (i = 0; i < whiteNum; i++) {
            if (attackRange[white[i].x][white[i].y] > 0) {
                whiteAttacked++;
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="position value">
    public void countPosValue() {
        int i;
        int blackPosValue = 0;
        int whitePosValue = 0;
        for (i = 0; i < blackNum; i++) {
            blackValue += blackPosVal[black[i].x][black[i].y];
        }
        for (i = 0; i < whiteNum; i++) {
            whiteValue += whitePosVal[white[i].x][white[i].y];
        }
    }
    //</editor-fold>
    
    private boolean isInMap(int x, int y) {
        return x >= 0 && x < 6 && y >= 0 && y < 6;
    }
    
    //<editor-fold defaultstate="collapsed" desc="evaluate">
    public int evaluate(int color, int depth) {
        blackValue = 0;
        whiteValue = 0;
        findPos();
        findAttackRange();
        findAttackedPiece();
        countPosValue();
        findMoveRange();
        if (color == BLACK) {
            if (whiteAttacked == 1) {
                whiteNum--;
            }
            if (blackAttacked > 1) {
                blackNum--;
            } else if (blackAttacked == 1) {
                blackValue -= 15;
            }
        } else if (color == WHITE) {
            if (blackAttacked == 1) {
                blackNum--;
            }
            if (whiteAttacked > 1) {
                whiteNum--;
            } else if (whiteAttacked == 1) {
                whiteValue -= 15;
            }
        }
        blackValue += blackNum * 30 + blackMoveRange * 5 + blackAttackRange * 10;
        whiteValue += whiteNum * 30 + whiteMoveRange * 5 + whiteAttackRange * 10;
        if (depth % 2 == 0) {
            if (color == BLACK) {
                return blackValue - whiteValue;
            } else {
                return whiteValue - blackValue;
            }
        } else {
            if (color == BLACK) {
                return whiteValue - blackValue;
            } else {
                return blackValue - whiteValue;
            }
        }
    }
    //</editor-fold>
}

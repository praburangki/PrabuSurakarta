/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

import ch08.logic.Move;
import ch08.logic.MoveValidator;
import java.util.Arrays;

/**
 *
 * @author praburangki
 */
public class Evaluate4 {

    int[][] attackPos = new int[6][6];
    int[][] protectPos = new int[6][6];
    final int BLACK = 1;
    final int WHITE = 2;

    MoveValidator validator;

    public Evaluate4(MoveValidator validator) {
        this.validator = validator;
    }

    private void getAttackInfo(int[][] map) {
        int[][] curPos = new int[6][6];
        for (int i = 0; i < curPos.length; i++) {
            curPos[i] = Arrays.copyOf(map[i], map[i].length);
        }

        for (int[] pos : attackPos) {
            Arrays.fill(pos, 0);
        }

        int i, j, m, n;
        boolean flag = true;
        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                for (m = 0; m < 6; m++) {
                    for (n = 0; n < 6; n++) {
                        while (flag) {
                            flag = false;
                            if (validator.isMoveValid(new Move(m, n, i, j)) && curPos[m][n] + curPos[i][j] == 3) {
                                attackPos[i][j]++;
                                flag = true;
                                n = -1;
                                m = 0;
                                curPos[m][n] = 0;
                            }
                        }
                    }
                }
                for (int k = 0; k < curPos.length; k++) {
                    curPos[k] = Arrays.copyOf(map[k], map[k].length);
                }
            }
        }
    }

    private void getProtectInfo(int[][] map) {
        int[][] curPos = new int[6][6];
        for (int i = 0; i < curPos.length; i++) {
            curPos[i] = Arrays.copyOf(map[i], map[i].length);
        }

        for (int[] pos : protectPos) {
            Arrays.fill(pos, 0);
        }

        int i, j, m, n;
        boolean flag = true;
        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                for (m = 0; m < 6; m++) {
                    for (n = 0; n < 6; n++) {
                        while (flag) {
                            flag = false;
                            if (curPos[m][n] - curPos[i][j] == 0 && curPos[m][n] == 1) {
                                curPos[m][n] = 3 - curPos[i][j];
                                if (validator.isMoveValid(new Move(m, n, i, j))) {
                                    protectPos[i][j]++;
                                    flag = true;
                                    n = -1;
                                    m = 0;
                                    curPos[m][n] = 0;
                                }
                            }
                        }
                    }
                }
                for (int k = 0; k < curPos.length; k++) {
                    curPos[k] = Arrays.copyOf(map[k], map[k].length);
                }
            }
        }
    }

    public int evaluate(int[][] map, int color) {
        int blackValue = 0;
        int whiteValue = 0;
        int bProtectValue = 0;
        int bAttackValue = 0;
        int wProtectValue = 0;
        int wAttackValue = 0;
        int i;
        int j;
        getAttackInfo(map);
        getProtectInfo(map);

        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                if (map[i][j] == 1) {
                    blackValue += 100;
                    if ((i == j || i + j == 5) && j == 1 && i == 1) {
                        blackValue += 20;
                    }
                    bProtectValue += protectPos[i][j];
                    if (attackPos[i][j] >= 100) {
                        blackValue -= 100;
                    } else {
                        bAttackValue += attackPos[i][j];
                    }
                    if (attackPos[i][j] >= 100) {
                        blackValue -= 100;
                    }
                }
                if (map[i][j] == 2) {
                    whiteValue += 100;
                    if ((i == j || i + j == 5) && j == 1 && i == 1) {
                        whiteValue += 20;
                    }
                    wProtectValue += protectPos[i][j];
                    if (attackPos[i][j] >= 100) {
                        whiteValue -= 100;
                    } else {
                        wAttackValue += attackPos[i][j];
                    }
                    if (attackPos[i][j] >= 100) {
                        whiteValue -= 100;
                    }
                }
            }
        }
        if (color == BLACK) {
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 6; j++) {
                    if (map[i][j] == 2) {
                        if (attackPos[i][j] - protectPos[i][j] > 0) {
                            blackValue += 100;
                        }
                        if (attackPos[i][j] - protectPos[i][j] == 0) {
                            whiteValue += 50;
                        }
                    }
                    if (map[i][j] == 1) {
                        if (protectPos[i][j] - attackPos[i][j] < 0) {
                            blackValue -= 50;
                        }
                    }

                }
            }
        } else {
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 6; j++) {
                    if (map[i][j] == 1) {
                        if (attackPos[i][j] - protectPos[i][j] > 0) {
                            whiteValue += 100;
                        }
                        if (attackPos[i][j] - protectPos[i][j] > 0) {
                            blackValue += 50;
                        }
                    }
                    if (map[i][j] == 2) {
                        if (protectPos[i][j] - attackPos[i][j] < 0) {
                            whiteValue -= 50;
                        }
                    }
                }
            }
        }
        blackValue -= bAttackValue + bProtectValue;
        whiteValue -= wAttackValue + wProtectValue;
        
        if (color == BLACK) {
            return blackValue - whiteValue;
        } else {
            return whiteValue - blackValue;
        }
    }
}

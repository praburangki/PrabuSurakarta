/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

import ch08.logic.Game;

/**
 *
 * @author praburangki
 */
class Stone {

    int x, y;
}

public class Evaluate {

    int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    int EACH_STONE = 300;
    int BE_ATTACKED = 150;
    int EACH_MOVE_RANGE = 50;
    int EACH_ATTACK_RANGE = 100;
    int MAXSIZE = 6;
    int BLACK = 2;
    int WHITE = 1;
    int NOSTONE = 0;
    int SELECT = 3;
    int MAX_VALUE = 9999999;

    Stone[] black, white;
    int[] greenx = {1, 1, 1, 1, 1, 1, 0, 1, 2, 3, 4, 5, 4, 4, 4, 4, 4, 4, 5, 4, 3, 2, 1, 0};
    int[] greeny = {0, 1, 2, 3, 4, 5, 4, 4, 4, 4, 4, 4, 5, 4, 3, 2, 1, 0, 1, 1, 1, 1, 1, 1};
    int[] bluex = {2, 2, 2, 2, 2, 2, 0, 1, 2, 3, 4, 5, 3, 3, 3, 3, 3, 3, 5, 4, 3, 2, 1, 0};
    int[] bluey = {0, 1, 2, 3, 4, 5, 3, 3, 3, 3, 3, 3, 5, 4, 3, 2, 1, 0, 2, 2, 2, 2, 2, 2};

    int[][] attackRange, attackNow, moveRange;

    int blackNum, whiteNum, blackMoveRange, whiteMoveRange;
    Game game;

    public Evaluate(Game game) {
        this.game = game;
        black = new Stone[12];
        white = new Stone[12];
        for (int i = 0; i < black.length; i++) {
            black[i] = new Stone();
            white[i] = new Stone();
        }
        attackRange = new int[MAXSIZE][MAXSIZE];
        attackNow = new int[MAXSIZE][MAXSIZE];
    }

    public boolean isValidPos(int x, int y) {
        return (x >= 0 && x < MAXSIZE) && (y >= 0 && y < MAXSIZE);
    }

    public void findPos(int[][] map) {
        int i, j;

        blackNum = 0;
        whiteNum = 0;
        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if (map[i][j] == BLACK) {
                    black[blackNum].x = i;
                    black[blackNum].y = j;
                    blackNum++;
                } else if (map[i][j] == WHITE) {
                    white[whiteNum].x = i;
                    white[whiteNum].y = j;
                    whiteNum++;
                }
            }
        }
    }

    public int canAttack(int[][] map, int x, int y, int d) {
        int i = 0, j = 0, blank = 0, flag = -1;
        if (d == 0) {
            for (i = x - 1; i >= 0; i--) {
                if (map[i][j] == NOSTONE) {
                    blank++;
                } else {
                    break;
                }
            }
            if (blank == x) {
                flag = 1;
            } else {
                flag = 0;
            }
        } else if (d == 1) {
            for (j = y + 1; j < MAXSIZE; j++) {
                if (map[i][j] == NOSTONE) {
                    blank++;
                } else {
                    break;
                }
            }
            if (blank == MAXSIZE - y - 1) {
                flag = 1;
            } else {
                flag = 0;
            }
        } else if (d == 2) {
            for (i = x + 1; i < MAXSIZE; i++) {
                if (map[i][j] == NOSTONE) {
                    blank++;
                } else {
                    break;
                }
            }
            if (blank == MAXSIZE - x - 1) {
                flag = 1;
            } else {
                flag = 0;
            }
        } else if (d == 3) {
            for (j = y - 1; j >= 0; j--) {
                if (map[i][j] == NOSTONE) {
                    blank++;
                } else {
                    break;
                }
            }
            if (blank == y) {
                flag = 1;
            } else {
                flag = 0;
            }
        }

        return flag;
    }

    public void findAttackRange(int[][] map) {
        int[][] tempMap;
        int i, j, p, canup, canlow;

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                attackRange[i][j] = 0;
                attackNow[i][j] = 0;
            }
        }

        for (i = 0; i < 6; i++) {
            canup = canAttack(map, greenx[i], greeny[i], LEFT);
            canlow = canAttack(map, greenx[i], greeny[i], RIGHT);

            if (canup != 0 && canlow == 0) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 16 || j == 10 || j == 4) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup != 0 && canlow == 0) {
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 13 || j == 19 || j == 1) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 16 || j == 10 || j == 4) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 1) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == -1) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        attackNow[greenx[i]][greeny[i]] = 2;
                    }
                    break;
                }
            }
        }

        //绿线右侧
        for (i = 6; i < 12; i++) {
            canup = canAttack(map, greenx[i], greeny[i], UP);
            canlow = canAttack(map, greenx[i], greeny[i], DOWN);
            if (canup != 0 && canlow == 0) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 22 || j == 16 || j == 10) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 19 || j == 1 || j == 7) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 22 || j == 16 || j == 10) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 1) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == -1) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        attackNow[greenx[i]][greeny[i]] = 2;
                    }
                    break;
                }
            }
        }

        //绿线下侧
        for (i = 12; i < 18; i++) {
            canup = canAttack(map, greenx[i], greeny[i], RIGHT);
            canlow = canAttack(map, greenx[i], greeny[i], LEFT);
            if (canup != 0 && canlow == 0) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 4 || j == 22 || j == 16) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 1 || j == 7 || j == 13) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 4 || j == 22 || j == 16) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 1) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == -1) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        attackNow[greenx[i]][greeny[i]] = 2;
                    }
                    break;
                }
            }
        }

        for (i = 18; i < 24; i++) {
            canup = canAttack(map, greenx[i], greeny[i], DOWN);
            canlow = canAttack(map, greenx[i], greeny[i], UP);
            if (canup != 0 && canlow == 0) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 10 || j == 4 || j == 22) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 7 || j == 13 || j == 19) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        if (map[greenx[j]][greeny[j]] == BLACK) {
                            attackNow[greenx[i]][greeny[i]] = 1;
                        } else if (map[greenx[j]][greeny[j]] == WHITE) {
                            attackNow[greenx[i]][greeny[i]] = -1;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 10 || j == 4 || j == 22) {
                        continue;
                    }
                    if (map[greenx[j]][greeny[j]] == BLACK) {
                        attackRange[greenx[i]][greeny[i]]++;
                    } else if (map[greenx[j]][greeny[j]] == WHITE) {
                        attackRange[greenx[i]][greeny[i]]--;
                    }
                }
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 1) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == BLACK && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = 1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == -1) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] == WHITE && map[greenx[i]][greeny[i]] == 0) {
                        attackNow[greenx[i]][greeny[i]] = -1;
                    } else if (map[greenx[j]][greeny[j]] != NOSTONE) {
                        attackNow[greenx[i]][greeny[i]] = 2;
                    }
                    break;
                }
            }
        }

        for (i = 0; i < 6; i++) {
            canup = canAttack(map, bluex[i], bluey[i], LEFT);
            canlow = canAttack(map, bluex[i], bluey[i], RIGHT);
            if (canup != 0 && canlow == 0) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 15 || j == 9 || j == 3) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 14 || j == 20 || j == 2) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 23; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 15 || j == 9 || j == 3) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
                for (j = 6; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
            }
        }
        
        for (i = 6; i < 12; i++) {
            canup = canAttack(map, bluex[i], bluey[i], UP);
            canlow = canAttack(map, bluex[i], bluey[i], DOWN);
            if (canup != 0 && canlow == 0) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 21 || j == 15 || j == 9) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 20 || j == 2 || j == 8) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 5; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 21 || j == 15 || j == 9) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
                for (j = 12; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
            }
        }

        for (i = 12; i < 18; i++) {
            canup = canAttack(map, bluex[i], bluey[i], RIGHT);
            canlow = canAttack(map, bluex[i], bluey[i], LEFT);
            if (canup != 0 && canlow == 0) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 3 || j == 21 || j == 15) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 2 || j == 8 || j == 14) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 11; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 3 || j == 21 || j == 15) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
                for (j = 18; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
            }
        }

        //蓝线下侧
        for (i = 18; i < 24; i++) {
            canup = canAttack(map, bluex[i], bluey[i], RIGHT);
            canlow = canAttack(map, bluex[i], bluey[i], LEFT);
            if (canup != 0 && canlow == 0) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 9 || j == 3 || j == 21) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup == 0 && canlow != 0) {
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 1) % 24) {
                    if (j == 8 || j == 14 || j == 20) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
            } else if (canup != 0 && canlow != 0) {
                for (j = 17; j != i; j = (j + 23) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
                for (; j != i; j = (j + 23) % 24) {
                    if (j == 9 || j == 3 || j == 21) {
                        continue;
                    }
                    if (map[bluex[j]][bluey[j]] == BLACK) {
                        attackRange[bluex[i]][bluey[i]]++;
                    } else if (map[bluex[j]][bluey[j]] == WHITE) {
                        attackRange[bluex[i]][bluey[i]]--;
                    }
                }
                for (j = 0; j != i; j = (j + 1) % 24) {
                    if (map[bluex[j]][bluey[j]] != NOSTONE) {
                        if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 1) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == BLACK && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = 1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == -1) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else if (map[bluex[j]][bluey[j]] == WHITE && attackNow[bluex[i]][bluey[i]] == 0) {
                            attackNow[bluex[i]][bluey[i]] = -1;
                        } else {
                            attackNow[bluex[i]][bluey[i]] = 2;
                        }
                        break;
                    }
                }
            }
        }
    }

    public void findMoveRange(int[][] map) {
        int i, j, r;
        int dx[] = {-1, -1, -1, 0, 1, 1, 1, 0}, dy[] = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                moveRange[i][j] = 0;
            }
        }
        blackMoveRange = whiteMoveRange = 0;

        for (i = 0; i < blackNum; i++) {
            for (r = 0; r < 8; r++) {
                if (isValidPos(black[i].x + dx[r], black[i].y + dy[r]) && map[black[i].x + dx[r]][black[i].y + dy[r]] == NOSTONE) {
                    if (attackRange[black[i].x + dx[r]][black[i].y + dy[r]] < 0) {
                        if (attackNow[black[i].x + dx[r]][black[i].y + dy[r]] < 0 || attackNow[black[i].x + dx[r]][black[i].y + dy[r]] == 2) {
                            continue;
                        }
                    }
                    moveRange[black[i].x + dx[r]][black[i].y + dy[r]] = 1;
                }
            }
        }
        for (i = 0; i < whiteNum; i++) {
            for (r = 0; r < 8; r++) {
                if (isValidPos(white[i].x + dx[r], white[i].y + dy[r]) && map[white[i].x + dx[r]][white[i].y + dy[r]] == NOSTONE) {
                    if (attackRange[white[i].x + dx[r]][white[i].y + dy[r]] > 0) {
                        if (attackNow[white[i].x + dx[r]][white[i].y + dy[r]] > 0) {
                            continue;
                        }
                    }
                    if (moveRange[white[i].x + dx[r]][white[i].y + dy[r]] == 1) {
                        moveRange[white[i].x + dx[r]][white[i].y + dy[r]] = 3;
                    } else {
                        moveRange[white[i].x + dx[r]][white[i].y + dy[r]] = 2;
                    }
                }
            }
        }

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if ((moveRange[i][j] & 1) != 0) {
                    blackMoveRange++;
                }
                if ((moveRange[i][j] & 2) != 0) {
                    whiteMoveRange++;
                }
            }
        }
    }

    public int evalue(int[][] map, int color) {
        int i, j;
        int blackBeingAttacked = 0, whiteBeingAttacked = 0;
        int blackValue = 0, whiteValue = 0;
        int blackAttackRange = 0, whiteAttackRange = 0;

        int[][] blackPosValue = {
            {50, 100, 100, 100, 100, 50},
            {100, 120, 150, 150, 120, 100},
            {100, 150, 120, 120, 150, 100},
            {110, 160, 130, 130, 160, 110},
            {110, 130, 160, 160, 130, 110},
            {50, 100, 100, 100, 100, 50}
        };

        int[][] whitePosValue = {
            {50, 100, 100, 100, 100, 50},
            {110, 130, 160, 160, 130, 110},
            {110, 160, 130, 130, 160, 110},
            {100, 150, 120, 120, 150, 100},
            {100, 120, 150, 150, 120, 100},
            {50, 100, 100, 100, 100, 50}
        };

        findPos(map);
        findAttackRange(map);
        findMoveRange(map);

        for (i = 0; i < blackNum; i++) {
            if (attackRange[black[i].x][black[i].y] < 0) {
                blackBeingAttacked++;
            }
        }
        for (i = 0; i < whiteNum; i++) {
            if (attackRange[white[i].x][white[i].y] > 0) {
                whiteBeingAttacked++;
            }
        }

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if (attackRange[i][j] > 0) {
                    blackAttackRange++;
                } else if (attackRange[i][j] < 0) {
                    whiteAttackRange++;
                }
            }
        }

        for (i = 0; i < MAXSIZE; i++) {
            for (j = 0; j < MAXSIZE; j++) {
                if (map[i][j] == BLACK) {
                    blackValue += blackPosValue[i][j];
                } else if (map[i][j] == WHITE) {
                    whiteValue += whitePosValue[i][j];
                }
            }
        }

        if (color == BLACK) {
            if (whiteBeingAttacked != 0) {
                whiteNum--;
            }
            if (blackBeingAttacked > 1) {
                blackNum--;
            } else if (blackBeingAttacked != 0) {
                blackValue -= BE_ATTACKED;
            }
        }
        if (color == WHITE) {
            if (blackBeingAttacked != 0) {
                blackNum--;
            }
            if (whiteBeingAttacked > 1) {
                whiteNum--;
            } else if (whiteBeingAttacked != 0) {
                whiteValue -= BE_ATTACKED;
            }
        }

        if (color == BLACK) {
            if (whiteNum == 0 && blackNum != 0) {
                return MAX_VALUE;
            }
            if (blackNum == 0 && whiteNum != 0) {
                return -MAX_VALUE;
            }

            if (blackMoveRange == 0 && whiteMoveRange != 0) {
                return -MAX_VALUE;
            }
            blackValue = blackValue + blackMoveRange * EACH_MOVE_RANGE + blackAttackRange
                    * EACH_ATTACK_RANGE + blackNum * EACH_STONE;
            whiteValue = whiteValue + whiteMoveRange * EACH_MOVE_RANGE + whiteAttackRange
                    * EACH_ATTACK_RANGE + whiteNum * EACH_STONE;

            return blackValue - whiteValue;
        } else if (color == WHITE) {
            if (blackNum == 0 && whiteNum != 0) {
                return MAX_VALUE;
            }
            if (whiteNum == 0 && blackNum != 0) {
                return -MAX_VALUE;
            }

            if (whiteMoveRange == 0 && blackMoveRange != 0) {
                return -MAX_VALUE;
            }
            blackValue = blackValue + blackMoveRange * EACH_MOVE_RANGE + blackAttackRange
                    * EACH_ATTACK_RANGE + blackNum * EACH_STONE;
            whiteValue = whiteValue + whiteMoveRange * EACH_MOVE_RANGE + whiteAttackRange
                    * EACH_ATTACK_RANGE + whiteNum * EACH_STONE;

            return whiteValue - blackValue;
        }

        return -1;
    }
}

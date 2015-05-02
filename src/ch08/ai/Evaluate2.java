/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

/**
 *
 * @author praburangki
 */
public class Evaluate2 {

    private int[][] whitePosValue = {
        {50, 100, 100, 100, 100, 50},
        {100, 120, 150, 150, 120, 100},
        {100, 150, 120, 120, 150, 100},
        {110, 160, 130, 130, 160, 110},
        {110, 130, 160, 160, 130, 110},
        {50, 100, 100, 100, 100, 50}
    };

    private int[][] blackPosValue = {
        {50, 100, 100, 100, 100, 50},
        {110, 130, 160, 160, 130, 110},
        {110, 160, 130, 130, 160, 110},
        {100, 150, 120, 120, 150, 100},
        {100, 120, 150, 150, 120, 100},
        {50, 100, 100, 100, 100, 50}
    };
    
    private final int STONE = 300;
    private int whiteNum;
    private int blackNum;
    
    private void findPos(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if(map[i][j] == 2)
                    blackNum++;
                else if(map[i][j] == 1)
                    whiteNum++;
            }
        }
    }

    public int evalue(int[][] map, int color) {
        int whiteScore, blackScore;
        
        findPos(map);
        
        whiteScore = whiteNum * STONE;
        blackScore = blackNum * STONE;
        
        whiteScore += getPosValue(map, 1);
        blackScore += getPosValue(map, 2);
        
        if(color == 1) return whiteScore - blackScore;
        
        return blackScore - whiteScore;
    }
    
    private int getPosValue(int[][] map, int color) {
        int res = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if(map[i][j] == color) {
                    if(color == 1) res += whitePosValue[i][j];
                    else res += blackPosValue[i][j];
                }
            }
        }
        
        return res;
    }
}

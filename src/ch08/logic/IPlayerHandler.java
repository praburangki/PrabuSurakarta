/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.logic;

/**
 *
 * @author praburangki
 */
public interface IPlayerHandler {
    public Move getMove();
    
    public void moveSuccessfullyExecuted(Move move);
}

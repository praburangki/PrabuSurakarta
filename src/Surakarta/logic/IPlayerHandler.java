package Surakarta.logic;

/**
 *
 * @author praburangki
 */
public interface IPlayerHandler {
    public Move getMove();
    
    public void moveSuccessfullyExecuted(Move move);
}

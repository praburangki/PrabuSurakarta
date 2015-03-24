package chess;
import chess.ai.SimpleAiPlayerHandler;
import chess.console.ChessConsole;
import chess.gui.ChessGui;
import chess.logic.ChessGame;
import chess.logic.Piece;


public class Main {

	public static void main(String[] args) {

		// first we create the game
		ChessGame chessGame = new ChessGame();

		// then we create the clients/players
		ChessGui chessGui = new ChessGui(chessGame);
		//ChessConsole chessConsole = new ChessConsole(chessGame);
		SimpleAiPlayerHandler ai1 = new SimpleAiPlayerHandler(chessGame);
		SimpleAiPlayerHandler ai2 = new SimpleAiPlayerHandler(chessGame);

		// set strength of AI
		ai1.maxDepth = 1;
		ai2.maxDepth = 2;

		// then we attach the clients/players to the game
		//chessGame.setPlayer(Piece.COLOR_WHITE, chessGui);
		//chessGame.setPlayer(Piece.COLOR_WHITE, chessConsole);
		chessGame.setPlayer(Piece.COLOR_WHITE, ai2);
		//chessGame.setPlayer(Piece.COLOR_BLACK, ai1);
		chessGame.setPlayer(Piece.COLOR_BLACK, chessGui);

		// in the end we start the game
		new Thread(chessGame).start();
	}
	
}
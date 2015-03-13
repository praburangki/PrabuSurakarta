package chess.logic;

import chess.console.ChessConsole;


/**
 * reference
 *   a  b  c  d  e  f  g  h  
 *  +--+--+--+--+--+--+--+--+
 * 8|BR|BN|BB|BQ|BK|BB|BN|BR|8
 * +--+--+--+--+--+--+--+--+
 * 7|BP|BP|BP|BP|BP|BP|BP|BP|7
 *  +--+--+--+--+--+--+--+--+
 * ..
 * 2|WP|WP|WP|WP|WP|WP|WP|WP|2
 *  +--+--+--+--+--+--+--+--+
 * 1|WR|WN|WB|WQ|WK|WB|WN|WR|1
 *  +--+--+--+--+--+--+--+--+
 *   a  b  c  d  e  f  g  h  
 *
 */
public class MoveValidator {

	private ChessGame chessGame;
	private Piece sourcePiece;
	private Piece targetPiece;
	private boolean debug;

	public MoveValidator(ChessGame chessGame){
		this.chessGame = chessGame;
	}
	
	/**
	 * Checks if the specified move is valid
	 * @param move to validate
	 * @param debug 
	 * @return true if move is valid, false if move is invalid
	 */
	public boolean isMoveValid(Move move, boolean debug) {
		this.debug = debug;
		int sourceRow = move.sourceRow;
		int sourceColumn = move.sourceColumn;
		int targetRow = move.targetRow;
		int targetColumn = move.targetColumn;
		
		sourcePiece = this.chessGame.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
		targetPiece = this.chessGame.getNonCapturedPieceAtLocation(targetRow, targetColumn);
			
		// source piece does not exist
		if( sourcePiece == null ){
			log("source piece does not exist");
			return false;
		}
		
		// source piece has right color?
		if( sourcePiece.getColor() == Piece.COLOR_WHITE
				&& this.chessGame.getGameState() == ChessGame.GAME_STATE_WHITE){
			// ok
		}else if( sourcePiece.getColor() == Piece.COLOR_BLACK
				&& this.chessGame.getGameState() == ChessGame.GAME_STATE_BLACK){
			// ok
		}else{
			log("it's not your turn: "
					+"pieceColor="+Piece.getColorString(sourcePiece.getColor())
					+"gameState="+this.chessGame.getGameState());
			ChessConsole.printCurrentGameState(this.chessGame);
			// it's not your turn
			return false;
		}
		
		// check if target location within boundaries
		if( targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8
				|| targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H){
			//target row or column out of scope
			log("target row or column out of scope");
			return false;
		}
		
		// validate piece movement rules
		boolean validPieceMove = false;
		switch (sourcePiece.getType()) {
			case Piece.TYPE_BISHOP:
				validPieceMove = isValidBishopMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			case Piece.TYPE_KING:
				validPieceMove = isValidKingMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			case Piece.TYPE_KNIGHT:
				validPieceMove = isValidKnightMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			case Piece.TYPE_PAWN:
				validPieceMove = isValidPawnMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			case Piece.TYPE_QUEEN:
				validPieceMove = isValidQueenMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			case Piece.TYPE_ROOK:
				validPieceMove = isValidRookMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
			default: break;
		}
		if( !validPieceMove){
			return false;
		}else{
			// ok
		}
		
		
		// handle stalemate and checkmate
		// ..
		
		return true;
	}

	private boolean isTargetLocationCaptureable() {
		if( targetPiece == null ){
			return false;
		}else if( targetPiece.getColor() != sourcePiece.getColor()){
			return true;
		}else{
			return false;
		}
	}

	private boolean isTargetLocationFree() {
		return targetPiece == null;
	}

	private boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		//The bishop can move any number of squares diagonally, but may not leap
		//over other pieces.
		
		// target location possible?
		if( isTargetLocationFree() || isTargetLocationCaptureable()){
			//ok
		}else{
			//target location not free and not captureable
			return false;
		}
		
		boolean isValid = false;
		// first lets check if the path to the target is diagonally at all
		int diffRow = targetRow - sourceRow;
		int diffColumn = targetColumn - sourceColumn;
		
		if( diffRow==diffColumn && diffColumn > 0){
			// moving diagonally up-right
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,+1,+1);

		}else if( diffRow==-diffColumn && diffColumn > 0){
			// moving diagnoally down-right
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,-1,+1);
			
		}else if( diffRow==diffColumn && diffColumn < 0){
			// moving diagnoally down-left
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,-1,-1);

		}else if( diffRow==-diffColumn && diffColumn < 0){
			// moving diagnoally up-left
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,+1,-1);
			
		}else{
			// not moving diagonally
			isValid = false;
		}
		return isValid;
	}

	private boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		// The queen combines the power of the rook and bishop and can move any number
		// of squares along rank, file, or diagonal, but it may not leap over other pieces.
		//
		boolean result = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
		result |= isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
		return result;
	}

	private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		boolean isValid = false;
		// The pawn may move forward to the unoccupied square immediately in front
		// of it on the same file, or on its first move it may advance two squares
		// along the same file provided both squares are unoccupied
		if( isTargetLocationFree() ){
			if( sourceColumn == targetColumn){
				// same column
				if(  sourcePiece.getColor() == Piece.COLOR_WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// move one up
						isValid = true;
					}else{
						//not moving one up
						isValid = false;
					}
				}else{
					// black
					if( sourceRow-1 == targetRow ){
						// move one down
						isValid = true;
					}else{
						//not moving one down
						isValid =  false;
					}
				}
			}else{
				// not staying in the same column
				isValid = false;
			}
			
		// or it may move
		// to a square occupied by an opponent�s piece, which is diagonally in front
		// of it on an adjacent file, capturing that piece. 
		}else if( isTargetLocationCaptureable() ){
			
			if( sourceColumn+1 == targetColumn || sourceColumn-1 == targetColumn){
				// one column to the right or left
				if(  sourcePiece.getColor() == Piece.COLOR_WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// move one up
						isValid = true;
					}else{
						//not moving one up
						isValid = false;
					}
				}else{
					// black
					if( sourceRow-1 == targetRow ){
						// move one down
						isValid = true;
					}else{
						//not moving one down
						isValid = false;
					}
				}
			}else{
				// note one column to the left or right
				isValid = false;
			}
		}
		
		// on its first move it may advance two squares
		// ..
		
		// The pawn has two special
		// moves, the en passant capture, and pawn promotion.
		
		// en passant
		// ..
		return isValid;
	}

	private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		// The knight moves to any of the closest squares which are not on the same rank,
		// file or diagonal, thus the move forms an "L"-shape two squares long and one
		// square wide. The knight is the only piece which can leap over other pieces.
		
		// target location possible?
		if( isTargetLocationFree() || isTargetLocationCaptureable()){
			//ok
		}else{
			//target location not free and not captureable
			return false;
		}
		
		if( sourceRow+2 == targetRow && sourceColumn+1 == targetColumn){
			// move up up right
			return true;
		}else if( sourceRow+1 == targetRow && sourceColumn+2 == targetColumn){
			// move up right right
			return true;
		}else if( sourceRow-1 == targetRow && sourceColumn+2 == targetColumn){
			// move down right right
			return true;
		}else if( sourceRow-2 == targetRow && sourceColumn+1 == targetColumn){
			// move down down right
			return true;
		}else if( sourceRow-2 == targetRow && sourceColumn-1 == targetColumn){
			// move down down left
			return true;
		}else if( sourceRow-1 == targetRow && sourceColumn-2 == targetColumn){
			// move down left left
			return true;
		}else if( sourceRow+1 == targetRow && sourceColumn-2 == targetColumn){
			// move up left left
			return true;
		}else if( sourceRow+2 == targetRow && sourceColumn-1 == targetColumn){
			// move up up left
			return true;
		}else{
			return false;
		}
	}

	private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		// target location possible?
		if( isTargetLocationFree() || isTargetLocationCaptureable()){
			//ok
		}else{
			//target location not free and not captureable
			return false;
		}
		
		// The king moves one square in any direction, the king has also a special move which is
		// called castling and also involves a rook.
		boolean isValid = true;
		if( sourceRow+1 == targetRow && sourceColumn == targetColumn){
			//up
			isValid = true;
		}else if( sourceRow+1 == targetRow && sourceColumn+1 == targetColumn){
			//up right
			isValid = true;
		}else if( sourceRow == targetRow && sourceColumn+1 == targetColumn){
			//right
			isValid = true;
		}else if( sourceRow-1 == targetRow && sourceColumn+1 == targetColumn){
			//down right
			isValid = true;
		}else if( sourceRow-1 == targetRow && sourceColumn == targetColumn){
			//down
			isValid = true;
		}else if( sourceRow-1 == targetRow && sourceColumn-1 == targetColumn){
			//down left
			isValid = true;
		}else if( sourceRow == targetRow && sourceColumn-1 == targetColumn){
			//left
			isValid = true;
		}else if( sourceRow+1 == targetRow && sourceColumn-1 == targetColumn){
			//up left
			isValid = true;
		}else{
			//moving too far
			isValid = false;
		}
		
		// castling
		// ..
		
		return isValid;
	}

	private boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		// The rook can move any number of squares along any rank or file, but
		// may not leap over other pieces. Along with the king, the rook is also
		// involved during the king's castling move.
		if( isTargetLocationFree() || isTargetLocationCaptureable()){
			//ok
		}else{
			//target location not free and not captureable
			return false;
		}
		
		boolean isValid = false;
		
		// first lets check if the path to the target is straight at all
		int diffRow = targetRow - sourceRow;
		int diffColumn = targetColumn - sourceColumn;
		
		if( diffRow == 0 && diffColumn > 0){
			// right
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,0,+1);

		}else if( diffRow == 0 && diffColumn < 0){
			// left
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,0,-1);
			
		}else if( diffRow > 0 && diffColumn == 0){
			// up
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,+1,0);

		}else if( diffRow < 0 && diffColumn == 0){
			// down
			isValid = !arePiecesBetweenSourceAndTarget(sourceRow,sourceColumn,targetRow,targetColumn,-1,0);
			
		}else{
			//not moving straight
			isValid = false;
		}
		
		return isValid;
	}


	private boolean arePiecesBetweenSourceAndTarget(int sourceRow, int sourceColumn,
			int targetRow, int targetColumn, int rowIncrementPerStep, int columnIncrementPerStep) {
		
		int currentRow = sourceRow + rowIncrementPerStep;
		int currentColumn = sourceColumn + columnIncrementPerStep;
		while(true){
			if(currentRow == targetRow && currentColumn == targetColumn){
				break;
			}
			if( currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8
				|| currentColumn < Piece.COLUMN_A || currentColumn > Piece.COLUMN_H){
				break;
			}

			if( this.chessGame.isNonCapturedPieceAtLocation(currentRow, currentColumn)){
				// pieces in between source and target
				return true;
			}

			currentRow += rowIncrementPerStep;
			currentColumn += columnIncrementPerStep;
		}
		return false;
	}
	
	public static void main(String[] args) {
		ChessGame ch = new ChessGame();
		MoveValidator mo = new MoveValidator(ch);
		Move move = null;
		boolean isValid;
		
		int sourceRow; int sourceColumn; int targetRow; int targetColumn;
		int testCounter = 1;

		// ok
		sourceRow = Piece.ROW_2; sourceColumn = Piece.COLUMN_D;
		targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_D;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid( move,true);
		ch.movePiece(move);
		System.out.println(testCounter+". test result: "+(true==isValid));
		testCounter++;
		ch.changeGameState();
		
		// it's not white's turn
		sourceRow = Piece.ROW_2; sourceColumn = Piece.COLUMN_B;
		targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_B;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid( move, true);
		System.out.println(testCounter+". test result: "+(false==isValid));
		testCounter++;
		
		// ok
		sourceRow = Piece.ROW_7; sourceColumn = Piece.COLUMN_E;
		targetRow = Piece.ROW_6; targetColumn = Piece.COLUMN_E;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid( move, true);
		ch.movePiece( move );
		System.out.println(testCounter+". test result: "+(true==isValid));
		testCounter++;
		ch.changeGameState();
		
		// pieces in the way
		sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_F;
		targetRow = Piece.ROW_4; targetColumn = Piece.COLUMN_C;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid(move, true);
		System.out.println(testCounter+". test result: "+(false==isValid));
		testCounter++;
		
		// ok
		sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_C;
		targetRow = Piece.ROW_4; targetColumn = Piece.COLUMN_F;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid(move, true);
		ch.movePiece(move);
		System.out.println(testCounter+". test result: "+(true==isValid));
		testCounter++;
		ch.changeGameState();
		
		// ok
		sourceRow = Piece.ROW_8; sourceColumn = Piece.COLUMN_B;
		targetRow = Piece.ROW_6; targetColumn = Piece.COLUMN_C;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid( move, true);
		ch.movePiece(move);
		System.out.println(testCounter+". test result: "+(true==isValid));
		testCounter++;
		ch.changeGameState();
		
		// invalid knight move
		sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
		targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_G;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid( move, true);
		System.out.println(testCounter+". test result: "+(false==isValid));
		testCounter++;
		
		// invalid knight move
		sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
		targetRow = Piece.ROW_2; targetColumn = Piece.COLUMN_E;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid(move, true);
		System.out.println(testCounter+". test result: "+(false==isValid));
		testCounter++;
		
		// ok
		sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
		targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_H;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid(move, true);
		ch.movePiece(move);
		System.out.println(testCounter+". test result: "+(true==isValid));
		testCounter++;
		ch.changeGameState();

		// pieces in between
		sourceRow = Piece.ROW_8; sourceColumn = Piece.COLUMN_A;
		targetRow = Piece.ROW_5; targetColumn = Piece.COLUMN_A;
		move = new Move(sourceRow, sourceColumn, targetRow, targetColumn);
		isValid = mo.isMoveValid(move, true);
		ch.movePiece(move);
		System.out.println(testCounter+". test result: "+(false==isValid));
		testCounter++;
		
		//ConsoleGui.printCurrentGameState(ch);

	}

	private void log(String message) {
		if(debug) System.out.println(message);
		
	}
	
}

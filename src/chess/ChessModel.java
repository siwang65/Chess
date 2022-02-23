package chess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import enums.ChessColor;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import shared.Move;

/**
 * Model of the chess game
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
@SuppressWarnings("deprecation")
public class ChessModel extends Observable implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This is used to create new piece
	 */
	private static final boolean WHITE = true;
	
	/**
	 * This is used to create new piece
	 */
	private static final boolean BLACK = false;
	
	/**
	 * The game board which holds all pieces
	 * [row][column]
	 * column index 0-7 correspond to a-h
	 */
	private Piece[][] board;
	
	// TODO make private again (Also change accessors in loadGame)
	
	/**
	 * Log for en passant
	 */
	public Pawn enPassant = null;
	
	/**
	 * The white king on the board
	 */
	public King whiteKing;
	
	/**
	 * The black king on the board
	 */
	public King blackKing;

	/**
	 * Whether it's my turn or not
	 */
	private boolean myTurn = false;
	
	/**
	 * Set myTurn based on the input
	 * 
	 * @param mine	Whether it's my turn or not
	 */
	public void setMyTurn(boolean mine) { myTurn = mine; }
	
	/**
	 * Check whether it's my turn
	 * 
	 * @return	true if it's my turn
	 * 			false if else
	 */
	public boolean isMyTurn() { return myTurn; }

	public ChessModel() {
		board = new Piece[8][8];
		initializeBoard();
	}
	
	/**
	 * Get the piece at a specific location
	 * 
	 * @param row	The row of the piece
	 * @param col	The column of the piece
	 * @return		The piece at this location
	 */
	public Piece getPiece(int row, int col) { return board[row][col]; }

	/**
	 * Initialize the board
	 */
	private void initializeBoard() {
		board[7][0] = new Rook(BLACK, 7, 0);
		board[7][1] = new Knight(BLACK, 7, 1);
		board[7][2] = new Bishop(BLACK, 7, 2);
		board[7][3] = new Queen(BLACK, 7, 3);
		blackKing = new King(BLACK, 7, 4);
		board[7][4] = blackKing;
		board[7][5] = new Bishop(BLACK, 7, 5);
		board[7][6] = new Knight(BLACK, 7, 6);
		board[7][7] = new Rook(BLACK, 7, 7);

		for (int col = 0; col < 8; col++)
			board[6][col] = new Pawn(BLACK, 6, col);

		board[0][0] = new Rook(WHITE, 0, 0);
		board[0][1] = new Knight(WHITE, 0, 1);
		board[0][2] = new Bishop(WHITE, 0, 2);
		board[0][3] = new Queen(WHITE, 0, 3);
		whiteKing = new King(WHITE, 0, 4);
		board[0][4] = whiteKing;
		board[0][5] = new Bishop(WHITE, 0, 5);
		board[0][6] = new Knight(WHITE, 0, 6);
		board[0][7] = new Rook(WHITE, 0, 7);

		for (int col = 0; col < 8; col++)
			board[1][col] = new Pawn(WHITE, 1, col);
	}

	/**
	 * Make a move and notify the observer
	 * 
	 * @param currRow	The row of the piece we want to move
	 * @param currCol	The column of the piece we want to move
	 * @param desRow	The row we want to move this piece to
	 * @param desCol	The column we want to move this piece to
	 * @param who		Who is calling this method
	 * @return			The move message
	 */
	public Move makeAMove(int currRow, int currCol, int desRow, int desCol, String who) {
		int isValid = board[currRow][currCol].isValidMove(board, desRow, desCol);
		if (isValid == 0) return null;
		Move msg= new Move(currRow, currCol, desRow, desCol);

		Piece curr = null;
		King currKing = null;
		// Valid normal move
		if (isValid == 1) {
			if (board[desRow][desCol] instanceof King) {
				King k = (King) board[desRow][desCol];
				if (k.getColor().equals(ChessColor.WHITE))
					whiteKing = null;
				else blackKing = null;
			}
			board[desRow][desCol] = board[currRow][currCol];
			board[desRow][desCol].updateLocation(desRow, desCol);
			board[currRow][currCol] = null;
			curr = board[desRow][desCol];
		// Valid en passant
		} else if (isValid == 2) {
			Pawn p = (Pawn) board[currRow][currCol];
			Pawn temp = (Pawn) board[currRow][desCol];
			if (enPassant.equals(temp)) {
				board[currRow][desCol] = null;
				board[desRow][desCol] = p;
				board[desRow][desCol].updateLocation(desRow, desCol);
				curr = board[desRow][desCol];
				board[currRow][currCol] = null;
			}
		// Valid castle
		} else if (isValid == 3) {
			King king = (King) board[currRow][currCol];
			int xDir = 1;
			if (desCol - currCol < 0) xDir = -1;
			Rook rook = findRook(xDir, desRow, desCol);
			int rookCol = rook.getCol();
			board[desRow][desCol] = king;
			board[desRow][desCol].updateLocation(desRow, desCol);
			board[currRow][currCol] = null;
			board[currRow][rookCol] = null;
			board[desRow][desCol - xDir] = rook;
			board[desRow][desCol - xDir].updateLocation(desRow, desCol - xDir);
			curr = board[desRow][desCol - xDir];
			currKing = (King) board[desRow][desCol];
		}

		// If we have a enPassantable piece, after the next move,
		// No matter it's captured or not, it's not enPassantable anymore
		if (enPassant != null) {
			enPassant.setEnPassant(false);
			enPassant = null;
		}

		if (isGameOver()) {
			setChanged();
			this.notifyObservers(msg);
			return msg;
		}
		
		// check handling
		if (curr instanceof King) ((King) curr).updateCheckedBy(board);
		else isChecking(curr);
		if (currKing != null) isChecking(currKing);
		// Pawn handling
		if (curr instanceof Pawn) {
			Pawn p = (Pawn) curr;
			// Check if we need to promote this piece
			int endRow = 0;
			if (p.getColor().equals(ChessColor.WHITE))
				endRow = 7;
			else endRow = 0;
			if (desRow == endRow && who.equals("controller"))
				msg.setPromotion(true, "c");
			// Check if it's en passantable now
			if (p.checkIfFirstMove()) {
				p.firstMoveDone();
				if (Math.abs(desRow - currRow) == 2) {
					p.setEnPassant(true);
					enPassant = p;
				}
			} else if (curr.checkIfFirstMove())
				curr.firstMoveDone();
		}
		setChanged();
		this.notifyObservers(msg);
		return msg;
	}

	/**
	 * Helper for castle, find the rook 
	 * 
	 * @param xDir		The direction we're looking
	 * @param desRow	The final row of the king
	 * @param desCol	The final column of the king
	 * @return			The rood we found
	 */
	private Rook findRook(int xDir, int desRow, int desCol) {
		int tempCol = desCol + xDir;
		while (tempCol >= 0 && tempCol < 8) {
			if (board[desRow][tempCol] instanceof Rook)
				return (Rook) board[desRow][tempCol];
			tempCol += xDir;
		}
		return null;
	}
	
	/**
	 * Generate a random valid move for the AI
	 * This AI can't do castle, en passant, or promotion
	 * 
	 * @param color		The color of the controller
	 * @return			The move we found (null if none valid)
	 */
	public Move getRandomMove(ChessColor color) {
		ArrayList<Move> validMoves = new ArrayList<>();
		for (int r = 0; r<8; r++) {
			for (int c = 0; c<8; c++) {
				if (board[r][c] == null) continue;
				if (board[r][c].getColor().equals(color))
					validMoves.addAll(board[r][c].validMoves(board));
			}
		}
		
		Collections.shuffle(validMoves);
		
		for (Move move : validMoves) {
			Piece p = board[move.getCol()][move.getRow()];
			if (p instanceof Pawn) {
				// TODO: verify if check!
				if (color.equals(ChessColor.WHITE) 
						&& move.getRow() == 7) 
					continue;
				else if (color.equals(ChessColor.BLACK)
						&& move.getRow() == 0)
					continue;
			} else return move;
		}
		return null;
	}

	/**
	 * Helper method for the check logic
	 * Check whether the input piece is checking
	 * the opponent's king
	 * 
	 * @param p		The piece we want to check
	 */
	private void isChecking(Piece p) {
		ArrayList<Move> validMoves = p.validMoves(board);
		King king;
		if (p.getColor().equals(ChessColor.WHITE)) king = blackKing;
		else king = whiteKing;
		for (Move m: validMoves) {
			if (m.getCol() == king.getCol() &&
					m.getRow() == king.getRow()) {
				king.addToCheckedBy(p);
				return;
			}
		}
	}

	/**
	 * Promote a pawn and notify the GUI
	 * 
	 * @param row	The row of the pawn we want to promote
	 * @param col	The column of the pawn we want to promote
	 * @param type	The type of piece we want to promote the pawn to
	 * @return		The change message
	 */
	public Piece promotePawn(int row, int col, String type) {
		boolean color = false;
		if (board[row][col].getColor().equals(ChessColor.WHITE))
			color = true;
		Piece p = null;
		if (type.equals("Queen")) p = new Queen(color, row, col);
		else if (type.equals("Rook")) p = new Rook(color, row, col);
		else if (type.equals("Bishop")) p = new Bishop(color, row, col);
		else if (type.equals("Knight")) p = new Knight(color, row, col);
		else {System.err.println("Invalid type: " + type);}
		addPiece(p);
		return p;
	}

	/**
	 * Check whether our king is checked by the opponent
	 * 
	 * @param color	The color of our controller
	 * @return		true if our king is checked
	 * 				false if not
	 */
	public boolean isChecked(ChessColor color) {
		King king = color.equals(ChessColor.WHITE) ? whiteKing : blackKing;
		return king.isChecked();
	}

	/**
	 * Check whether the game is over
	 * 
	 * @return	true if the game is over
	 * 			false if not
	 */
	public boolean isGameOver() {
		return blackKing == null || whiteKing == null;
	}

	/**
	 * Check whether it's the winner or not
	 * 
	 * @param color The color of our controller
	 * @return		true if it's the winner
	 * 				false if not
	 */
	public boolean isWinner(ChessColor color) {
		if (color.equals(ChessColor.WHITE)) return blackKing == null;
		else return whiteKing == null;
	}

	/**
	 * Save the game into a file
	 * 
	 * @param fn	File name of the save file
	 */
	public void saveGame(String fn) {
		try {
			FileOutputStream file = new FileOutputStream(fn + ".chess");
			ObjectOutputStream oos = new ObjectOutputStream(file);
			oos.writeObject(this);
			oos.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load a saved game
	 * 
	 * @param fn	File name of the save file
	 */
	public void loadGame(String fn) {
		try {
			FileInputStream file = new FileInputStream(fn + ".chess");
			ObjectInputStream ois = new ObjectInputStream(file);
			ChessModel saved = (ChessModel) ois.readObject();
			ois.close();
			file.close();

			loadGame(saved);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a saved game
	 * 
	 * @param model	 The new model
	 */
	public void loadGame(ChessModel model) {
		this.enPassant = model.enPassant;
		this.whiteKing = model.whiteKing;
		this.blackKing = model.blackKing;
		this.myTurn = model.isMyTurn();
		this.board = model.getBoard();

		this.setChanged();
		this.notifyObservers(model);
	}
	
	/**
	 * Add a piece to the board
	 * 
	 * @param piece
	 */
	public void addPiece(Piece piece) {
		this.board[piece.getRow()][piece.getCol()] = piece;

		this.setChanged();
		this.notifyObservers(piece);
	}

	/**
	 * Get the game board
	 * 
	 * @return	The game board
	 */
	public Piece[][] getBoard() { return board; }
	
	// this is just for testing purpose
//	public void printBoard() {
//		System.out.println(">>>");
//
//		for (int row = 7; row >= 0; row--) {
//			System.out.print("|");
//			for (int col = 0; col < 8; col++) {
//				Piece p = board[row][col];
//				if (p == null) {
//					System.out.print("  |");
//					continue;
//				}
//				String color = " ";
//				String pieceType = " ";
//				if (p.getColor().equals(ChessColor.WHITE)) color = "W";
//				else color = "B";
//				if (p instanceof Pawn) pieceType = "P";
//				else if (p instanceof King) pieceType = "K";
//				else if (p instanceof Queen) pieceType = "Q";
//				else if (p instanceof Knight) pieceType = "N";
//				else if (p instanceof Bishop) pieceType = "B";
//				else if (p instanceof Rook) pieceType = "R";
//				System.out.print(color+pieceType+"|");
//			}
//			System.out.println();
//			System.out.println(" -- -- -- -- -- -- -- -- ");
//		}
//
//		System.out.println();
//	}
}
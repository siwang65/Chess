package pieces;

import java.io.Serializable;
import java.util.ArrayList;
import enums.ChessColor;
import shared.Location;
import shared.Move;

/**
 * The Piece superclass
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public abstract class Piece implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The color of this piece
	 */
	protected ChessColor color;
	
	/**
	 * The row of this piece
	 */
	protected int row;
	
	/**
	 * The column of this piece
	 */
	protected int col;

	/**
	 * Whether this is the first move or not
	 */
	protected boolean isFirstMove = true;

	/**
	 * Constructor
	 * 
	 * @param white		Whether this piece is white or not
	 */
	public Piece(boolean white) {
		this(white, -1, -1);
	}

	/**
	 * Constructor
	 * 
	 * @param white		Whether this piece is white or not
	 * @param r			The row of this piece
	 * @param c			The column of this piece
	 */
	public Piece(boolean white, int r, int c) {
		if (white) {
			color = ChessColor.WHITE;
		} else {
			color = ChessColor.BLACK;
		}

		row = r;
		col = c;
	}

	/**
	 * The first move is made
	 */
	public void firstMoveDone() { isFirstMove = false; }

	/**
	 * Whether this move is the first move or not
	 * 
	 * @return	true if this is the first move
	 * 			false if not
	 */
	public boolean checkIfFirstMove() { return isFirstMove; }

	/**
	 * Get the color of this piece
	 * 
	 * @return	The color of this piece
	 */
	public ChessColor getColor() { return color; }

	/**
	 * Check whether this piece is white or not
	 * 
	 * @return	true if this piece is white
	 * 			fals if not
	 */
	public boolean isWhite() { return color == ChessColor.WHITE; }

	/**
	 * Get the location of this piece
	 * 
	 * @return	The location of this piece
	 */
	public Location getPosition() {
		if (row == 0 || col == 0) return null;
		else return new Location(row, col);
	}

	/**
	 * Get the row of this piece
	 * 
	 * @return	The row of this piece
	 */
	public int getRow() { return row; }

	/**
	 * Get the column of this piece
	 * 
	 * @return	The column of this piece
	 */
	public int getCol() { return col; }

	/**
	 * Update the location of this piece
	 * 
	 * @param desRow	The new row
	 * @param desCol	The new column
	 */
	public void updateLocation(int desRow, int desCol) {
		row = desRow;
		col = desCol;
	}

	/**
	 * Get all the valid moves that this piece can make on the 
	 * given board
	 * 
	 * @param board	The board this piece is on
	 * @return		All the valid moves this piece can make
	 */
	public abstract ArrayList<Move> validMoves(Piece[][] board);

	/**
	 * Check whether this move is valid
	 * 
	 * @param board		The board this piece is on
	 * @param desRow	The destination row
	 * @param desCol	The destination column
	 * @return			0 - invalid move
	 * 					1 - valid move
	 * 					2 - en passant
	 * 					3 - castle
	 */
	public int isValidMove(Piece[][] board, int desRow, int desCol) {
		ArrayList<Move> validMoves = validMoves(board);
		for (Move m: validMoves) {
			if (m.getCol() == desCol && m.getRow() == desRow)
				return 1;
		}
		return 0;
	}

	/**
	 * The path of the image of this piece in the directory
	 * 
	 * @return	The path
	 */
	public String getImagePath() {
		return "src/images/" + (isWhite()?"white":"black") + "/" + getPieceName() + ".png";
	}

	/**
	 * Get the type of this piece
	 * 
	 * @return	The type of this piece
	 */
	public abstract String getPieceName();
}

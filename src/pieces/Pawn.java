package pieces;

import java.util.ArrayList;
import enums.ChessColor;
import shared.Move;

/**
 * The Pawn
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public class Pawn extends Piece {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Whether this pawn is en passantable or not
	 */
	private boolean isEnPassant = false;

	/**
	 * Constructor
	 * 
	 * @param white		Whether this piece is white or not
	 * @param r			The row of this piece
	 * @param c			The column of this piece
	 */
	public Pawn(boolean white, int r, int c) { super(white, r, c); }

	/**
	 * Check whether this pawn is en passantable or not
	 * @return	true if this pawn can be captured by en passant
	 * 			falss if not
	 */
	public boolean checkEnPassant() { return isEnPassant; }

	/**
	 * Set this pawn according to the input
	 * @param en	Whether this pawn is en passantable or not
	 */
	public void setEnPassant(boolean en) { isEnPassant = en; }

	/**
	 * Get all the valid moves that this piece can make on the 
	 * given board
	 * 
	 * @param board	The board this piece is on
	 * @return		All the valid moves this piece can make
	 */
	@Override
	public ArrayList<Move> validMoves(Piece[][] board) {
		// Oh god, this was not a good first piece to write.  White and black go different directions.
		ArrayList<Move> moves = new ArrayList<>();

		// We know the pawn isn't on the last square of the board, because if
		//  it was it wouldn't be a pawn anymore.  If we don't implement that,
		//  we'll have to add special handling.
		int yOffset;
		if (isWhite()) yOffset = 1;
		else yOffset = -1;

		int newRow = row + yOffset;
		if (newRow >= 8 || newRow < 0) return moves;

		if (board[row+yOffset][col] == null)
			moves.add(new Move(row, col, row + yOffset, col));

		// If it's first move
		if (isFirstMove && board[row+2*yOffset][col] == null)
			moves.add(new Move(row, col, row+2*yOffset, col));

		if (col > 0) {
			Piece leftDiag = board[row + yOffset][col - 1];
			if (leftDiag != null && !leftDiag.getColor().equals(color))
				moves.add(new Move(row, col, row + yOffset, col - 1));
		}
		if (col < 7) {
			Piece rightDiag = board[row + yOffset][col + 1];
			if (rightDiag != null && !rightDiag.getColor().equals(color))
				moves.add(new Move(row, col, row + yOffset, col + 1));
		}
		return moves;
	}

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
	@Override
	public int isValidMove(Piece[][] board, int desRow, int desCol) {
		int dir;
		if (color.equals(ChessColor.WHITE)) dir = 1;
		else dir = -1;

		// Move one step forward diagonally
		if (Math.abs(desCol - col) == 1 && desRow - row == dir) {
			// Check whether it's en passant or not
			if (board[row][desCol] instanceof Pawn &&
					board[desRow][desCol] == null) {
				Pawn tempP = (Pawn) board[row][desCol];
				if (tempP.checkEnPassant()) return 2;
			}
		}

		// Check for normal moves
		ArrayList<Move> validMoves = validMoves(board);
		for (Move m: validMoves) {
			if (m.getCol() == desCol && m.getRow() == desRow)
				return 1;
		}

		return 0; // invalid move
	}

	/**
	 * Get the type of this piece
	 */
	@Override
	public String getPieceName() {
		return "pawn";
	}
}

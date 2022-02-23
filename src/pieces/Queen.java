package pieces;

import java.util.ArrayList;
import shared.Move;

/**
 * The Queen
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public class Queen extends Piece {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * 
	 * @param white		Whether this piece is white or not
	 * @param r			The row of this piece
	 * @param c			The column of this piece
	 */
	public Queen(boolean white, int r, int c) {super(white, r, c);}

	/**
	 * Get all the valid moves that this piece can make on the 
	 * given board
	 * 
	 * @param board	The board this piece is on
	 * @return		All the valid moves this piece can make
	 */
	@Override
	public ArrayList<Move> validMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();

		boolean[] dirs = new boolean[] {true, true, true, true, true, true, true, true};
		for (int offset = 1; offset < 8; offset++) {
			if (dirs[0] && 0 <= row - offset) {  // Down
				if (board[row - offset][col] == null)
					moves.add(new Move(row, col, row-offset, col));
				else if (!board[row - offset][col].getColor().equals(color)) {
					moves.add(new Move(row, col, row-offset, col));
					dirs[0] = false;
				} else {dirs[0] = false;}
			}
			if (dirs[1] && 0 <= col - offset) {  // Left
				if (board[row][col - offset] == null)
					moves.add(new Move(row, col, row, col-offset));
				else if (!board[row][col - offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row, col-offset));
					dirs[1] = false;
				} else {dirs[1] = false;}
			}
			if (dirs[2] && row + offset < 8) {  // Up
			 	if (board[row + offset][col] == null)
			 		moves.add(new Move(row, col, row+offset, col));
			 	else if (!board[row + offset][col].getColor().equals(color)) {
					moves.add(new Move(row, col, row+offset, col));
					dirs[2] = false;
				} else {dirs[2] = false;}
			}
			if (dirs[3] && col + offset < 8) {  // Right
				if (board[row][col + offset] == null)
					moves.add(new Move(row, col, row, col+offset));
				else if (!board[row][col + offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row, col+offset));
					dirs[3] = false;
				} else {dirs[3] = false;}
			}
			if (dirs[4] && row - offset >= 0 && col - offset >= 0) {  // Down Left
				if (board[row - offset][col - offset] == null)
					moves.add(new Move(row, col, row-offset, col-offset));
				else if (!board[row - offset][col - offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row-offset, col-offset));
					dirs[4] = false;
				} else {dirs[4] = false;}
			}
			if (dirs[5] && row + offset < 8 && col - offset >= 0) {  // Up Left
				if (board[row + offset][col - offset] == null)
					moves.add(new Move(row, col, row+offset, col-offset));
				else if (!board[row + offset][col - offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row+offset, col-offset));
					dirs[5] = false;
				} else {dirs[5] = false;}
			}
			if (dirs[6] && row - offset >= 0 && col + offset < 8) {  // Down Right
			 	if (board[row - offset][col + offset] == null)
			 		moves.add(new Move(row, col, row-offset, col+offset));
			 	else if (!board[row - offset][col + offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row-offset, col+offset));
					dirs[6] = false;
				} else {dirs[6] = false;}
			}
			if (dirs[7] && row + offset < 8 && col + offset < 8) {  // Up Right
				if (board[row + offset][col + offset] == null)
					moves.add(new Move(row, col, row+offset, col+offset));
				else if (!board[row + offset][col + offset].getColor().equals(color)) {
					moves.add(new Move(row, col, row+offset, col+offset));
					dirs[7] = false;
				} else {dirs[7] = false;}
			}
		}

		return moves;
	}

	/**
	 * Get the type of this piece
	 */
	@Override
	public String getPieceName() {
		return "queen";
	}
}

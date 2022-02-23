package pieces;

import java.util.ArrayList;
import shared.Move;

/**
 * The Knight
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public class Knight extends Piece {
	
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
	public Knight(boolean white, int r, int c) {super(white, r, c);}

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

		for (int ro : new int[]{-2, -1, 1, 2}) {
			for (int co : new int[]{-2, -1, 1, 2}) {
				if (Math.abs(ro) == Math.abs(co)) {continue;}

				if (0 <= row + ro && row + ro < 8
						&& 0 <= col + co && col + co < 8) {
					if (board[row + ro][col + co] == null
							|| !board[row + ro][col + co].getColor().equals(color))
						moves.add(new Move(row, col, row+ro, col+co));
				}
			}
		}
		return moves;
	}

	/**
	 * Get the type of this piece
	 */
	@Override
	public String getPieceName() {
		return "knight";
	}
}

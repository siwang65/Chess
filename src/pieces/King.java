package pieces;

import java.util.ArrayList;
import shared.Move;

/**
 * The King
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public class King extends Piece {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * All the opponent's pieces that's checking this king
	 */
	private ArrayList<Piece> checkedBy;

	/**
	 * Constructor
	 * 
	 * @param white		Whether this piece is white or not
	 * @param r			The row of this piece
	 * @param c			The column of this piece
	 */
	public King(boolean white, int r, int c) {
		super(white, r, c);
		checkedBy = new ArrayList<>();
	}

	/**
	 * Check whether this king is checked or not
	 * 
	 * @return	true if this king is checked
	 * 			false if not
	 */
	public boolean isChecked() { return checkedBy.size() != 0; }

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
		ArrayList<String> checkedCell = getCheckedCell(board);

		for (int ro : new int[] {-1, 0, 1}) {
			for (int co : new int[] {-1, 0, 1}) {
				Move currMove = null;
				int tempRow = row + ro;
				int tempCol = col + co;
				if (tempRow < 0 || tempCol < 0 || tempRow >= 8 || tempCol >= 8)
					continue;

				if (board[tempRow][tempCol] == null ||
						!board[tempRow][tempCol].getColor().equals(color))
					currMove = new Move(row, col, tempRow, tempCol);

				if (currMove == null) continue;
				else {
					int row = currMove.getRow();
					int col = currMove.getCol();
					String s = String.valueOf(row) + String.valueOf(col);
					if (checkedCell.contains(s)) continue;
					else moves.add(currMove);
				}
			}
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
		// Check for valid castle
		int xMove = desCol - col;
		int yMove = desRow - row;
		if (Math.abs(xMove) > 2 || Math.abs(yMove) > 1)
			return 0;

		int xDir = -1;
		if (xMove > 0) xDir = 1;

		// Check for castle
		if (Math.abs(xMove) == 2 && yMove == 0) {
			if (col < 3 || col > 4 || board[row][col + xDir] != null ||
					board[row][col + 2 * xDir] != null)
				return 0;
			int tempCol = col + 3 * xDir;
			while (tempCol >= 0 && tempCol < 8) {
				if (board[desRow][tempCol] instanceof Rook) {
					Rook r = (Rook) board[desRow][tempCol];
					if (isFirstMove && r.checkIfFirstMove()
							&& pathSafe(board, xDir)
							&& !isCheckedAfterCastle(board, desRow, desCol))
						return 3;
				}
				tempCol += xDir;
			}
		}
		// Check for normal moves
		ArrayList<Move> validMoves = validMoves(board);
		for (Move m: validMoves) {
			if (m.getCol() == desCol && m.getRow() == desRow)
				return 1;
		}
		return 0;
	}

	/**
	 * We want to check whether the input piece is attacking
	 * the given location on the board
	 * 
	 * @param board		The board this piece is on
	 * @param p			The piece we want to check
	 * @param r			The target row
	 * @param c			The target column
	 * @return			true if the piece is attacking the given locatino
	 * 					false if not
	 */
	private boolean isAttacking(Piece[][] board, Piece p, int r, int c) {
		ArrayList<Move> validMoves = p.validMoves(board);
		for (Move m: validMoves) {
			if (m.getCol() == c && m.getRow() == r)
				return true;
		}
		return false;
	}

	/**
	 * Get all the location that's being checked by the enemy
	 * 
	 * @param board		The board this piece is on
	 * @return			List of location that's checked by the enemy
	 */
	private ArrayList<String> getCheckedCell(Piece[][] board) {
		ArrayList<String> checkedCell = new ArrayList<>();

		for (Piece p : checkedBy) {
			ArrayList<Move> validMoves = p.validMoves(board);
			for (Move m: validMoves) {
				int row = m.getRow();
				int col = m.getCol();
				String s = String.valueOf(row) + String.valueOf(col);
				checkedCell.add(s);
			}
		}
		return checkedCell;
	}

	/**
	 * Iterate through all pieces that's checking the king before the move
	 * and update the list
	 * 
	 * @param board		The board this piece is on
	 */
	public void updateCheckedBy(Piece[][] board) {
		ArrayList<Piece> temp = new ArrayList<>();

		for (Piece[] rows : board) {
			for (Piece p: rows) {
				if ((p == null) || p.getColor().equals(color)) continue;
				if (isAttacking(board, p, row, col))
					temp.add(p);
			}
		}

		checkedBy = temp;
	}

	/**
	 * This new piece is also checking this king
	 * 
	 * @param p	The piece to add
	 */
	public void addToCheckedBy(Piece p) {
		if (!checkedBy.contains(p))
			checkedBy.add(p);
		}

	/**
	 * Check whether the path to castle is save
	 * 
	 * @param board		The board this piece is on
	 * @param xDir		The direction the king moves towards the rook
	 * @return			true if the path towards the rook is not attacked
	 * 					false if otherwise
	 */
	private boolean pathSafe(Piece[][] board, int xDir) {
		for (Piece[] rows : board) {
			for (Piece p: rows) {
				if (p == null) continue;
				else if (p.getColor().equals(color)) continue;
				else if (isAttacking(board, p, row, col + xDir)
							|| isAttacking(board, p, row, col + 2*xDir))
					return false;
			}
		}
		return true;
	}

	/**
	 * Check whether the king is checked after castle
	 * 
	 * @param board		The board this piece is on
	 * @param desRow	The destination row
	 * @param desCol	The destination column
	 * @return			true if the king is checked at the new location
	 * 					false if not
	 */
	private boolean isCheckedAfterCastle(Piece[][] board, int desRow, int desCol) {
		for (Piece[] rows : board) {
			for (Piece p: rows) {
				if (p == null) continue;
				else if (p.getColor().equals(color)) continue;
				else if (isAttacking(board, p, row, col))
					return true;
			}
		}
		return false;
	}

	/**
	 * Get the type of this piece
	 */
	@Override
	public String getPieceName() {
		return "king";
	}
}


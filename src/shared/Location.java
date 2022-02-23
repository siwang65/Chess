package shared;

import java.io.Serializable;

/**
 * Hold the location of a piece
 * 
 * @author Chase Hult
 * @author Siwen Wang
 */
public class Location implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8923684733156715384L;
	
	/**
	 * The row on the board
	 */
	private int row;
	
	/**
	 * The column on the board
	 */
	private int col;
	
	/**
	 * Constructor
	 * 
	 * @param row	The row on the board
	 * @param col	The column on the board
	 */
	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Get the row of this location
	 * 
	 * @return	The row
	 */
	public int getRow() { return row; }
	
	/**
	 * Get the column of this location
	 * 
	 * @return	The column
	 */
	public int getCol() { return col; }
}

package shared;

/**
 * The move, the message that passed from observable to observer
 * 
 * @author Chase Hult
 * @author Siwen Wang
 */
public class Move extends Location {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2959471318504392287L;

	/**
	 * The previous row
	 */
	private int oldRow;
	
	/**
	 * The previous column
	 */
	private int oldCol;
	
	/**
	 * The new row
	 */
	private int newRow;
	
	/**
	 * The new column
	 */
	private int newCol;
	
	/**
	 * Whether this move is a promotion
	 */
	private boolean isPromotion = false;
	
	/**
	 * The type the pawn would promote to
	 */
	private String promotedType = null;
	
	/**
	 * Constructor
	 * 
	 * @param oldRow	The previous row
	 * @param oldCol	The previous column
	 * @param row		The new row
	 * @param col		The new column
	 */
	public Move(int oldRow, int oldCol, int row, int col) {
		super(row, col);
		this.oldRow = oldRow;
		this.oldCol = oldCol;
		this.newCol= col;
		this.newRow= row;
	}
	
	/**
	 * Constructor
	 * 
	 * @param oldLoc	Old location
	 * @param newRow	New row
	 * @param newCol	New column
	 */
	public Move(Location oldLoc, int newRow, int newCol) {
		this(oldLoc.getRow(), oldLoc.getCol(), newRow, newCol);
	}
	
	/**
	 * Constructor
	 * 
	 * @param oldLoc	Old location
	 * @param newLoc	New location
	 */
	public Move(Location oldLoc, Location newLoc) {
		this(oldLoc, newLoc.getRow(), newLoc.getCol());
	}

	/**
	 * Getter for oldRow
	 * @return	oldRow
	 */
	public int oldRow() {return oldRow;}
	
	/**
	 * Getter for oldCol
	 * 
	 * @return	oldCol
	 */
	public int oldCol() {return oldCol;}
	
	/**
	 * Getter for newRow
	 * 
	 * @return	newRow
	 */
	public int newRow() {return newRow;}
	
	/**
	 * Getter for newCol
	 * 
	 * @return	newCol
	 */
	public int newCol() {return newCol;}

	/**
	 * Getter for isPromotion
	 * 
	 * @return	isPromotion
	 */
	public boolean isPromotion() { return isPromotion; }
	
	/**
	 * Setter for both isPromotion and promotedType
	 * 
	 * @param b		Whether this move is promotion
	 * @param type	The type of piece the pawn is promoting to
	 */
	public void setPromotion(boolean b, String type) { 
		isPromotion = b; 
		promotedType = type;
	}
	
	/**
	 * Getter for promotedType
	 * 
	 * @return	promotedType
	 */
	public String getPromotedType() { return promotedType; }
	
	/**
	 * Override the equals method
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Move) {
			Move o = (Move) other;
			return this.getCol() == o.getCol() && this.getRow() == o.getRow() 
					&& this.getCol() == o.getCol() && this.getRow() == o.getRow()
					&& this.isPromotion == o.isPromotion()
					&& this.getPromotedType().equals(o.getPromotedType());
		} else {return false;}
	}
}

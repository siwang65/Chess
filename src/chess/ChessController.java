package chess;

import enums.ChessColor;
import pieces.Piece;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import shared.Move;
import javafx.application.Platform;

/**
 * Controller of the chess game
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
public class ChessController {

	/**
	 * The model this controller holds
	 */
	private ChessModel model;
	
	/**
	 * The color of this controller
	 */
	private ChessColor color;
	
	/**
	 * Whether it's playing as computer or not
	 */
	private boolean AI = false;
	
	/**
	 * The socket for the connection
	 */
	private Socket connection;
	
	/**
	 * Input stream wrapper
	 */
	private ObjectInputStream ois;
	
	/**
	 * Out stream wrapper
	 */
	private ObjectOutputStream oos;

	/**
	 * Constructor
	 * 
	 * @param model		The model it holds
	 */
	public ChessController(ChessModel model) { this.model = model; }
	
	/**
	 * Check whether the game is over
	 * 
	 * @return		true if the game is over and someone won
	 * 				false if else
	 */
	public boolean isGameOver() { return model.isGameOver(); }

	/**
	 * Check whether this player is checked by the opponent
	 * 
	 * @return	true if it's checked
	 * 			false if not
	 */
	public boolean isChecked() { return model.isChecked(color); }
	
	/**
	 * Check whether this controller is played by AI
	 * 
	 * @return		true if it's playing as AI
	 * 				false if it's playing by human
	 */
	public boolean isAI() { return AI; }
	
	/**
	 * Set this controller as AI playing
	 */
	public void setAsAI() { AI = true; }
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Piece getPieceAt(int row, int col) { return model.getPiece(row, col); }
	
	/**
	 * Make a move and send the thread message
	 * 
	 * @param currRow
	 * @param currCol
	 * @param desRow
	 * @param desCol
	 */
	public void makeAMove(int currRow, int currCol, int desRow, int desCol) {
		makeAMove(currRow, currCol, desRow, desCol, "controller");
	}
	
	public void makeAMove(int currRow, int currCol, int desRow, int desCol, String type) {
		Move message = model.makeAMove(currRow, currCol, desRow, desCol, type);
		if (message != null && oos != null) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}

	/**
	 * This is mainly for the test suite
	 * Make a move
	 * 
	 * @param color			The color of this move
	 * @param from			The location of the piece
	 * @param to			The destination
	 */
	public void makeAMove(String color, String from, String to) {
		int currCol = from.charAt(0) - 97;
		int desCol = to.charAt(0) - 97;
		int currRow = Character.getNumericValue(from.charAt(1)) - 1;
		int desRow = Character.getNumericValue(to.charAt(1)) - 1;
		makeAMove(currRow, currCol, desRow, desCol);
	}
	
	/**
	 * Promote the pawn
	 * 
	 * @param desRow	The row of the pawn	we want to promote
	 * @param desCol	The column of the pawn we want to promote
	 * @param type		The piece type the player want to promote to
	 */
	public void promotion(int desRow, int desCol, String type) {
		Piece piece = model.promotePawn(desRow, desCol, type);
		if (oos != null) {
			try {
				oos.writeObject(piece);
				oos.flush();
			} catch (IOException e) {e.printStackTrace();}
		}
	}

	/**
	 * Make a random move for the AI
	 */
	public void makeRandomMove() {
		Move move = model.getRandomMove(color);
		makeAMove(move.oldRow(), move.oldCol(), move.getRow(), move.getCol());
	}
	
	/**
	 * Save the game into a file
	 * 
	 * @param fn	File name of the save file
	 */
	public void saveGame(String filename) { model.saveGame(filename); }
	
	/**
	 * Load a saved game
	 * 
	 * @param fn	File name of the save file
	 */
	public void loadGame(String filename) {
		model.loadGame(filename);
		try {
			oos.writeObject(model);
			oos.flush();
		} catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * Check whether this controller is the winner of the game
	 * 
	 * @return	true if this controller win
	 * 			false if not
	 */
	public boolean isWinner() {
		if (model.isWinner(color)) return true;
		else return false;
	}

	/**
	 * Getter for color
	 * 
	 * @return	color
	 */
	public ChessColor getColor() { return color; }
	
	/**
	 * Setter for color
	 * 
	 * @param white		whether it's white or not
	 */
	public void setColor(boolean white) {
		if (white) color = ChessColor.WHITE;
		else color = ChessColor.BLACK;
	}

	/**
	 * Initialize the instance as server
	 * 
	 * @param port		The port we're connecting to
	 */
	public void startServer(String port){
		color = ChessColor.WHITE;
		model.setMyTurn(true);
		
		try{
			ServerSocket server= new ServerSocket(Integer.parseInt(port));
			connection = server.accept();
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());
			
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							Object data = ois.readObject();
							if (data instanceof Move) {
								Move move = (Move) data;
								Platform.runLater(() -> {
									makeAMove(move.oldRow(), move.oldCol(), move.newRow(), move.newCol(), "server");
								});
							} else if (data instanceof ChessModel) {
								ChessModel newModel = (ChessModel) data;
								Platform.runLater(() -> {model.loadGame(newModel);});
							} else if (data instanceof Piece) {
								Piece piece = (Piece) data;
								Platform.runLater(() -> {model.addPiece(piece);});
							} else {
								System.err.println("Invalid data type.");
							}

						}
					} catch (Exception e) {
						System.err.println("Something went wrong with the network! " + e.getMessage());
					}
				}
			};
			new Thread(r).start();
			server.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Initialize the instance as client
	 * 
	 * @param ip		The IP address we are connecting to
	 * @param port		The port we're connecting to
	 */
	public void startClient(String ip, String port) {
		try{
			connection = new Socket(ip, Integer.parseInt(port));
			color = ChessColor.BLACK;
			model.setMyTurn(false);
			
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							Object data = ois.readObject();
							if (data instanceof Move) {
								Move move = (Move) data;
								Platform.runLater(() -> {
									makeAMove(move.oldRow(), move.oldCol(), move.newRow(), move.newCol(), "client");
								});
							} else if (data instanceof ChessModel) {
								ChessModel newModel = (ChessModel) data;
								Platform.runLater(() -> {model.loadGame(newModel);});
							} else if (data instanceof Piece) {
								Piece piece = (Piece) data;
								Platform.runLater(() -> {model.addPiece(piece);});
							} else {
								System.err.println("Invalid data type.");
							}
						}
					} catch (Exception e) {System.err.println(e.getMessage());}
				}
			};
			new Thread(r).start();			
		} catch(IOException e){
			System.err.println("Something went wrong with the network! " + e.getMessage());
		}
	}
}

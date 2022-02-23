/**
 * This class launches the Chess GUI to allow users to play the game.
 *
 * @author Chase Hult
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 * @author Siwen Wang
 */
import chess.ChessView;
import javafx.application.Application;

/**
 * Main class for the chess game
 * 
 * @author Chase Hult
 */
public class Chess {
	
	/**
	 * Main method, starting the game
	 * 
	 * @param args	input arguments
	 */
	public static void main(String[] args) {
		Application.launch(ChessView.class, args);
	}
}

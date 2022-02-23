import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessController;
import chess.ChessModel;

/**
 * Testing everything that's not gui or thread
 * Aiming >90% statement coverage
 * 
 * @author Siwen Wang
 */
public class ChessModelTesting {

	/**
	 * Simple simulation of a game
	 */
	@Test
	void test1() {
		ChessModel model1 = new ChessModel();
		ChessController controller1 = new ChessController(model1);
		controller1.setColor(true); // default controller is white

		ChessModel model2 = new ChessModel();
		ChessController controller2 = new ChessController(model2);
		controller2.setColor(false); // default controller is black

		controller1.makeAMove("WHITE", "e2", "e4");
		controller2.makeAMove("WHITE", "e2", "e4");
		controller1.makeAMove("BLACK", "e7", "e5");
		controller2.makeAMove("BLACK", "e7", "e5");
		controller1.makeAMove("WHITE", "d2", "d3");
		controller2.makeAMove("WHITE", "d2", "d3");
		controller1.makeAMove("BLACK", "d7", "d5");
		controller2.makeAMove("BLACK", "d7", "d5");
		controller1.makeAMove("WHITE", "g1", "f3");
		controller2.makeAMove("WHITE", "g1", "f3");
		controller1.makeAMove("BLACK", "b8", "c6");
		assertFalse(controller2.isChecked());
		controller2.makeAMove("BLACK", "b8", "c6");
		controller1.makeAMove("WHITE", "c1", "g5");
		assertFalse(controller2.isGameOver());
		controller2.makeAMove("WHITE", "c1", "g5");
		controller1.makeAMove("BLACK", "f8", "e7");
		controller2.makeAMove("BLACK", "f8", "e7");
		assertFalse(controller1.isGameOver());
		assertFalse(controller1.isChecked());
		controller1.makeAMove("WHITE", "h2", "h4");
		controller2.makeAMove("WHITE", "h2", "h4");
		controller1.makeAMove("BLACK", "c8", "g4");
		controller2.makeAMove("BLACK", "c8", "g4");
		controller1.makeAMove("WHITE", "b2", "b4");
		controller2.makeAMove("WHITE", "b2", "b4");
		controller1.makeAMove("BLACK", "d5", "e4");
		controller2.makeAMove("BLACK", "d5", "e4");
		assertFalse(controller1.isWinner());
		controller1.makeAMove("WHITE", "b1", "a3");
		controller2.makeAMove("WHITE", "b1", "a3");
		controller1.makeAMove("BLACK", "e7", "b4");
		controller2.makeAMove("BLACK", "e7", "b4");
		assertTrue(controller1.isChecked());
		assertFalse(controller2.isChecked());
		controller1.makeAMove("WHITE", "d1", "c1");
		controller2.makeAMove("WHITE", "d1", "c1");
		controller1.makeAMove("BLACK", "b4", "e1");
		controller2.makeAMove("BLACK", "b4", "e1");
		assertTrue(controller1.isGameOver());
		assertTrue(controller2.isGameOver());
		assertFalse(controller1.isWinner());
		assertTrue(controller2.isWinner());
	}

	/**
	 * Testing en passant
	 */
	@Test
	void test_en_passant() {
		ChessModel model1 = new ChessModel();
		ChessController controller1 = new ChessController(model1);
		controller1.setColor(true); // default controller is white

		ChessModel model2 = new ChessModel();
		ChessController controller2 = new ChessController(model2);
		controller2.setColor(false); // default controller is black

		controller1.makeAMove("WHITE", "e2", "e4");
		controller2.makeAMove("WHITE", "e2", "e4");
		controller1.makeAMove("BLACK", "a7", "a6");
		controller2.makeAMove("BLACK", "a7", "a6");
		controller1.makeAMove("WHITE", "e4", "e5");
		controller2.makeAMove("WHITE", "e4", "e5");
		controller1.makeAMove("BLACK", "d7", "d5");
		controller2.makeAMove("BLACK", "d7", "d5");
		// Now black Pawn at d5 can be en passant by white Pawn at e5
		assertFalse(controller1.isChecked());
		assertFalse(controller2.isChecked());
		controller1.makeAMove("WHITE", "e5", "d6");
		controller2.makeAMove("WHITE", "e5", "d6");
	}

	/**
	 * testing castle
	 */
	@Test
	void test_castle() {
		ChessModel model = new ChessModel();
		ChessController controller = new ChessController(model);
		controller.setColor(true); // default controller is white

		controller.makeAMove("WHITE", "e2", "e4");
		controller.makeAMove("BLACK", "c7", "c5");
		controller.makeAMove("WHITE", "g1", "f3");
		controller.makeAMove("BLACK", "d7", "d6");
		controller.makeAMove("WHITE", "f1", "b5");
		controller.makeAMove("BLACK", "b8", "c6");
		// castle
		controller.makeAMove("WHITE", "e1", "g1");
		controller.makeAMove("BLACK", "c8", "d7");
		controller.makeAMove("WHITE", "c2", "c4");
		controller.makeAMove("BLACK", "e7", "e5");
		controller.makeAMove("WHITE", "d1", "a4");
		controller.makeAMove("BLACK", "g8", "f6");
		controller.makeAMove("WHITE", "d2", "d3");
		controller.makeAMove("BLACK", "f8", "e7");
		controller.makeAMove("WHITE", "h2", "h4");
		// castle
		controller.makeAMove("BLACK", "e8", "g8");
		controller.makeAMove("WHITE", "c1", "g5");
		controller.makeAMove("BLACK", "h7", "h6");
		controller.makeAMove("WHITE", "g5", "e3");
		controller.makeAMove("BLACK", "f6", "g4");
		controller.makeAMove("WHITE", "e3", "d2");
		controller.makeAMove("BLACK", "f7", "f5");
		controller.makeAMove("WHITE", "d2", "c3");
		controller.makeAMove("BLACK", "a7", "a6");
		controller.makeAMove("WHITE", "b5", "c6");
		controller.makeAMove("BLACK", "d7", "c6");
		controller.makeAMove("WHITE", "a4", "b3");
		controller.makeAMove("BLACK", "f5", "e4");
		controller.makeAMove("WHITE", "d3", "e4");
		controller.makeAMove("BLACK", "e7", "h4");
		controller.makeAMove("WHITE", "f3", "h4");
		controller.makeAMove("BLACK", "d8", "h4");
		controller.makeAMove("WHITE", "g2", "g3");
		controller.makeAMove("BLACK", "h4", "h2");
		assertTrue(controller.isChecked());
		assertFalse(controller.isGameOver());
		assertFalse(controller.isWinner());
		controller.makeAMove("WHITE", "g1", "h2");
		assertTrue(controller.isChecked());
		controller.makeAMove("BLACK", "g4", "h2");
		assertTrue(controller.isGameOver());
		assertFalse(controller.isWinner());
	}

	/**
	 * A a little bit longer game
	 * Test promotion
	 */
	@Test
	void test2() {
		ChessModel model1 = new ChessModel();
		ChessController controller1 = new ChessController(model1);
		controller1.setColor(true); // default controller is white

		ChessModel model2 = new ChessModel();
		ChessController controller2 = new ChessController(model2);
		controller2.setColor(false); // default controller is black

		makeMoves(controller1, controller2, "WHITE", "e2", "e4");
		makeMoves(controller1, controller2, "BLACK", "e7", "e5");
		makeMoves(controller1, controller2, "WHITE", "d2", "d3");
		makeMoves(controller1, controller2, "BLACK", "d7", "d6");
		makeMoves(controller1, controller2, "WHITE", "g1", "f3");
		makeMoves(controller1, controller2, "BLACK", "f7", "f6");
		makeMoves(controller1, controller2, "WHITE", "b1", "c3");
		makeMoves(controller1, controller2, "BLACK", "c8", "g4");
		makeMoves(controller1, controller2, "WHITE", "a2", "a4");
		makeMoves(controller1, controller2, "BLACK", "b8", "a6");
		makeMoves(controller1, controller2, "WHITE", "a1", "a3");
		makeMoves(controller1, controller2, "BLACK", "d8", "e7");
		makeMoves(controller1, controller2, "WHITE", "a4", "a5");
		makeMoves(controller1, controller2, "BLACK", "a8", "b8");
		makeMoves(controller1, controller2, "WHITE", "c3", "b5");
		makeMoves(controller1, controller2, "BLACK", "b8", "a8");
		makeMoves(controller1, controller2, "WHITE", "a3", "c3");
		makeMoves(controller1, controller2, "BLACK", "e7", "f7");
		assertFalse(controller1.isChecked());
		assertFalse(controller2.isChecked());
		makeMoves(controller1, controller2, "WHITE", "b5", "c7");
		assertFalse(controller1.isChecked());
		assertTrue(controller2.isChecked());
		assertFalse(controller1.isGameOver());
		makeMoves(controller1, controller2, "BLACK", "e8", "d8");
		assertFalse(controller2.isChecked());

		makeMoves(controller1, controller2, "WHITE", "h2", "h3");
		makeMoves(controller1, controller2, "BLACK", "g4", "f3");
		makeMoves(controller1, controller2, "WHITE", "g2", "f3");
		makeMoves(controller1, controller2, "BLACK", "f7", "e8");
		makeMoves(controller1, controller2, "WHITE", "c7", "e8");
		makeMoves(controller1, controller2, "BLACK", "a6", "c7");
		makeMoves(controller1, controller2, "WHITE", "e8", "c7");
		makeMoves(controller1, controller2, "BLACK", "d8", "d7");
		makeMoves(controller1, controller2, "WHITE", "h1", "g1");
		makeMoves(controller1, controller2, "BLACK", "a8", "c8");
		makeMoves(controller1, controller2, "WHITE", "d3", "d4");
		makeMoves(controller1, controller2, "BLACK", "g8", "e7");
		makeMoves(controller1, controller2, "WHITE", "d4", "e5");
		makeMoves(controller1, controller2, "BLACK", "f6", "e5");
		makeMoves(controller1, controller2, "WHITE", "f1", "b5");
		makeMoves(controller1, controller2, "BLACK", "e7", "c6");
		makeMoves(controller1, controller2, "WHITE", "a5", "a6");
		makeMoves(controller1, controller2, "BLACK", "c8", "c7");
		makeMoves(controller1, controller2, "WHITE", "d1", "d5");
		makeMoves(controller1, controller2, "BLACK", "d7", "e8");
		assertFalse(controller1.isChecked());
		assertFalse(controller2.isChecked());
		assertFalse(controller1.isGameOver());
		assertFalse(controller2.isWinner());

		makeMoves(controller1, controller2, "WHITE", "b5", "a4");
		makeMoves(controller1, controller2, "BLACK", "f8", "e7");
		makeMoves(controller1, controller2, "WHITE", "d5", "b5");
		makeMoves(controller1, controller2, "BLACK", "h7", "h6");
		makeMoves(controller1, controller2, "WHITE", "a6", "b7");
		makeMoves(controller1, controller2, "BLACK", "e8", "d7");
		// promote
		System.out.println("Promote this Pawn to Queen");
		makeMoves(controller1, controller2, "WHITE", "b7", "b8");
		makeMoves(controller1, controller2, "BLACK", "h8", "b8");
		makeMoves(controller1, controller2, "WHITE", "b5", "b8");
		makeMoves(controller1, controller2, "BLACK", "e7", "f6");
		makeMoves(controller1, controller2, "WHITE", "f3", "f4");
		makeMoves(controller1, controller2, "BLACK", "e5", "f4");
		makeMoves(controller1, controller2, "WHITE", "c1", "f4");
		makeMoves(controller1, controller2, "BLACK", "a7", "a5");
		makeMoves(controller1, controller2, "WHITE", "c3", "d3");
		makeMoves(controller1, controller2, "BLACK", "f6", "e7");
		makeMoves(controller1, controller2, "WHITE", "g1", "g7");
		makeMoves(controller1, controller2, "BLACK", "d7", "e6");
		makeMoves(controller1, controller2, "WHITE", "b8", "b3");
		assertTrue(controller2.isChecked());
		assertFalse(controller1.isChecked());
		makeMoves(controller1, controller2, "BLACK", "e6", "d7");
		assertFalse(controller2.isChecked());

		makeMoves(controller1, controller2, "WHITE", "e4", "e5");
		makeMoves(controller1, controller2, "BLACK", "d7", "c8");
		makeMoves(controller1, controller2, "WHITE", "e5", "d6");
		makeMoves(controller1, controller2, "BLACK", "c6", "b4");
		makeMoves(controller1, controller2, "WHITE", "d3", "d2");
		makeMoves(controller1, controller2, "BLACK", "e7", "d6");
		makeMoves(controller1, controller2, "WHITE", "f4", "d6");
		makeMoves(controller1, controller2, "BLACK", "c7", "g7");
		makeMoves(controller1, controller2, "WHITE", "d6", "b4");
		makeMoves(controller1, controller2, "BLACK", "g7", "g1");
		assertTrue(controller1.isChecked());
		assertFalse(controller2.isChecked());
		makeMoves(controller1, controller2, "WHITE", "e1", "e2");
		assertFalse(controller1.isChecked());
		makeMoves(controller1, controller2, "BLACK", "g1", "e1");
		assertTrue(controller1.isChecked());
		makeMoves(controller1, controller2, "WHITE", "e2", "e1");
		assertFalse(controller2.isChecked());
		makeMoves(controller1, controller2, "BLACK", "c8", "b7");
		makeMoves(controller1, controller2, "WHITE", "b4", "a5");
		makeMoves(controller1, controller2, "BLACK", "b7", "a8");
		makeMoves(controller1, controller2, "WHITE", "a5", "b6");
		makeMoves(controller1, controller2, "BLACK", "a8", "b8");
		makeMoves(controller1, controller2, "WHITE", "a4", "c6");
		makeMoves(controller1, controller2, "BLACK", "h6", "h5");
		// Check Mate
		makeMoves(controller1, controller2, "WHITE", "d2", "d8");
		assertTrue(controller2.isChecked());
		assertFalse(controller1.isChecked());

		makeMoves(controller1, controller2, "BLACK", "b8", "b7");
		makeMoves(controller1, controller2, "WHITE", "c6", "b7");
		assertTrue(controller1.isGameOver());
		assertTrue(controller1.isWinner());
		assertFalse(controller2.isWinner());
		model1.saveGame("test");
	}

	/**
	 * Helper method, make the same move at two controller
	 * 
	 * @param controller1	The first controller
	 * @param controller2	The second controller
	 * @param color			The color of this move
	 * @param from			The location of the piece
	 * @param to			The destination
	 */
	private void makeMoves(ChessController controller1, ChessController controller2,
							String color, String from, String to) {
		controller1.makeAMove(color, from, to);
		controller2.makeAMove(color, from, to);
	}

}

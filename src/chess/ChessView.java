package chess;

import java.io.FileInputStream;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import pieces.Piece;
import shared.Move;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;

/**
 * The view of the chess game
 * 
 * @author Chase Hult
 * @author Siwen Wang
 * @author Valeria Garcia
 * @author Fatimah Aldhamen
 */
@SuppressWarnings("deprecation")
public class ChessView extends Application implements Observer {
	
	/**
	 * The controller it holds
	 */
	private ChessController controller;
	
	/**
	 * The model the controller holds
	 */
	private ChessModel model;
	
	/**
	 * The stage we display
	 */
	private Stage stage;

	/**
	 * Whether we can click on the window or not
	 */
	private boolean canClick = false;
	
	/**
	 * Is there a piece selected by the player or not
	 */
	private boolean clicked = false;
	
	/**
	 * The piece selected by the player
	 */
	private ImageView selectedPiece;
	
	/**
	 * The name of the save file
	 */
	private static final String saveFileName = "saved";

	/**
	 * Main
	 * @param args	input arguments
	 */
	public static void main(String[] args) { launch(args); }
	
	/**
	 * Make an AI movement
	 */
	private void AIMove() {
		if (!model.isMyTurn()) return;
		controller.makeRandomMove(); 
	}
	
	/**
	 * Update the circle that have been filled
	 */
	@Override
	public void update(Observable o, Object arg){
		try {
			generateBoard();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(controller.isGameOver()) {
			canClick = false;
			if (controller.isWinner()) {
				Alert alert= new Alert(AlertType.INFORMATION, "Congratulations! You win!");
				alert.showAndWait();
			} else {
				Alert alert= new Alert(AlertType.INFORMATION, "You lose!");
				alert.showAndWait();
			}
			return;
		}
		
		if (arg instanceof Move) {
			Move msg = (Move) arg;
			if (msg.isPromotion() && msg.getPromotedType().equals("c")){
				Alert alert= new Alert(AlertType.INFORMATION, "Congratulations! You can upgrade your pawn!");
				alert.showAndWait().ifPresent(response -> {
					if (response==ButtonType.OK){
						String type;
						while (true) {
							TextInputDialog txt= new TextInputDialog("");
							txt.setHeaderText("Type in one: Queen, Rook, Bishop, Knight");
							txt.showAndWait();
							type= txt.getEditor().getText();
							
							if (type.equals("Queen") || type.equals("Rook")
									|| type.equals("Bishop") || type.equals("Knight")) 
								break;
							else {
								Alert err = new Alert(AlertType.ERROR, "You must promote your pawn to Queen, Rook, Bishop, or Knight!");
								err.showAndWait();
							}
						}
						controller.promotion(msg.getRow(), msg.getCol(), type);
					}
				});
			} 
			
			if (msg.isPromotion()) return;
			
			if (model.isMyTurn()) {
				model.setMyTurn(false);
				canClick = false;
			} else {
				model.setMyTurn(true);
				canClick = true;
			}
			
			if (controller.isAI()) {
				canClick = false;
				if (model.isMyTurn()) AIMove();
			}
		} else if (arg instanceof ChessModel) {
			
		} else if (arg instanceof Piece) {
			
		} else {
			System.err.println("Invalid update arg type");
		}
	}
	
	/**
	 * Start the game
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		selectedPiece = null;
		stage = primaryStage;
		stage.setTitle("Chess");
		model = new ChessModel();
		model.addObserver(this);
		controller = new ChessController(model);
		generateBoard();
		stage.show();
	}

	/**
	 * Initialize the board
	 * 
	 * @throws Exception
	 */
	private void generateBoard() throws Exception {
		BorderPane root= new BorderPane();
		// row index
		VBox vbox= new VBox(33);
		vbox.setPrefWidth(15);
		vbox.setAlignment(Pos.CENTER);
		for (int i=8; i>0; i--){
			String str= Integer.toString(i);
			Label label = new Label(str);
			label.setAlignment(Pos.CENTER);
			vbox.getChildren().add(label);
		}
		root.setLeft(vbox);
		// column index
		String letters= "abcdefgh";
		HBox hbox= new HBox(45);
		hbox.setFillHeight(true);
		hbox.setAlignment(Pos.CENTER);
		char[] chars= letters.toCharArray();
		for (char ch: chars){
			hbox.getChildren().add(new Label(String.valueOf(ch)));
		}
		root.setBottom(hbox);
		// menu
		MenuBar menuBar= new MenuBar();
		Menu menu= new Menu("File");
		MenuItem newGame= new MenuItem("New Game");
		MenuItem load= new MenuItem("Load Game");
		load.setOnAction((event) -> {
			controller.loadGame(saveFileName);
		});
		
		MenuItem save= new MenuItem("Save Game");
		save.setOnAction((event) -> {
			controller.saveGame(saveFileName);
			Alert alert = new Alert(AlertType.INFORMATION, "Game saved successfully.");
			alert.showAndWait();
			
		});
		makeMenu(newGame);
		menu.getItems().add(newGame);
		menu.getItems().add(load);
		menu.getItems().add(save);
		menuBar.getMenus().add(menu);
		root.setTop(menuBar);
		
		// draw the board
		GridPane board = new GridPane();
		Color[] squareColors = new Color[] {Color.WHITE, Color.WHEAT};
		for (int r = 0; r < 8; r++){
			for (int c = 0; c < 8; c++){
				int modelRow = 7 - r; // the corresponding row index in model
				Piece p = controller.getPieceAt(modelRow, c);
				Rectangle item = new Rectangle(50, 50, squareColors[(r+c)%2]);
				board.add(item, c, r);
				
				if (p != null) {
					Image image = new Image(new FileInputStream(p.getImagePath()));
					ImageView iv = new ImageView(image);
					iv.setFitHeight(50);
					iv.setFitWidth(50);
					board.add(iv, c, r);
					
					iv.setOnMouseClicked((event) -> {
						if (!canClick) return;
						if (!clicked && selectedPiece == null) {
							if (!p.getColor().equals(controller.getColor())) return;
				    		clicked = true;
				    		selectedPiece = iv;
				    	} else if (clicked && selectedPiece.equals(iv)) {
				    		clicked = false;
				    		selectedPiece = null;
				    	} else if (clicked && selectedPiece != null) {
							int desRow = 7 - GridPane.getRowIndex(iv);
							int desCol = GridPane.getColumnIndex(iv);
							int currRow = 7 - GridPane.getRowIndex(selectedPiece);
							int currCol = GridPane.getColumnIndex(selectedPiece);
							if (!controller.getPieceAt(currRow, currCol).getColor().equals(
									controller.getPieceAt(desRow, desCol).getColor()))
								controller.makeAMove(currRow, currCol, desRow, desCol);
							clicked = false;
							selectedPiece = null;
				    	}
					});
				}
				
				item.setOnMouseClicked((evnet) -> {
					if (!canClick) return;
					if (!clicked || selectedPiece == null) return;
					int desRow = 7 - GridPane.getRowIndex(item);
					int desCol = GridPane.getColumnIndex(item);
					int currRow = 7 - GridPane.getRowIndex(selectedPiece);
					int currCol = GridPane.getColumnIndex(selectedPiece);
					controller.makeAMove(currRow, currCol, desRow, desCol);
					clicked = false;
					selectedPiece = null;
				});
			}
		}
		
		root.setCenter(board);
		Scene scene = new Scene(root, 433, 443);
		stage.setScene(scene);
	}

	/**
	 * Make the menu
	 * 
	 * @param newGame	The new game button
	 */
	public void makeMenu(MenuItem newGame){
		newGame.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent actionEvent) {
		    	Dialog<ButtonType> dialog= new Dialog<>();
		    	dialog.setTitle("Network Setup");
		    	ButtonType ok= new ButtonType("OK", ButtonData.OK_DONE);
		    	ButtonType cancel= new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		    	Label lbl= new Label("Create:");
		    	RadioButton server= new RadioButton("Server");
		    	server.setSelected(true);
		    	RadioButton client= new RadioButton("Client");
		    	ToggleGroup group= new ToggleGroup();
		    	server.setToggleGroup(group);
		    	client.setToggleGroup(group);
		    	HBox h= new HBox(lbl, server, client);
		    	
		    	Label lbl2= new Label("Play as:");
		    	RadioButton human= new RadioButton("Human");
		    	human.setSelected(true);
		    	RadioButton computer= new RadioButton("Computer");
		    	ToggleGroup group2= new ToggleGroup();
		    	human.setToggleGroup(group2);
		    	computer.setToggleGroup(group2);
		    	HBox h2= new HBox(lbl2, human, computer);
		    	
		    	Label lbl3= new Label("Server");
		    	Label lbl4= new Label("Port");
		    	TextField ip= new TextField("localhost");
		    	TextField port= new TextField("4000");
		    	HBox h3= new HBox(lbl3, ip, lbl4, port);
		    	
		    	GridPane grid= new GridPane();
		    	grid.add(h, 1, 1);
		    	grid.add(h2, 1, 2);
		    	grid.add(h3, 1, 3);
		    	
		    	dialog.getDialogPane().setContent(grid);
		    	dialog.getDialogPane().getButtonTypes().add(ok);
		    	dialog.getDialogPane().getButtonTypes().add(cancel);
		    	dialog.showAndWait().ifPresent(res -> {
		    	    if (res.equals(ok)) {
		    	    	try {
		    	    		 start(stage);
		    	    		 if (server.isSelected()) {
		    	    			 controller.startServer(port.getText());
		    	    			 canClick = true;
		    	    		 } else {
		    	    			 controller.startClient(ip.getText(), port.getText());
		    	    			 canClick = false;
		    	    		 }
		    	    		 if (computer.isSelected()) {
		    	    			 controller.setAsAI();
		    	    			 AIMove();
		    	    		 }
		    	    	} catch (Exception e) {e.printStackTrace();}
		    	    }
		    	});
		    }
		});
	}
}

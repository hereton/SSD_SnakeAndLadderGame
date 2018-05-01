package application;


import game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class BoardUIController {

	@FXML
	private Pane pane;
	@FXML
	private Button rollButton;
	@FXML
	private Label rolledNumLabel;
	@FXML
	private AnchorPane player1;
	@FXML
	private AnchorPane player2;
	@FXML
	private AnchorPane player3;
	@FXML
	private AnchorPane player4;
	
	private Game game;
	
	public BoardUIController() {
		this.game = new Game();
	}
	

	@FXML
	public void initialize() {

//		GridPane gp = new GridPane();
//		for (int row = 0; row < 10; row++) {
//			for (int col = 0; col < 10; col++) {
//
//				VBox hBox = new VBox();
//				hBox.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" + "-fx-border-radius:2,2,2,2;"
//						+ "-fx-border-color: blue;");
//
//				if (row == 0 && col == 0) {
//					for (int n = 0; n < players.length; n++) {
//						player = new Label("player " + (n + 1));
//						hBox.getChildren().add(player);
//					}
//
//				} else {
//					hBox.getChildren().add(new Label(row + "-" + col));
//					hBox.getChildren().add(new Label((row + 1) + "-" + col));
//				}
//
//				gp.add(hBox, col, row);
//			}
//		}
//
//		pane.getChildren().add(gp);
		
	}

	public void handleRollButton(ActionEvent e) {
		
	}

	public void setPlayer(int numberPlayer) {
		for(int i = 0 ; i < numberPlayer; i++) {
			game.addPlayer("player"+(i+1));
		}
	}
}

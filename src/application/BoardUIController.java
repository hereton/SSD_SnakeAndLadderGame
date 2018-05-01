package application;

import java.util.ArrayList;
import java.util.List;

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
	@FXML
	private AnchorPane boardAndPiece;

	private Game game;
	private List<AnchorPane> playersUI;

	public BoardUIController() {
		this.game = new Game();
		playersUI = new ArrayList<>();
	}

	@FXML
	public void initialize() {
		game.start();
		if (game.getPlayerSize() == 2) {
			playersUI.add(player1);
			playersUI.add(player2);
		} else if (game.getPlayerSize() == 3) {
			playersUI.add(player1);
			playersUI.add(player2);
			playersUI.add(player3);
		} else {
			playersUI.add(player1);
			playersUI.add(player2);
			playersUI.add(player3);
			playersUI.add(player4);
		}
		for (AnchorPane player : playersUI) {
			player.setVisible(true);
		}
		// GridPane gp = new GridPane();
		// for (int row = 0; row < 10; row++) {
		// for (int col = 0; col < 10; col++) {
		//
		// VBox hBox = new VBox();
		// hBox.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
		// "-fx-border-radius:2,2,2,2;"
		// + "-fx-border-color: blue;");
		//
		// if (row == 0 && col == 0) {
		// for (int n = 0; n < players.length; n++) {
		// player = new Label("player " + (n + 1));
		// hBox.getChildren().add(player);
		// }
		//
		// } else {
		// hBox.getChildren().add(new Label(row + "-" + col));
		// hBox.getChildren().add(new Label((row + 1) + "-" + col));
		// }
		//
		// gp.add(hBox, col, row);
		// }
		// }
		//
		// pane.getChildren().add(gp);

	}

	public void handleRollButton(ActionEvent e) {
		System.out.println("-----------------");
		System.out.println(game.currentPlayerName());
		System.out.println("Position : " + game.currentPlayerPosition());
		int face = game.currentPlayerRollDice();
		System.out.println("Die face = " + face);
		game.currentPlayerMove(face);
		System.out.println("Position : " + game.currentPlayerPosition());
		if (game.currentPlayerWin()) {
			System.out.println(game.currentPlayerName() + " win");
			System.out.println("Game win");
			game.end();
		} else {
			game.switchPlayer();
		}

	}

	public void setPlayer(int numberPlayer) {
		for (int i = 0; i < numberPlayer; i++) {
			game.addPlayer("player" + (i + 1));
		}
	}
}

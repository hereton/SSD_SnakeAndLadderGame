package application;

import java.util.ArrayList;
import java.util.List;

import game.Die;
import game.Game;
import game.Player;
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
	private Die die;
	private List<AnchorPane> playersUI;
	private int playersIndexUI = 0;
	private boolean goRight = true;
	private List<Boolean> directionPlayers;

	public BoardUIController() {
		this.game = new Game();
		this.die = new Die();
		playersUI = new ArrayList<>();
		directionPlayers = new ArrayList<>();
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
		for (int i = 0; i < playersUI.size(); i++) {
			directionPlayers.add(true);
			playersUI.get(i).setVisible(true);
			boardAndPiece.getChildren().get(i + 1).setLayoutX(0);
			boardAndPiece.getChildren().get(i + 1).setLayoutY(560);
			if (boardAndPiece.getChildren().get(i + 1).getId().equals("player2")) {
				boardAndPiece.getChildren().get(i + 1).setLayoutX(20);
			}
			if (boardAndPiece.getChildren().get(i + 1).getId().equals("player3")) {
				boardAndPiece.getChildren().get(i + 1).setLayoutX(40);
			}
			if (boardAndPiece.getChildren().get(i + 1).getId().equals("player4")) {
				boardAndPiece.getChildren().get(i + 1).setLayoutX(60);
			}
		}
	}

	public void handleRollButton(ActionEvent e) {
		System.out.println("-----------------");
		System.out.println(game.currentPlayerName());
		System.out.println("Position : " + game.currentPlayerPosition());

		int face = game.currentPlayerRollDice();

		System.out.println("Die face = " + face);

		game.currentPlayerMove(face);

		System.out.println(getPlayerX(playersIndexUI));
		System.out.println(getPlayerY(playersIndexUI));

		System.out.println("Position : " + game.currentPlayerPosition());
		playUIMove(face);
		if (game.currentPlayerWin()) {
			// System.out.println(game.currentPlayerName() + " win");
			// System.out.println("Game win");
			game.end();
			rollButton.setDisable(true);
		} else {
			playersIndexUI = game.switchPlayer();
		}
	}

	public void setPlayer(int numberPlayer) {
		for (int i = 0; i < numberPlayer; i++) {
			game.addPlayer("player" + (i + 1));
		}
	}

	private void playUIMove(int face) {
		for (int i = 1; i <= face; i++) {
			// direction right
			if (directionPlayers.get(playersIndexUI)) {
				playerGoRight(playersIndexUI);
				if (getPlayerX(playersIndexUI) > 799) {
					setPlayerUp(playersIndexUI);
					playerGoLeft(playersIndexUI);
					directionPlayers.set(playersIndexUI, false); // change direction to left.
				}
			} else if (!directionPlayers.get(playersIndexUI)) {
				playerGoLeft(playersIndexUI);
				if (getPlayerX(playersIndexUI) < 0) {
					setPlayerUp(playersIndexUI);
					playerGoRight(playersIndexUI);
					directionPlayers.set(playersIndexUI, true); // change direction to right.
				}
			}
		}
	}

	private double getPlayerX(int playerIndex) {
		return boardAndPiece.getChildren().get(playerIndex + 1).getLayoutX();
	}

	private double getPlayerY(int playerIndex) {
		return boardAndPiece.getChildren().get(playerIndex + 1).getLayoutY();
	}

	private void playerGoRight(int playersIndexUI) {
		boardAndPiece.getChildren().get(playersIndexUI + 1).setLayoutX(getPlayerX(playersIndexUI) + 80);
	}

	private void playerGoLeft(int playersIndexUI) {
		boardAndPiece.getChildren().get(playersIndexUI + 1).setLayoutX(getPlayerX(playersIndexUI) - 80);
	}

	private void setPlayerUp(int playersIndexUI) {
		boardAndPiece.getChildren().get(playersIndexUI + 1).setLayoutY(getPlayerY(playersIndexUI) - 60);
	}

}

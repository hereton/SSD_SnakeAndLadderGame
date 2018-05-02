package application;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

public class BoardUIController {

	@FXML
	private Pane pane;
	@FXML
	private Button rollButton;
	@FXML
	private Label rolledNumLabel;
	@FXML
	private Label dieFace;
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
	private int playersIndexUI = 0;
	private List<Boolean> directionPlayers;
	private List<Boolean> reachTheGoalButFaceNotRight;
	PathTransition t;

	public BoardUIController() {
		this.game = new Game();
		playersUI = new ArrayList<>();
		directionPlayers = new ArrayList<>();
		reachTheGoalButFaceNotRight = new ArrayList<>();

		t = new PathTransition();

	}

	@FXML
	public void initialize() {
		game.start();
		rolledNumLabel.setText(game.currentPlayerName() + " Turn");
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
			reachTheGoalButFaceNotRight.add(false);
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
		int nextMove = game.currentPlayerPosition();
		int face = game.currentPlayerRollDice();
		game.currentPlayerMove(face);
		nextMove = game.currentPlayerPosition() - nextMove;
		playUIMove(nextMove);
		if (game.currentPlayerWin()) {
			game.end();
			rollButton.setDisable(true);
		} else {
			playersIndexUI = game.switchPlayer();
			rolledNumLabel.setText(game.currentPlayerName() + " Turn");
		}
	}

	public void setPlayer(int numberPlayer) {
		for (int i = 0; i < numberPlayer; i++) {
			game.addPlayer("Player" + (i + 1));
		}
	}

	private void playUIMove(int face) {
		if (reachTheGoalButFaceNotRight.get(playersIndexUI))
			directionPlayers.set(playersIndexUI, false);
		System.out.println(face);
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
			if (getPlayerX(playersIndexUI) < 80 && getPlayerY(playersIndexUI) < 60) {
				directionPlayers.set(playersIndexUI, true);
				reachTheGoalButFaceNotRight.set(playersIndexUI, true);
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

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
	private int playersIndexUI = 0;
	private List<Boolean> directionPlayers;
	private List<Boolean> reachTheGoalButFaceNotRight;
	private List<Boolean> playersBackward;

	public BoardUIController() {
		this.game = new Game();
		playersUI = new ArrayList<>();
		directionPlayers = new ArrayList<>();
		reachTheGoalButFaceNotRight = new ArrayList<>();
		playersBackward = new ArrayList<>();
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
			reachTheGoalButFaceNotRight.add(false);
			playersBackward.add(false);
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

		int nextMove = game.currentPlayerPosition();
		int face = game.currentPlayerRollDice();

		System.out.println("Die face = " + face);

		game.currentPlayerMove(face);
		System.out.println("Position : " + game.currentPlayerPosition());
		nextMove = game.currentPlayerPosition() - nextMove;

		if (playersBackward.get(playersIndexUI)) {
			playersBackward.set(playersIndexUI, false);
			directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
			System.out.println("change to nomal direction after hit speacial tile");
		}
		if (nextMove < 0) {
			System.out.println("next move less that zero " + nextMove);
			directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
			playersBackward.set(playersIndexUI, true);
			nextMove = nextMove - (2 * nextMove);
			System.out.println("this is going back ward " + nextMove);
		}

		playUIMove(nextMove);
		if (game.currentPlayerWin()) {
			System.out.println(game.currentPlayerName() + " win");
			System.out.println("Game win");
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
		System.out.println(face);
		if (reachTheGoalButFaceNotRight.get(playersIndexUI))
			directionPlayers.set(playersIndexUI, false);
		for (int i = 1; i <= face; i++) {
			// direction right
			System.out.println("player position: " + directionPlayers.get(playersIndexUI));
			if (directionPlayers.get(playersIndexUI)) {
				playerGoRight(playersIndexUI);
				if (getPlayerX(playersIndexUI) > 799) {
					if (playersBackward.get(playersIndexUI)) {
						setPlayerDown(playersIndexUI); // after move on to snake
					} else {
						setPlayerUp(playersIndexUI); // normal move
					}
					playerGoLeft(playersIndexUI);
					directionPlayers.set(playersIndexUI, false); // change direction to left.

				}
			} else if (!directionPlayers.get(playersIndexUI)) {
				playerGoLeft(playersIndexUI);
				if (getPlayerX(playersIndexUI) < 0) {
					if (playersBackward.get(playersIndexUI)) {
						setPlayerDown(playersIndexUI);
					} else {
						setPlayerUp(playersIndexUI);
					}
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

	private void setPlayerDown(int playersIndexUI) {
		boardAndPiece.getChildren().get(playersIndexUI + 1).setLayoutY(getPlayerY(playersIndexUI) + 60);

	}

}

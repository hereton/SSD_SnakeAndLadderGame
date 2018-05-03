package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BoardUIController implements Observer {

	@FXML
	private Pane pane;
	@FXML
	private Button rollButton;
	@FXML
	private Label rolledNumLabel, dieFace, status;
	@FXML
	private AnchorPane player1, player2, player3, player4, boardAndPiece;

	private Game game;
	private List<AnchorPane> playersUI = new ArrayList<>();
	private List<Boolean> directionPlayers = new ArrayList<>();
	private List<Boolean> reachTheGoalButFaceNotRight = new ArrayList<>();
	private List<Boolean> playersBackward = new ArrayList<>();
	private int playersIndexUI = 0;

	public BoardUIController() {
		this.game = new Game();
		game.addObserver(this);
	}

	@FXML
	public void initialize() {
		game.start();
		rolledNumLabel.setText(game.currentPlayerName() + " Turn");
		switch (game.getPlayerSize()) {
		case 4:
			playersUI.add(player4);
		case 3:
			playersUI.add(player3);
		case 2:
			playersUI.add(player1);
			playersUI.add(player2);
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
		int nextMove = game.currentPlayerPosition();
		int face = game.currentPlayerRollDice();
		dieFace.setText("roll : " + face);
		game.currentPlayerMove(face);
		nextMove = game.currentPlayerPosition() - nextMove;
		if (playersBackward.get(playersIndexUI)) {
			playersBackward.set(playersIndexUI, false);
			directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
		}
		if (nextMove < 0) {
			directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
			playersBackward.set(playersIndexUI, true);
			nextMove *= -1;
		}
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
			game.addPlayer("Player " + (i + 1));
		}
	}

	private void playUIMove(int face) {
		if (reachTheGoalButFaceNotRight.get(playersIndexUI))
			directionPlayers.set(playersIndexUI, false);
		for (int i = 1; i <= face; i++) {
			// direction right
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

	@Override
	public void update(Observable o, Object arg) {
		status.setText(arg.toString());
	}

}

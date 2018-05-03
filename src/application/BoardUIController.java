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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BoardUIController implements Observer {

	@FXML
	private Pane pane;
	@FXML
	private Button rollButton;
	@FXML
	private Label turnPlayer_label, statusPlayer_label, player1_label, player2_label, player3_label, player4_label;
	@FXML
	private ImageView dice_imageView;
	@FXML
	private AnchorPane player1, player2, player3, player4, boardAndPiece;

	private Game game;
	private List<AnchorPane> playersUI = new ArrayList<>();
	private List<Boolean> directionPlayers = new ArrayList<>();
	private List<Boolean> reachTheGoalButFaceNotRight = new ArrayList<>();
	private List<Boolean> playersBackward = new ArrayList<>();
	private int playersIndexUI = 0;
	private List<Image> diceImages;

	public BoardUIController(Game game) {
		this.game = game;
		game.addObserver(this);
	}

	@FXML
	public void initialize() {
		game.start();
		turnPlayer_label.setText("Turn : " + game.currentPlayerName());
		setPlayersToStartPoint();
	}

	public void handleRollButton(ActionEvent e) {
		int nextMove = game.currentPlayerPosition();
		int face = game.currentPlayerRollDice();
		setDiceFace(face);
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
			turnPlayer_label.setText(game.currentPlayerName() + " Turn");
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

	private void setPlayersToStartPoint() {
		switch (game.getPlayerSize()) {
		case 4:
			playersUI.add(player4);
			player4_label.setText(game.getPlayersName(3));

		case 3:
			playersUI.add(player3);
			player3_label.setText(game.getPlayersName(2));

		case 2:
			playersUI.add(player1);
			playersUI.add(player2);
			player1_label.setText(game.getPlayersName(0));
			player2_label.setText(game.getPlayersName(1));

		}
		for (int i = 0; i < playersUI.size(); i++) {
			directionPlayers.add(true);
			reachTheGoalButFaceNotRight.add(false);
			playersBackward.add(false);
			playersUI.get(i).setVisible(true);
			boardAndPiece.getChildren().get(i + 1).setLayoutX((i) * 20);
			boardAndPiece.getChildren().get(i + 1).setLayoutY(560);
		}
		diceImages = new ArrayList<>();
		diceImages.add(new Image("Dice1.png"));
		diceImages.add(new Image("Dice2.png"));
		diceImages.add(new Image("Dice3.png"));
		diceImages.add(new Image("Dice4.png"));
		diceImages.add(new Image("Dice5.png"));
		diceImages.add(new Image("Dice6.png"));

	}

	private void setDiceFace(int face) {
		dice_imageView.setImage(diceImages.get(face - 1));
	}

	@Override
	public void update(Observable o, Object arg) {
		statusPlayer_label.setText(arg.toString());
	}

}

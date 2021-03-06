package offline.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import game.Game;
import game.replay.Action;
import game.replay.MoveAction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
	private ActionEvent event;
	private Iterator<Action> replay;

	public BoardUIController(Game game) {
		this.game = game;
		this.game.addObserver(this);
	}

	@FXML
	public void initialize() {
		game.start();
		turnPlayer_label.setText("Turn : " + game.currentPlayerName());
		setPlayersToStartPoint();
	}

	public void handleRollButton(ActionEvent e) {
		event = e;
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
			openWinUI();

		} else {
			playersIndexUI = game.switchPlayer();
			turnPlayer_label.setText(game.currentPlayerName() + " Turn");
		}
	}

	public void runReplay() {
		setPlayersToStartPoint();
		new Thread(() -> {
			while (replay.hasNext()) {
				MoveAction ac = (MoveAction) replay.next();
				String playername = ac.getPlayerName();

				if (playername.equals(player1_label.getText())) {
					playersIndexUI = 0;
				} else if (playername.equals(player2_label.getText())) {
					playersIndexUI = 1;
				} else if (playername.equals(player3_label.getText())) {
					playersIndexUI = 2;
				} else if (playername.equals(player4_label.getText())) {
					playersIndexUI = 3;
				}

				int stepmoves = ac.getStepMove();
				int dieFace = ac.getDieFace();
				if (playersBackward.get(playersIndexUI)) {
					playersBackward.set(playersIndexUI, false);
					directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
				}
				if (stepmoves < 0) {
					directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
					playersBackward.set(playersIndexUI, true);
					stepmoves *= -1;
				}
				final int uimove = stepmoves;
				Platform.runLater(() -> {
					turnPlayer_label.setText(playername);
					playUIMove(uimove);
					setDiceFace(dieFace);
				});
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Platform.runLater(() -> {
				openWinUI();
			});
		}).start();
	}

	private void playUIMove(int face) {
		if (reachTheGoalButFaceNotRight.get(playersIndexUI))
			directionPlayers.set(playersIndexUI, false);
		for (int i = 1; i <= face; i++) {
			// direction right
			if (directionPlayers.get(playersIndexUI)) {
				playerGoRight(playersIndexUI);
				if (getPlayerX(playersIndexUI) > 790) {
					if (playersBackward.get(playersIndexUI)) {
						setPlayerDown(playersIndexUI); // after move on to snake
					} else {
						setPlayerUp(playersIndexUI); // normal move
					}
					playerGoLeft(playersIndexUI);
					directionPlayers.set(playersIndexUI, false); // change direction to left.

				}
			} else {
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

			if (getPlayerX(playersIndexUI) < 80 && getPlayerY(playersIndexUI) < 40) {
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
		this.directionPlayers.clear();
		this.reachTheGoalButFaceNotRight.clear();
		this.playersUI.clear();
		this.playersBackward.clear();
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
			boardAndPiece.getChildren().get(i + 1).setLayoutX((i) * 15);
			boardAndPiece.getChildren().get(i + 1).setLayoutY(530);
		}
		diceImages = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			diceImages.add(new Image("Dice" + i + ".png"));
		}
	}

	private void setDiceFace(int face) {
		dice_imageView.setImage(diceImages.get(face - 1));
	}

	@Override
	public void update(Observable o, Object arg) {
		statusPlayer_label.setText(arg.toString());
		if (arg.equals("restart")) {
			restartGame();
		}
		if (arg.equals("new game")) {
			newGame();
		}
		if (arg.equals("replay")) {
			this.replay = game.getLastReplay();
			this.runReplay();
		}
	}

	private void restartGame() {
		playersIndexUI = 0;
		List<String> playersName = new ArrayList<>();
		for (int i = 0; i < game.getPlayerSize(); i++) {
			playersName.add(game.getPlayersName(i));
		}
		game.restart();
		for (String player : playersName) {
			game.addPlayer(player);
		}
		this.game.addObserver(this);
		game.start();
		turnPlayer_label.setText("Turn : " + game.currentPlayerName());
		setPlayersToStartPoint();
		rollButton.setDisable(false);
	}

	private void newGame() {
		Stage newStage = new Stage();
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("GameUI.fxml"));
			Scene scene = new Scene(root);
			newStage.setScene(scene);
			newStage.sizeToScene();
			newStage.setTitle("Snake and Ladder !");
			newStage.show();
			newStage.setResizable(false);
		} catch (Exception e) {
			System.out.println("Exception creating scene: " + e.getMessage());
		}
		Node source = (Node) event.getSource();
		Stage thisStage = (Stage) source.getScene().getWindow();
		thisStage.close();
	}

	private void openWinUI() {
		Stage newStage = new Stage();
		try {
			WinControllerUI winCon = new WinControllerUI(game.currentPlayerName());
			winCon.addObserver(this);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WinUI.fxml"));
			loader.setController(winCon);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);

			newStage.setScene(scene);
			newStage.sizeToScene();
			newStage.setTitle("Snake and Ladder !");
			newStage.show();
			newStage.setResizable(false);
		} catch (Exception ex) {
			System.out.println("Exception creating scene: " + ex.getMessage());
		}
	}
}

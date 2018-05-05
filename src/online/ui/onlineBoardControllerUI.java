package online.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import com.esotericsoftware.kryonet.Client;

import game.replay.Action;
import game.replay.MoveAction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.stage.Stage;
import online.data.RollDice;
import online.data.RoomData;

public class onlineBoardControllerUI implements Observer {
	@FXML
	private AnchorPane boardAndPiece, player1, player2, player3, player4;
	@FXML
	private Label turnPlayer_label, statusPlayer_label, player1_name_label, player2_name_label, player3_name_label,
			player4_name_label;
	@FXML
	private ImageView dice_imageView;
	@FXML
	private Button rollButton;

	private RoomData roomdata;
	private List<AnchorPane> playersUI = new ArrayList<>();
	private List<Boolean> directionPlayers = new ArrayList<>();
	private List<Boolean> reachTheGoalButFaceNotRight = new ArrayList<>();
	private List<Boolean> playersBackward = new ArrayList<>();
	private int playersIndexUI = 0;
	private List<Image> diceImages;

	private Client client;
	private String myName;
	private String currentTurn;
	private Iterator<Action> replay;
	private Event event;

	public onlineBoardControllerUI(Client client) {
		this.client = client;
		diceImages = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			diceImages.add(new Image("Dice" + i + ".png"));
		}

	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	@FXML
	public void handleRollButton(ActionEvent e) {
		event = e;
		RollDice roll = new RollDice();
		roll.name = myName;
		client.sendTCP(roll);
	}

	@FXML
	public void initialize() {
		setPlayerTurn(this.currentTurn);
		refreshPlayer();

	}

	public void setRoomData(RoomData roomdata) {
		this.roomdata = roomdata;
	}

	public void setPlayerTurn(String currentPlayerTurn) {
		this.currentTurn = currentPlayerTurn;
		try {
			// set status turn
			turnPlayer_label.setText("Turn : " + currentPlayerTurn);

			if (currentPlayerTurn.equals(myName)) {
				// enable roll button
				rollButton.setDisable(false);
			} else {
				// disable roll button
				rollButton.setDisable(true);
			}
		} catch (NullPointerException npe) {
			// do nothing
		}
	}

	public void runReplay() {
		// TODO restart replay
		directionPlayers.clear();
		reachTheGoalButFaceNotRight.clear();
		playersBackward.clear();
		for (int i = 0; i < playersUI.size(); i++) {
			setPlayerUIToStartPoint(i);
		}
		new Thread(() -> {
			String currentPlayer = "";
			while (replay.hasNext()) {
				MoveAction a = (MoveAction) replay.next();
				currentPlayer = a.getPlayerName();
				Platform.runLater(() -> {
					move(a.getPlayerName(), a.getDieFace(), a.getStepMove());
				});
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			final String playerWin = currentPlayer;
			Platform.runLater(() -> {
				openWinUI(playerWin);
			});
		}).start();
	}

	/**
	 * 
	 * @param playername
	 * @param steps
	 *            can be both positive and negative
	 */
	public void move(String playername, int dieFace, int steps) {
		try {
			setDiceFace(dieFace);
			if (playersBackward.get(playersIndexUI)) {
				playersBackward.set(playersIndexUI, false);
				directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
			}
			if (steps < 0) {
				directionPlayers.set(playersIndexUI, !directionPlayers.get(playersIndexUI));
				playersBackward.set(playersIndexUI, true);
				steps *= -1;
			}
			if (playername.equals(player1_name_label.getText())) {
				playersIndexUI = 0;
				playUIMove(steps);

			} else if (playername.equals(player2_name_label.getText())) {
				playersIndexUI = 1;
				playUIMove(steps);
			} else if (playername.equals(player3_name_label.getText())) {
				playersIndexUI = 2;
				playUIMove(steps);
			} else if (playername.equals(player4_name_label.getText())) {
				playersIndexUI = 3;
				playUIMove(steps);
			}
		} catch (IndexOutOfBoundsException o) {

		}
	}

	/**
	 * maximum 4 players add picture and label to board
	 * 
	 * @param playername
	 */
	public void addPlayerToBoard(String playername) {
		Platform.runLater(() -> {
			int playerIndex = roomdata.players.indexOf(playername);
			if (playerIndex == 0) {
				playersUI.add(0, this.player1);
				player1_name_label.setText(playername);
				setPlayerUIToStartPoint(playerIndex);
			}
			if (playerIndex == 1) {
				playersUI.add(1, this.player2);
				player2_name_label.setText(playername);
				setPlayerUIToStartPoint(playerIndex);
			}
			if (playerIndex == 2) {
				playersUI.add(2, this.player3);
				player3_name_label.setText(playername);
				setPlayerUIToStartPoint(playerIndex);

			}
			if (playerIndex == 3) {
				playersUI.add(3, this.player4);
				player4_name_label.setText(playername);
				setPlayerUIToStartPoint(playerIndex);
			}
		});
	}

	/**
	 * it's not working :( so sad :(
	 */
	public void removePlayerFromBoard(String playername) {
		playersUI.clear();
		for (String name : roomdata.players) {

			if (name.equals(player1_name_label.getText())) {
				playersUI.add(player1);
			}

			if (name.equals(player2_name_label.getText())) {
				playersUI.add(player2);
			}

			if (name.equals(player3_name_label.getText())) {
				playersUI.add(player3);
			}

			if (name.equals(player4_name_label.getText())) {
				playersUI.add(player4);
			}
		}
	}

	private void setPlayerUIToStartPoint(int i) {
		directionPlayers.add(true);
		reachTheGoalButFaceNotRight.add(false);
		playersBackward.add(false);
		playersUI.get(i).setVisible(true);
		boardAndPiece.getChildren().get(i + 1).setLayoutX((i) * 14);
		boardAndPiece.getChildren().get(i + 1).setLayoutY(560);

	}

	public void refreshPlayer() {
		try {
			playersUI.clear();
			for (String s : roomdata.players) {
				addPlayerToBoard(s);
			}
		} catch (NullPointerException npe) {

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

	public void setPlayerWin(String playerWin) {
		rollButton.setDisable(true);
		Platform.runLater(() -> {
			openWinUI(playerWin);
		});
	}

	private void openWinUI(String player) {
		Stage newStage = new Stage();
		try {
			onlineWinControllerUI winCon = new onlineWinControllerUI(player);
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

	private void setDiceFace(int face) {
		try {
			dice_imageView.setImage(diceImages.get(face - 1));
		} catch (NullPointerException npe) {
			// do nothing
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			String s = (String) arg;
			if (s.equals("replay")) {
				this.runReplay();
			}
			if (s.equals("new game")) {
				this.newGame();
				// TODO
				try {
					client.reconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void setReplay(Iterator<Action> replay) {
		this.replay = replay;
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

}

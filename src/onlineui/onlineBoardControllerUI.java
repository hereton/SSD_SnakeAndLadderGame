package onlineui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import online.RollData;
import online.RoomData;

public class onlineBoardControllerUI {
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

	@FXML
	public void handleRollButton(ActionEvent e) {

	}

	public void handlePing(RollData rd) {
		for (String s : rd.data.keySet()) {
			System.out.println(s + " : " + rd.data.get(s));
		}
	}

	public void setRoomdata(RoomData roomdata) {
		this.roomdata = roomdata;
	}

	/**
	 * 
	 * @param playername
	 * @param steps
	 *            can be both positive and negative
	 */
	public void move(String playername, int steps) {
		for (AnchorPane player : playersUI) {
			if (player.getChildren().get(1).toString().equals(playername)) {
				// move by step
			}
		}
	}

	/**
	 * maximum 4 players add picture and label to board
	 * 
	 * @param playername
	 */
	public void addPlayerToBoard(String playername) {
		int playerIndex = roomdata.players.indexOf(playername);
		if (playerIndex == 0) {
			playersUI.set(0, this.player1);
			player1_name_label.setText(playername);
			setPlayerUIToStartPoint(playerIndex);
		}
		if (playerIndex == 1) {
			playersUI.set(1, this.player2);
			player2_name_label.setText(playername);
			setPlayerUIToStartPoint(playerIndex);
		}
		if (playerIndex == 2) {
			playersUI.set(1, this.player3);
			player3_name_label.setText(playername);
			setPlayerUIToStartPoint(playerIndex);

		}
		if (playerIndex == 3) {
			playersUI.set(1, this.player4);
			player4_name_label.setText(playername);
			setPlayerUIToStartPoint(playerIndex);
		}
	}

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
		boardAndPiece.getChildren().get(i + 1).setLayoutX((i) * 20);
		boardAndPiece.getChildren().get(i + 1).setLayoutY(560);
	}
}

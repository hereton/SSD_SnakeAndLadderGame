package onlineui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import online.RollData;

public class onlineBoardControllerUI {
	@FXML
	private AnchorPane boardAndPiece, player1, player2, player3, player4;
	@FXML
	private Label turnPlayer_label, statusPlayer_label;
	@FXML
	private ImageView dice_imageView;
	@FXML
	private Button rollButton;

	@FXML
	public void handleRollButton(ActionEvent e) {

	}


	/**
	 * 
	 * @param playername
	 * @param steps
	 *            can be both positive and negative
	 */
	public void move(String playername, int steps) {

	}

	/**
	 * maximum 4 players add picture and label to board
	 * 
	 * @param Playername
	 */
	public void addPlayerToBoard(String Playername) {

	}

	public void removePlayerFromBoard(String playername) {

	}
}

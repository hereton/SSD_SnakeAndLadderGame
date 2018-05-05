package online.ui;

import java.util.Observable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class onlineWinControllerUI extends Observable {
	@FXML
	private Label player_win_label;
	@FXML
	private Button replay_button, home_button;

	private String winPlayer;

	public onlineWinControllerUI(String winPlayer) {
		this.winPlayer = winPlayer;
	}

	@FXML
	public void initialize() {
		System.out.println(winPlayer);
		player_win_label.setText(winPlayer + " WIN !!");
	}

	public void handleReplayButton(ActionEvent e) {

		updateObserver("replay");
		closeWinUI(e);
	}

	public void handleNewGameButton(ActionEvent e) {
		updateObserver("new game");
		closeWinUI(e);
	}

	private void closeWinUI(ActionEvent e) {
		Node source = (Node) e.getSource();
		Stage thisStage = (Stage) source.getScene().getWindow();
		thisStage.close();
	}

	private void updateObserver(String result) {
		setChanged();
		notifyObservers(result);
	}

}

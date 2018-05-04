package offline.ui;

import java.util.Observable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WinControllerUI extends Observable {
	@FXML
	private Label playerWin_label;
	@FXML
	private Button replay_button, restart_button, newGame_button;

	private String winPlayer;

	public WinControllerUI(String winPlayer) {
		this.winPlayer = winPlayer;
	}

	@FXML
	public void initialize() {
		playerWin_label.setText(winPlayer);
	}

	public void handleReplayButton(ActionEvent e) {

		updateObserver("replay");
		// closeWinUI(e);
	}

	public void handleRestartButton(ActionEvent e) {
		updateObserver("restart");
		// closeWinUI(e);
	}

	public void handleNewGameButton(ActionEvent e) {
		updateObserver("new game");
		// closeWinUI(e);
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

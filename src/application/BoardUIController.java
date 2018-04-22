package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BoardUIController {

	@FXML
	private Button rollButton;
	@FXML
	private Label rolledNumLabel;

	public void handleRollButton() {
		int rolled = (int) (Math.random() * 6) + 1;
		rolledNumLabel.setText("Player x walk: " + rolled + " block");
	}
}

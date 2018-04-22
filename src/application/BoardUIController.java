package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardUIController {

	@FXML
	private Pane pane;
	@FXML
	private Button rollButton;
	@FXML
	private Label rolledNumLabel;

	@FXML
	public void initialize() {
		GridPane gp = new GridPane();
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Button button = new Button(i + ", " + j);
				gp.add(button, j, i);
			}
		}
		pane.getChildren().add(gp);
	}

	public void handleRollButton(ActionEvent e) {
		int rolled = (int) (Math.random() * 6) + 1;
		rolledNumLabel.setText("Player x walk: " + rolled + " block");
	}
}

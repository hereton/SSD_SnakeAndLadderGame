package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class GameUIController {

	@FXML
	private ChoiceBox<Integer> choiseBox;
	@FXML
	private Button playButton;

	@FXML
	public void initialize() {
		for (int i = 2; i <= 4; i++) {
			choiseBox.getItems().add(i);
		}
		choiseBox.getSelectionModel().select(0);
	}

	public void handlePlay(ActionEvent e) {
		int numberPlayer = choiseBox.getValue();
		System.out.println(numberPlayer);
		Stage stage = new Stage();
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("BoardUI.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setTitle("Snake and Ladder !");
			stage.show();
			Node source = (Node) e.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		} catch (Exception ex) {
			System.out.println("Exception creating scene: " + ex.getMessage());
		}
	}

}

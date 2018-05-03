package application;

import game.Game;
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

	private Game game;

	public GameUIController() {
		this.game = new Game();
	}

	public void initialize() {
		for (int i = 2; i <= 4; i++) {
			choiseBox.getItems().add(i);
		}
		choiseBox.getSelectionModel().select(0);
	}

	public void handlePlay(ActionEvent e) {
		// add player

		int numberPlayer = choiseBox.getValue();
		Stage newStage = new Stage();
		try {
			BoardUIController bCon = new BoardUIController(this.game);
			bCon.setPlayer(numberPlayer);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardUI.fxml"));
			loader.setController(bCon);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);

			newStage.setScene(scene);
			newStage.sizeToScene();
			newStage.setTitle("Snake and Ladder !");
			newStage.show();

			Node source = (Node) e.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		} catch (Exception ex) {
			System.out.println("Exception creating scene: " + ex.getMessage());
		}
	}

}

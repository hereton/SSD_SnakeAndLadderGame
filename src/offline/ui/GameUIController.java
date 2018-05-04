package offline.ui;

import game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameUIController {

	@FXML
	private Button playButton;
	@FXML
	private CheckBox checkBoxPlayer1, checkBoxPlayer2, checkBoxPlayer3, checkBoxPlayer4;
	@FXML
	private TextField textFieldPlayer1, textFieldPlayer2, textFieldPlayer3, textFieldPlayer4;

	private Game game;

	public GameUIController() {
		this.game = new Game();
	}

	public void initialize() {

	}

	public void handlePlay(ActionEvent e) {
		CheckBox[] checkboxes = { checkBoxPlayer1, checkBoxPlayer2, checkBoxPlayer3, checkBoxPlayer4 };
		TextField[] textfields = { textFieldPlayer1, textFieldPlayer2, textFieldPlayer3, textFieldPlayer4 };
		for (int i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].isSelected()) {
				String playername = !textfields[i].getText().trim().isEmpty() ? textfields[i].getText()
						: "Player " + (i + 1);
				game.addPlayer(playername);
			}
		}

		Stage newStage = new Stage();
		try {
			BoardUIController bCon = new BoardUIController(this.game);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardUI.fxml"));
			loader.setController(bCon);
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);

			newStage.setScene(scene);
			newStage.sizeToScene();
			newStage.setTitle("Snake and Ladder !");
			newStage.show();
			newStage.setResizable(false);

			Node source = (Node) e.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		} catch (Exception ex) {
			System.out.println("Exception creating scene: " + ex.getMessage());
		}
	}

}

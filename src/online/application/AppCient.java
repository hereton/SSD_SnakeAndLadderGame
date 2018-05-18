package online.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppCient extends Application {
	// public static String IP_ADDRESS = "35.197.154.253";
	public static String IP_ADDRESS = "127.0.0.1";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("../ui/GameUi.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setTitle("Snake and Ladder !");
			stage.show();
			stage.setResizable(false);
		} catch (Exception e) {
			System.out.println("Exception creating scene: " + e.getMessage());
			e.printStackTrace();
		}
	}

}

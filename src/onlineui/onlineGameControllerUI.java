package onlineui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import application.BoardUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import online.PlayerDisconnect;
import online.PlayerJoin;
import online.PlayerTurn;
import online.RollData;
import online.RollDice;
import online.RoomData;

public class onlineGameControllerUI {
	@FXML
	private Label status_label;
	@FXML
	private Button joinButton;
	@FXML
	private TextField playerName_textField;

	private Client client;
	private int SERVER_PORT = 3309;
	private String SERVER_IP = "127.0.0.1";

	private onlineBoardControllerUI controller;

	@FXML
	public void initialize() {
		createConnection();
	}

	private void createConnection() {
		client = new Client();
		client.addListener(new ClientListener());

		client.getKryo().register(RoomData.class);
		client.getKryo().register(PlayerJoin.class);
		client.getKryo().register(RollDice.class);
		client.getKryo().register(PlayerTurn.class);
		client.getKryo().register(RollData.class);
		client.getKryo().register(ArrayList.class);
		client.getKryo().register(HashMap.class);
		client.getKryo().register(PlayerDisconnect.class);

		client.start();
		try {
			client.connect(5000, SERVER_IP, SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Cannot connect to : " + SERVER_IP + ":" + SERVER_PORT);
			e.printStackTrace();
		} finally {
			System.out.println("Connected to " + SERVER_IP + ":" + SERVER_PORT);
		}
	}

	private class ClientListener extends Listener {
		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof RoomData) {
				System.out.println("Received room data");
				RoomData roomStatus = (RoomData) o;

				for (String s : roomStatus.players) {
					System.out.println(s);
				}
				// controller.setRoomData(roomStatus);
				Platform.runLater(() -> {
					if (roomStatus.isPlaying) {
						status_label.setText("Game is playing");
						joinButton.setDisable(true);
					} else {
						int playersize = roomStatus.players.size();
						if (playersize == 4)
							status_label.setText("Status : Full (4/4)");
						else
							status_label.setText("Status : waiting (" + playersize + "/4)");
					}
				});
			}

			if (o instanceof PlayerTurn) {
				System.out.println("Received player turn");
				// controller.setPlayerTurn( ((PlayerTurn) o).currentPlayerTurn);
			}

			if (o instanceof RollData) {
				System.out.println("Reveived Roll Data");
				RollData rd = (RollData) o;
				if (controller != null) {

				}
			}

			if (o instanceof PlayerDisconnect) {
				System.out.println("Whoops some player is disconnected");
			}
		}
	}

	@FXML
	public void handlePlayButton(ActionEvent e) {
		String name = playerName_textField.getText();
		PlayerJoin player = new PlayerJoin();
		player.name = name;
		client.sendTCP(player);

		Stage newStage = new Stage();
		try {
			controller = new onlineBoardControllerUI();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardUI.fxml"));
			loader.setController(controller);
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

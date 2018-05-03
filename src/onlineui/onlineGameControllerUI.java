package onlineui;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

		client.start();
		try {
			client.connect(5000, SERVER_IP, SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Cannot connect to : " + SERVER_IP + ":" + SERVER_PORT);
			// e.printStackTrace();
		} finally {
			System.out.println("Connected to " + SERVER_IP + ":" + SERVER_PORT);
		}
	}

	private class ClientListener extends Listener {
		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof RoomData) {
				System.out.println("Recieve room data");
				RoomData roomStatus = (RoomData) o;
				if (roomStatus.isPlaying) {
					status_label.setText("Game is playing");
					joinButton.setDisable(true);
				} else {
					int playersize = roomStatus.players.size();
					// cannot update in main thread javafx convention
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							status_label.setText("Status : waiting (" + playersize + "/4)");
						}
					});
				}
			}
		}
	}

	@FXML
	public void handlePlayButton() {
		String name = playerName_textField.getText();
		PlayerJoin player = new PlayerJoin();
		player.name = name;
		client.sendTCP(player);
	}
}

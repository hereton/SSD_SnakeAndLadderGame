package onlineui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import online.PlayerJoin;
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

	private String name = "Admin1234";

	public onlineGameControllerUI() {
		client = new Client();
		client.addListener(new ClientListener());

		client.getKryo().register(RoomData.class);
		client.getKryo().register(PlayerJoin.class);

		client.start();
		try {
			client.connect(5000, SERVER_IP, SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Cannot connect to : " + SERVER_IP + ":" + SERVER_PORT);
			// e.printStackTrace();
		} finally {
			System.out.println("Connected to " + SERVER_IP + ":" + SERVER_PORT);
		}

		PlayerJoin player = new PlayerJoin();
		player.name = this.name;
		client.sendTCP(player);
	}

	private class ClientListener extends Listener {
		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof RoomData) {
				RoomData roomStatus = (RoomData) o;
				System.out.println(roomStatus.isPlaying);
				// System.out.println(roomStatus.numberOfPlayer);
			}
		}
	}

	public void handlePlay(ActionEvent e) {
		System.out.println("clicked");
	}
}

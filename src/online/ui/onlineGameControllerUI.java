package online.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
import online.application.AppCient;
import online.data.GameStatus;
import online.data.PlayerDisconnect;
import online.data.PlayerJoin;
import online.data.PlayerTurn;
import online.data.Replay;
import online.data.RollData;
import online.data.RollDice;
import online.data.RoomData;
import online.data.WinData;

public class onlineGameControllerUI {
	@FXML
	private Label status_label;
	@FXML
	private Button joinButton;
	@FXML
	private TextField playerName_textField;

	private Client client;
	private int SERVER_PORT = 3309;
	private final String SERVER_IP;

	private onlineBoardControllerUI controller;
	private RoomData roomStatus;

	public onlineGameControllerUI() {
		SERVER_IP = AppCient.IP_ADDRESS;
	}

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

		client.getKryo().register(WinData.class);
		client.getKryo().register(Replay.class);
		client.getKryo().register(ArrayList.class);

		client.getKryo().register(GameStatus.class);

		controller = new onlineBoardControllerUI(client);

		client.start();
		try {
			client.connect(5000, SERVER_IP, SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Cannot connect to : " + SERVER_IP + ":" + SERVER_PORT);
		} finally {
			System.out.println("Connected to " + SERVER_IP + ":" + SERVER_PORT);
		}
	}

	private class ClientListener extends Listener {
		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof RoomData) {
				roomStatus = (RoomData) o;
				controller.setRoomData(roomStatus);
				controller.refreshPlayer();
				// UPDATE HOME UI
				Platform.runLater(() -> {
					if (roomStatus.isPlaying) {
						status_label.setText("Game is playing");
						joinButton.setDisable(true);
					} else {
						int playersize = roomStatus.players.size();
						if (playersize == 4)
							status_label.setText("Status : Full (4/4)");
						else {
							status_label.setText("Status : waiting (" + playersize + "/4)");
							joinButton.setDisable(false);
						}
					}
				});
			}

			if (o instanceof PlayerTurn) {
				Platform.runLater(() -> controller.setPlayerTurn(((PlayerTurn) o).currentPlayerTurn));
			}

			if (o instanceof RollData) {
				RollData rd = (RollData) o;
				controller.move(rd.playername, rd.diceFace, rd.steps);
			}

			if (o instanceof PlayerDisconnect) {
				System.out.println("Whoops some player is disconnected");
				// PlayerDisconnect pd = (PlayerDisconnect) o;
				// TODO
				// GAME END? < this is the best idea.
			}

			if (o instanceof WinData) {
				WinData wd = (WinData) o;
				controller.setPlayerWin(wd.playername);
				controller.setReplay(wd.replays);
			}

			if (o instanceof GameStatus) {
				controller.setStatus(((GameStatus) o).status.toString());
			}

		}
	}

	public void joinServerGame(String name) {
		PlayerJoin player = new PlayerJoin();
		player.name = name;
		client.sendTCP(player);
	}

	@FXML
	public void handlePlayButton(ActionEvent e) {
		// set controller name
		String name = playerName_textField.getText();
		controller.setMyName(name);
		// join game on server
		joinServerGame(name);

		// new game scene
		Stage newStage = new Stage();
		try {
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
			ex.printStackTrace();
		}

	}

}

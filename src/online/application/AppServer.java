package online.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import game.Game;
import online.data.PlayerDisconnect;
import online.data.PlayerJoin;
import online.data.PlayerTurn;
import online.data.RollData;
import online.data.RollDice;
import online.data.RoomData;
import online.data.WinData;

public class AppServer extends Game {
	private Server server;
	private int SERVER_PORT = 3309;
	private List<Connection> connections = new ArrayList<>();
	private RoomData roomStatus = new RoomData();
	private Map<Connection, String> playermap = new HashMap<>();

	private Game game;

	public AppServer() {
		server = new Server();
		server.addListener(new ServerListener());

		server.getKryo().register(RoomData.class);
		server.getKryo().register(PlayerJoin.class);
		server.getKryo().register(RollDice.class);
		server.getKryo().register(PlayerTurn.class);
		server.getKryo().register(RollData.class);
		server.getKryo().register(ArrayList.class);
		server.getKryo().register(HashMap.class);
		server.getKryo().register(PlayerDisconnect.class);
		server.getKryo().register(WinData.class);

		server.start();
		try {
			server.bind(SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Another server use this port already : " + SERVER_PORT);
			e.printStackTrace();
		} finally {
			System.out.println("Server is running on port : " + SERVER_PORT);
		}

		this.game = new Game();
		roomStatus.isPlaying = game.isPlaying();
	}

	private class ServerListener extends Listener {
		@Override
		public void connected(Connection arg0) {
			super.connected(arg0);
			System.out.println("connected");
			connections.add(arg0);
			arg0.sendTCP(roomStatus);
		}

		@Override
		public void disconnected(Connection arg0) {
			super.disconnected(arg0);
			System.out.println(arg0 + "Disconnected");
			connections.remove(arg0);
			if (playermap.get(arg0) != null) {
				System.out.println("client in room");
				PlayerDisconnect pd = new PlayerDisconnect();
				pd.name = playermap.get(arg0);
				roomStatus.players.remove(pd.name);
				for (Connection c : connections) {
					c.sendTCP(pd);
				}
				sendRoomStatus();
				playermap.remove(arg0);
				if (playermap.keySet().size() == 0) {
					System.out.println("Restart game");
					game.restart();
				}
			}
		}

		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof PlayerJoin) {
				PlayerJoin player = (PlayerJoin) o;
				System.out.println("Player " + player.name + " is joining the server");
				game.addPlayer(player.name);
				roomStatus.players.add(player.name);
				playermap.put(arg0, player.name);
				sendRoomStatus();
				sendCurrentPlayerTurn();
			}
			if (o instanceof RollDice) {
				System.out.println("Rolling dice");
				if (arg0.equals(connections.get(0))) {
					if (!game.isPlaying())
						game.start();
				}
				int nextMove = game.currentPlayerPosition();
				int face = game.currentPlayerRollDice();
				game.currentPlayerMove(face);
				nextMove = game.currentPlayerPosition() - nextMove;
				sendRollData(game.currentPlayerName(), face, nextMove);

				if (game.currentPlayerWin()) {
					// handle when someone win
					sendPlayerWin(game.currentPlayerName());
					game.end();

				} else {
					game.switchPlayer();
					sendCurrentPlayerTurn();
				}
			}
		}
	}

	private void sendRoomStatus() {
		for (Connection c : connections) {
			c.sendTCP(roomStatus);
		}
	}

	private void sendCurrentPlayerTurn() {
		PlayerTurn pt = new PlayerTurn();
		pt.currentPlayerTurn = game.currentPlayerName();
		for (Connection c : connections) {
			c.sendTCP(pt);
		}
	}

	private void sendRollData(String currentPlayerName, int face, int steps) {
		RollData datas = new RollData();
		datas.playername = currentPlayerName;
		datas.steps = steps;
		datas.diceFace = face;

		for (Connection c : connections) {
			c.sendTCP(datas);
		}
	}

	private void sendPlayerWin(String currentPlayerName) {
		WinData wd = new WinData();
		wd.playername = currentPlayerName;
		wd.replay = game.getLastReplay();
		for (Connection c : connections) {
			c.sendTCP(wd);
		}
	}

	public static void main(String[] args) {
		new AppServer();
	}

}

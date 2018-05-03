package online;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import game.Game;

public class AppServer extends Game {
	private Server server;
	private int SERVER_PORT = 3309;
	private List<Connection> connections = new ArrayList<>();
	private RoomData roomStatus = new RoomData();

	private Game game;

	public AppServer() {
		server = new Server();
		server.addListener(new ServerListener());

		server.getKryo().register(RoomData.class);
		server.getKryo().register(PlayerJoin.class);
		server.getKryo().register(RollDice.class);
		server.getKryo().register(PlayerTurn.class);
		server.getKryo().register(RollData.class);

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
			connections.remove(arg0);
		}

		@Override
		public void received(Connection arg0, Object o) {
			super.received(arg0, o);
			if (o instanceof PlayerJoin) {
				PlayerJoin player = (PlayerJoin) o;
				game.addPlayer(player.name);
				roomStatus.players.add(player.name);
				sendCurrentPlayerTurn();
			}
			if (o instanceof RollDice) {
				if (arg0.equals(connections.get(0))) {
					if (!game.isPlaying())
						game.start();
				}
				int face = game.currentPlayerRollDice();
				game.currentPlayerMove(face);
				sendPositionData();
				if (game.currentPlayerWin()) {
					game.end();
				} else {
					game.switchPlayer();
					sendCurrentPlayerTurn();
				}
			}
		}
	}

	private void sendCurrentPlayerTurn() {
		PlayerTurn pt = new PlayerTurn();
		pt.currentPlayerTurn = game.currentPlayerName();
	}

	private void sendPositionData() {
		RollData datas = new RollData();
		datas.data.put(game.currentPlayerName(), game.currentPlayerPosition());
		for (Connection c : connections) {
			c.sendTCP(datas);
		}
	}

	public static void main(String[] args) {
		new AppServer();
	}
}

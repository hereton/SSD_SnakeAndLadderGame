package online;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class AppCient {
	private Client client;
	private int SERVER_PORT = 3309;
	private String SERVER_IP = "127.0.0.1";

	private String name = "Admin1234";

	public AppCient() {
		client = new Client();
		client.addListener(new ClientListener());

		client.getKryo().register(RoomData.class);
		client.getKryo().register(PlayerJoin.class);

		client.start();
		try {
			client.connect(5000, SERVER_IP, SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Cannot connect to : " + SERVER_IP + ":" + SERVER_PORT);
			e.printStackTrace();
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
				System.out.println(roomStatus.numberOfPlayer);
			}
		}
	}

	public static void main(String[] args) {
		new AppCient();
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

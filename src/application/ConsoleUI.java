package application;

import java.util.Scanner;

import game.Game;

public class ConsoleUI {

	private Scanner scan = new Scanner(System.in);

	public void start(Game game) {
		System.out.print("How many players ? (2) : ");
		int nPlayers;
		try {
			nPlayers = Integer.parseInt(scan.nextLine());
		} catch (NumberFormatException nfe) {
			nPlayers = 2;
		}
		for (int i = 1; i <= nPlayers; i++) {
			System.out.printf("Enter Player %d name? (Player %d) : ", i, i);
			String name = scan.nextLine();
			name = name.equals("") ? "Player " + i : name;
			game.addPlayer(name);
		}
		game.start();
		while (game.isPlaying()) {
			System.out.println("-----------------");
			System.out.println(game.currentPlayerName());
			System.out.println("Position : " + game.currentPlayerPosition());
			System.out.println("Please hit enter to roll a die.");

			scan.nextLine();

			int face = game.currentPlayerRollDice();
			System.out.println("Die face = " + face);
			game.currentPlayerMove(face);
			System.out.println("Position : " + game.currentPlayerPosition());
			if (game.currentPlayerWin()) {
				System.out.println(game.currentPlayerName() + " win");
				System.out.println("Game win");
				game.end();
			} else {
				game.switchPlayer();
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		new ConsoleUI().start(game);
		game.setReplayMode(true);
		System.out.println("This is replay");
		game.restart();
		new ConsoleUI().start(game);
	}
}

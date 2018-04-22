package application;

import java.util.Scanner;

import game.Game;

public class ConsoleUI {

	private Scanner scan = new Scanner(System.in);

	public void start(Game game) {
		game.addPlayer("P1");
		game.addPlayer("P2");
		game.start();
		while (!game.isEnd()) {
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
		game.restart();
		new ConsoleUI().start(game);
	}
}

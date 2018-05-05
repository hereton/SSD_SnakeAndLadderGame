package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import game.replay.*;
import game.tile.TileType;

public class Game {

	private List<Player> players;
	private Die die;
	private Board board;
	private boolean isPlaying;
	private int currentPlayerIndex;

	private List<Action> replay = new ArrayList<>();
	private List<Action> lastReplay;

	public Game() {
		die = new Die();
		board = new Board();
		players = new ArrayList<>();
		initSpecialTile(board);
	}

	public void start() {
		for (Player player : players) {
			board.addPiece(player.getPiece(), 0);
		}
		currentPlayerIndex = 0;
		isPlaying = true;
	}

	public void addObserver(Observer o) {
		this.board.addObserver(o);
	}

	private void initSpecialTile(Board board) {
		int[][] ladders = new int[][] { { 1, 37 }, { 3, 13 }, { 8, 30 }, { 32, 84 }, { 51, 87 }, { 79, 98 } };
		int[][] snakes = new int[][] { { 50, 10 }, { 55, 14 }, { 61, 56 }, { 91, 52 }, { 97, 7 } };
		int[] stops = new int[] { 6, 27, 40, 63, 77, 89 };
		int[] backwards = new int[] { 16, 38, 85, 93 };

		for (int[] ladder : ladders) {
			board.addSpecialTile(TileType.LADDER, ladder[0], ladder[1]);
		}

		for (int[] snake : snakes) {
			board.addSpecialTile(TileType.SNAKE, snake[0], snake[1]);
		}

		for (int stop : stops) {
			board.addSpecialTile(TileType.FREEZE, stop, 1);
		}

		for (int backward : backwards) {
			board.addSpecialTile(TileType.BACKWARD, backward, 0);
		}
	}

	public void restart() {
		die = new Die();
		board = new Board();
		players = new ArrayList<>();
		replay = new ArrayList<>();
		currentPlayerIndex = 0;
		isPlaying = false;
		initSpecialTile(board);
	}

	public void addPlayer(String name) {
		players.add(new Player(name));
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void end() {
		isPlaying = false;
		lastReplay = new ArrayList<>();
		for (Action c : replay) {
			lastReplay.add(c);
		}
	}

	public Player currentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public int switchPlayer() {
		return currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	public void currentPlayerMove(int steps) {
		int nextMove = currentPlayerPosition();
		this.board.movePiece(currentPlayer().getPiece(), steps);
		nextMove = currentPlayerPosition() - nextMove;
		replay.add(new MoveAction(currentPlayer(), nextMove, die.getFace()));
	}

	public String currentPlayerName() {
		return currentPlayer().getName();
	}

	public int currentPlayerPosition() {
		return board.getPiecePosition(currentPlayer().getPiece());
	}

	public int currentPlayerRollDice() {
		return currentPlayer().roll(die);
	}

	public Iterator<Action> getLastReplay() {
		return new Iterator<Action>() {
			private int pointer = 0;

			@Override
			public boolean hasNext() {
				return pointer != (lastReplay.size());
			}

			@Override
			public Action next() {
				Action ac = lastReplay.get(pointer);
				pointer++;
				return ac;
			}

			public void reset() {
				this.pointer = 0;
			}
		};
	}

	public boolean currentPlayerWin() {
		return board.pieceIsAtGoal(currentPlayer().getPiece());
	}

	public int getPlayerSize() {
		return this.players.size();
	}

	public String getPlayersName(int index) {
		return players.get(index).getName();
	}
}

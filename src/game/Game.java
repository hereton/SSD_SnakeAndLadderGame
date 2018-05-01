package game;

import java.util.ArrayList;
import java.util.List;

import replay.*;
import tile.TileType;

public class Game {

	private List<Player> players;
	private Die die;
	private Board board;
	private boolean ended;
	private int currentPlayerIndex;

	// replay part
	private boolean isReplayMode = false;
	private List<Action> replay = new ArrayList<>();

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
		ended = false;
	}

	private void initSpecialTile(Board board) {
		int[][] ladders = new int[][] { { 2, 38 }, { 4, 14 }, { 9, 31 }, { 33, 85 }, { 52, 88 }, { 80, 99 } };
		int[][] snakes = new int[][] { { 51, 11 }, { 56, 15 }, { 62, 57 }, { 92, 53 }, { 98, 8 } };
		int[] stops = new int[] { 7, 26, 41, 64, 78, 90 };
		int[] backwards = new int[] { 17, 39, 86, 94 };

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
		if (!isReplayMode)
			replay = new ArrayList<>();
	}

	public void setReplayMode(boolean isReplayMode) {
		this.isReplayMode = isReplayMode;
	}

	public void addPlayer(String name) {
		players.add(new Player(name));
	}

	public boolean isEnd() {
		return ended;
	}

	public void end() {
		ended = true;
	}

	public Player currentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public int switchPlayer() {
		return currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	public void currentPlayerMove(int steps) {
		this.board.movePiece(currentPlayer().getPiece(), steps);
	}

	public String currentPlayerName() {
		return currentPlayer().getName();
	}

	public int currentPlayerPosition() {
		return board.getPiecePosition(currentPlayer().getPiece());
	}

	public int currentPlayerRollDice() {
		if (!isReplayMode) {
			RollAction action = new RollAction(currentPlayer(), die);
			action.Execute();
			replay.add(action);
			return action.getDieFace();
		} else {
			RollAction action = (RollAction) replay.get(0);
			replay.remove(0);
			return action.getDieFace();
		}
	}

	public boolean currentPlayerWin() {
		return board.pieceIsAtGoal(currentPlayer().getPiece());
	}

	public int getPlayerSize() {
		return this.players.size();
	}
}

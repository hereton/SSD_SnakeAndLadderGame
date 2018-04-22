package game;

import java.util.ArrayList;
import java.util.List;

import replay.*;

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
	}

	public void start() {
		for (Player player : players) {
			board.addPiece(player.getPiece(), 0);
		}
		currentPlayerIndex = 0;
		ended = false;
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

	public void switchPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
}

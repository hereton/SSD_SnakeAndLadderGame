package game.replay;

import game.Player;

public class MoveAction extends Action {
	private Player currentPlayer;
	private int stepmove;
	private int dieFace;

	public MoveAction(Player currentPlayer, int stepmove, int dieFace) {
		this.currentPlayer = currentPlayer;
		this.stepmove = stepmove;
		this.dieFace = dieFace;
	}

	public int getDieFace() {
		return dieFace;
	}

	@Override
	public String getPlayerName() {
		return currentPlayer.getName();
	}

	@Override
	public int getStepMove() {
		return this.stepmove;
	}
}

package replay;

import game.Die;
import game.Player;

public class RollAction extends Action {
	private Player currentPlayer;
	private Die die;
	private int dieFace;

	public RollAction(Player currentPlayer, Die die) {
		this.currentPlayer = currentPlayer;
		this.die = die;
	}

	public int getDieFace() {
		return dieFace;
	}

	@Override
	public String getPlayerName() {
		return currentPlayer.getName();
	}

	@Override
	public void Execute() {
		dieFace = currentPlayer.roll(die);
	}

}

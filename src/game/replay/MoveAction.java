package game.replay;

public class MoveAction extends Action {
	private String currentPlayer;
	private int stepmove;
	private int dieFace;

	public MoveAction(String currentPlayer, int stepmove, int dieFace) {
		this.currentPlayer = currentPlayer;
		this.stepmove = stepmove;
		this.dieFace = dieFace;
	}

	public int getDieFace() {
		return dieFace;
	}

	@Override
	public String getPlayerName() {
		return currentPlayer;
	}

	@Override
	public int getStepMove() {
		return this.stepmove;
	}
}

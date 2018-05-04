package game.tile;

public class Snake extends Square {
	private int stepbacks;

	public Snake(int number, int stepbacks) {
		super(number);
		this.stepbacks = stepbacks;
		super.tileType = TileType.SNAKE;
		super.isSpecialEffect = true;
	}

	@Override
	public int moveAccordingToSpecialEffect() {
		return nextMove(stepbacks);
	}

	public int desination() {
		return super.getNumber() + stepbacks;
	}
}

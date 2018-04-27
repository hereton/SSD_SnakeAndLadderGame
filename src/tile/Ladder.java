package tile;

public class Ladder extends Square {
	private int stepforwards;

	public Ladder(int number, int stepforwards) {
		super(number);
		this.stepforwards = stepforwards;
		super.tileType = TileType.LADDER;
		isSpecialEffect = true;
	}

	@Override
	public int moveAccordingToSpecialEffect() {
		return nextMove(stepforwards);
	}

	@Override
	public int nextMove(int steps) {
		return super.nextMove(steps);
	}

}

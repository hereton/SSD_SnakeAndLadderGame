package tile;

public class Backward extends Square {

	public Backward(int number) {
		super(number);
		super.tileType = TileType.BACKWARD;
	}

	@Override
	public int nextMove(int steps) {
		// mean backward
		return steps * (-1);
	}
}

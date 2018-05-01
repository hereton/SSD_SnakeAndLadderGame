package tile;

import java.util.HashMap;
import java.util.Map;

import game.Piece;

public class Freeze extends Square {
	private Map<Piece, Integer> freezes = new HashMap<>();
	private int turn;

	public Freeze(int number, int turn) {
		super(number);
		this.turn = turn;
		super.tileType = TileType.FREEZE;
	}

	@Override
	public void addPiece(Piece piece) {
		super.addPiece(piece);
		this.freezes.put(piece, this.turn);
	}

	public void move(Piece piece) {
		int currentTurnLeft = freezes.get(piece);
		currentTurnLeft -= 1;
		if (currentTurnLeft >= 0)
			freezes.put(piece, currentTurnLeft);
	}

	public int getTurnLeft(Piece piece) {
		return freezes.get(piece);
	}

}

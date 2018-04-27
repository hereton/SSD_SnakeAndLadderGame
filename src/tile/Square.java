package tile;

import java.util.ArrayList;
import java.util.List;

import game.Piece;

public class Square {

	protected TileType tileType;
	private List<Piece> pieces;
	private int number;
	private boolean goal;

	public Square(int number) {
		this.number = number;
		this.tileType = TileType.SQUARE;
		pieces = new ArrayList<Piece>();
		goal = false;
	}

	// this is activated when players move in
	protected boolean isSpecialEffect = false;

	public boolean isSpecialEffect() {
		return isSpecialEffect;
	}

	public int moveAccordingToSpecialEffect() {
		return 0;
	}

	public TileType getTileType() {
		return tileType;
	}

	public int nextMove(int steps) {
		return steps;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	public void addPiece(Piece piece) {
		pieces.add(piece);
	}

	public void removePiece(Piece piece) {
		pieces.remove(piece);
	}

	public boolean hasPiece(Piece piece) {
		return pieces.contains(piece);
	}

	public boolean isGoal() {
		return goal;
	}

	public int getNumber() {
		return number;
	}
}

package game;

import tile.Freeze;
import tile.Square;
import tile.TileType;

public class Board {

	public static final int SIZE = 100;

	private Square[] squares;

	public Board() {
		squares = new Square[Board.SIZE];
		for (int i = 0; i < squares.length; i++) {
			squares[i] = new Square(i);
		}
	}

	public void addSpecialTile(TileType tileType, int square) {
		
	}

	public void addPiece(Piece piece, int position) {
		squares[position].addPiece(piece);
	}

	public void movePiece(Piece piece, int steps) {
		int currentPosition = getPiecePosition(piece);
		TileType currentTileType = squares[currentPosition].getTileType();

		if (currentTileType == TileType.FREEZE) {
			Freeze fs = (Freeze) squares[currentPosition];
			fs.move(piece);
			if (fs.getTurnLeft(piece) > 0) {
				System.out.println("You are freezed : " + fs.getTurnLeft(piece) + " turn left");
				return;
			}
		}

		int nextPosition = squares[currentPosition].nextMove(steps) + currentPosition;
		squares[currentPosition].removePiece(piece);

		// CHECK POSITION OVERLAP
		if (nextPosition > SIZE - 1) {
			int overlap = nextPosition % (SIZE - 1);
			nextPosition = (SIZE - 1) - overlap;
			System.out.println(nextPosition);
		}

		Square nextSquare = squares[nextPosition];
		if (nextSquare.isSpecialEffect()) {
			System.out.println("You move in " + nextSquare.getTileType().toString() + " Tile");
			System.out.println("Move to " + nextSquare.moveAccordingToSpecialEffect() + " Position");
			nextPosition += nextSquare.moveAccordingToSpecialEffect();
		}

		squares[nextPosition].addPiece(piece);

		if (nextPosition == SIZE - 1) {
			squares[nextPosition].setGoal(true);
		}
	}

	public int getPiecePosition(Piece piece) {
		for (Square s : squares) {
			if (s.hasPiece(piece))
				return s.getNumber();
		}
		return -1;
	}

	public boolean pieceIsAtGoal(Piece piece) {
		return squares[getPiecePosition(piece)].isGoal();
	}
}

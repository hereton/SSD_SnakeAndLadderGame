package game;

import java.util.InputMismatchException;
import java.util.List;

import tile.Backward;
import tile.Freeze;
import tile.Ladder;
import tile.Snake;
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
		squares[5] = new Freeze(0, 2);
	}

	/**
	 * 
	 * @param tileType
	 * @param position
	 * @param desination
	 *            if backward , put any number , if freeze , put number of turn
	 *            freeze
	 */
	public void addSpecialTile(TileType tileType, int position, int desination) {
		Square square;
		switch (tileType) {
		case SNAKE:
			square = new Snake(position, (position - desination) * -1);
			break;
		case FREEZE:
			square = new Freeze(position, desination);
			break;
		case BACKWARD:
			square = new Backward(position);
			break;
		case LADDER:
			square = new Ladder(position, desination - position);
			break;
		default:
			square = new Square(position);
		}
		squares[position] = square;
	}

	public void addSpecialTile(List<Square> squares, int... positions) {
		if (squares.size() != positions.length) {
			throw new InputMismatchException("size of squares must be the same as size of positions");
		}
		for (int i = 0; i < squares.size(); i++) {
			this.squares[positions[i]] = squares.get(i);
		}
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
		if (squares[nextPosition].getTileType() != TileType.SQUARE) {
			System.out.println("You fall in " + squares[nextPosition].getTileType() + " Block");
		}

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

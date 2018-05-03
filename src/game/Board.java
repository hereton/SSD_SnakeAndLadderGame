package game;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Observable;

import tile.Backward;
import tile.Freeze;
import tile.Ladder;
import tile.Snake;
import tile.Square;
import tile.TileType;

public class Board extends Observable {

	public static final int SIZE = 100;
	private Square[] squares;

	public Board() {
		squares = new Square[Board.SIZE];
		for (int i = 0; i < squares.length; i++) {
			squares[i] = new Square(i);
		}
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
				setChanged();
				notifyObservers("You are freezed");
				return;
			}
		}

		int nextPosition = squares[currentPosition].nextMove(steps) + currentPosition;
		squares[currentPosition].removePiece(piece);

		// CHECK POSITION OVERLAP
		if (nextPosition > SIZE - 1) {
			int overlap = nextPosition % (SIZE - 1);
			nextPosition = (SIZE - 1) - overlap;
		}

		Square nextSquare = squares[nextPosition];

		String status = "";
		switch (nextSquare.getTileType()) {
		case LADDER:
			Ladder ld = (Ladder) nextSquare;
			status = "You fall in ladder from " + (nextSquare.getNumber() + 1) + " to " + (ld.desination() + 1);
			break;
		case BACKWARD:
			status = "You fall in backward, you will move backward next turn";
			break;
		case FREEZE:
			status = "You fall in skip, you will be skipped next turn";
			break;
		case SNAKE:
			Snake s = (Snake) nextSquare;
			status = "You fall in snake from " + (nextSquare.getNumber() + 1) + " to " + (s.desination() + 1);
			break;
		default:
		}
		setChanged();
		notifyObservers(status);

		if (nextSquare.isSpecialEffect()) {
			nextPosition += nextSquare.moveAccordingToSpecialEffect();
		}

		squares[nextPosition].addPiece(piece);
		if (squares[nextPosition].getTileType() != TileType.SQUARE) {
			// System.out.println("You fall in " + squares[nextPosition].getTileType() + "
			// Block");
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

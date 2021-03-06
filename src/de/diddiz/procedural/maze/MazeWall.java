package de.diddiz.procedural.maze;

/**
 * Wall inside a maze. Has a starting and an end point. May be an inner or an outer wall.
 *
 * @author DiddiZ
 */
public class MazeWall
{
	private final int fromX, fromY, toX, toY;

	MazeWall(int fromX, int fromY, int toX, int toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}
}
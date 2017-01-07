package de.diddiz.procedural.maze;

/**
 * @author DiddiZ
 */
public class MazeCell
{
	public final int x, y;
	private MazeCell group;
	boolean hasRightNeighbor, hasDownNeighbor;

	MazeCell(int x, int y) {
		this.x = x;
		this.y = y;
		group = this;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	MazeCell findGroup() {
		while (group.group != group) // Path compression
			group = group.group;

		return group;
	}

	void union(MazeCell other) {
		other.findGroup().group = findGroup();
	}
}
package de.diddiz.procedural.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.ToDoubleFunction;

/**
 * @author DiddiZ
 */
public class MazeGenerator
{
	private final int width, height;
	final MazeCell[] cells;

	private ToDoubleFunction<MazePath> priorityFunction;

	private MazeGenerator(int width, int height) {
		this.width = width;
		this.height = height;

		cells = new MazeCell[width * height];
	}

	public void setPriorityFunction(ToDoubleFunction<MazePath> priorityFunction) {
		this.priorityFunction = priorityFunction;
	}

	private PriorityQueue<MazePath> createPathQueue() {
		// Set default priority function if neccessary
		if (priorityFunction == null)
			priorityFunction = MazeGeneratorPriorities.random(new Random());

		final int numPaths = 2 * width * height - width - height;
		final PriorityQueue<MazePath> pathQueue = new PriorityQueue<>(numPaths, (p1, p2) -> -Double.compare(p1.priority, p2.priority));

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				// Horizontal
				if (x < width - 1)
					pathQueue.add(new MazePath(cells[y * width + x], cells[y * width + x + 1], priorityFunction));
				// Vertical
				if (y < height - 1)
					pathQueue.add(new MazePath(cells[y * width + x], cells[(y + 1) * width + x], priorityFunction));
			}

		return pathQueue;
	}

	/**
	 * Constructs a spanning tree covering all cells
	 */
	private void generate() {
		// Initialize cells
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cells[y * width + x] = new MazeCell(x, y);

		final PriorityQueue<MazePath> pathQueue = createPathQueue();
		int groups = width * height; // Every node is it's own group
		while (!pathQueue.isEmpty()) {
			final MazePath path = pathQueue.poll();
			if (path.from.findGroup() != path.to.findGroup()) { // Check if nodes are in different groups
				// Connect nodes
				if (path.from.x != path.to.x)
					path.from.hasRightNeighbor = true;
				else
					path.from.hasDownNeighbor = true;

				// Join components
				path.from.union(path.to);
				groups--;

				// Check if all nodes are covered
				if (groups == 1)
					break;
			}
		}
	}

	private List<MazeWall> getWalls() {
		final List<MazeWall> walls = new ArrayList<>();

		// Outer walls
		walls.add(new MazeWall(0, 0, width, 0));
		walls.add(new MazeWall(0, 0, 0, height));
		walls.add(new MazeWall(0, height, width, height));
		walls.add(new MazeWall(width, 0, width, height));

		// Inner walls
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				final MazeCell c = cells[y * width + x];
				if (!c.hasRightNeighbor && x < width - 1)
					walls.add(new MazeWall(c.x + 1, c.y, c.x + 1, c.y + 1));
				if (!c.hasDownNeighbor & y < height - 1)
					walls.add(new MazeWall(c.x, c.y + 1, c.x + 1, c.y + 1));
			}

		return walls;
	}

	public static List<MazeWall> generateMaze(int width, int height, ToDoubleFunction<MazePath> priorityFunction) {
		final MazeGenerator gen = new MazeGenerator(width, height);
		gen.setPriorityFunction(priorityFunction);

		gen.generate();

		return gen.getWalls();
	}
}

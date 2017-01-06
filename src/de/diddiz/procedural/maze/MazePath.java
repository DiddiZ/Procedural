package de.diddiz.procedural.maze;

import java.util.function.ToDoubleFunction;

public class MazePath
{
	public final MazeCell from, to;
	final double priority;

	MazePath(MazeCell from, MazeCell to, ToDoubleFunction<MazePath> priorityFunction) {
		this.from = from;
		this.to = to;
		priority = priorityFunction.applyAsDouble(this);
	}

	@Override
	public String toString() {
		return from + "->" + to;
	}
}
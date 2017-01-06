package de.diddiz.procedural.maze;

import java.util.Random;
import java.util.function.ToDoubleFunction;

public final class MazeGeneratorPriorities
{
	/**
	 * Generates a squares pattern
	 *
	 * @param dist
	 *        Size of each square
	 */
	public static ToDoubleFunction<MazePath> box(int dist) {
		final int d = dist;
		return m -> m.from.x % d == d - 1 && m.to.x % d == 0
				|| m.from.y % d == d - 1 && m.to.y % d == 0
						? 0 : 1;
	}

	/**
	 * Constant priority
	 */
	public static ToDoubleFunction<MazePath> constant(double value) {
		return m -> value;
	}

	/**
	 * Interpolates between two mazes
	 */
	public static ToDoubleFunction<MazePath> interpolate(ToDoubleFunction<MazePath> func1, ToDoubleFunction<MazePath> func2, double weight) {
		return m -> interpolate(func1.applyAsDouble(m), func2.applyAsDouble(m), weight);
	}

	/**
	 * Interpolates between two mazes
	 */
	public static ToDoubleFunction<MazePath> interpolate(ToDoubleFunction<MazePath> func1, ToDoubleFunction<MazePath> func2, ToDoubleFunction<MazePath> weight) {
		return m -> interpolate(func1.applyAsDouble(m), func2.applyAsDouble(m), weight.applyAsDouble(m));
	}

	public static ToDoubleFunction<MazePath> multiply(ToDoubleFunction<MazePath> func1, ToDoubleFunction<MazePath> func2) {
		return m -> func1.applyAsDouble(m) * func2.applyAsDouble(m);
	}

	/**
	 * Generates a completely random maze
	 */
	public static ToDoubleFunction<MazePath> random(Random rnd) {
		return m -> rnd.nextDouble();
	}

	/**
	 * Scales priorities with a random value
	 */
	public static ToDoubleFunction<MazePath> randomize(ToDoubleFunction<MazePath> func, Random rnd) {
		return m -> func.applyAsDouble(m) * rnd.nextDouble();
	}

	private static double interpolate(double a, double b, double f) {
		return a + (b - a) * f;
	}
}

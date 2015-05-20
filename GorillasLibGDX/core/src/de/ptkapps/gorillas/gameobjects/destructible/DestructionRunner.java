package de.ptkapps.gorillas.gameobjects.destructible;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.badlogic.gdx.graphics.Pixmap;

import de.ptkapps.gorillas.gameobjects.destructible.pattern.ImageDestructionPattern;

public class DestructionRunner implements Callable<Object> {

	private int startY;
	private int endY;
	private int startX;
	private int endX;
	private int negX;
	private int negY;
	private Pixmap result;
	private ImageDestructionPattern activeDestructionPattern;

	public DestructionRunner(int startY, int endY, int startX, int endX,
			int negX, int negY, Pixmap result,
			ImageDestructionPattern activeDestructionPattern) {

		this.startY = startY;
		this.endY = endY;
		this.startX = startX;
		this.endX = endX;
		this.negX = negX;
		this.negY = negY;
		this.result = result;
		this.activeDestructionPattern = activeDestructionPattern;
	}

	@Override
	public Object call() {

		for (int y = startY; y < endY; y++) {

			for (int x = startX; x < endX; x++) {

				if (activeDestructionPattern.shouldErase(x - startX + negX, y
						- startY + negY)) {

					result.drawPixel(x, y);
				}
			}
		}

		return null;
	}

	public static void runDestruction(int startY, int endY, int startX,
			int endX, int negX, int negY, Pixmap result,

			ImageDestructionPattern activeDestructionPattern) {

		int numThreads = Runtime.getRuntime().availableProcessors();

		List<Callable<Object>> callables = new ArrayList<Callable<Object>>();

		// add two callables, so maximum 2 cores are used
		callables
				.add(new DestructionRunner(startY,
						startY + (endY - startY) / 2, startX, endX, negX, negY,
						result, activeDestructionPattern));
		callables.add(new DestructionRunner(startY + (endY - startY) / 2, endY,
				startX, endX, negX, negY + (endY - startY) / 2, result,
				activeDestructionPattern));

		ExecutorService service = Executors.newFixedThreadPool(numThreads);

		try {
			List<Future<Object>> results = service.invokeAll(callables);

			results.get(0);
			results.get(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		service.shutdownNow();
		return;
	}
}

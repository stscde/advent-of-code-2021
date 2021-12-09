import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Advent of Code 2021/12/06 Part 1
 */
public class AOC202106 {

	private static int SIMULATE_DAYS = 80;

	private static int FISH_RESPAWN_AGED = 6;

	private static int FISH_RESPAWN_NEW = 8;

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202106.class.getResourceAsStream("AOC202106_input.txt")));
		String[] parts = br.readLine().split(",");
		br.close();

		List<AtomicInteger> fishes = new ArrayList<AtomicInteger>();
		Arrays.asList(parts).forEach(a -> fishes.add(new AtomicInteger(Integer.parseInt(a))));
		System.out.println("detected fishes: " + fishes.size());

		for (int i = 0; i < SIMULATE_DAYS; i++) {
			List<AtomicInteger> newFishes = new ArrayList<AtomicInteger>();

			for (AtomicInteger fish : fishes) {
				int daysLeft = fish.get();
				if (daysLeft == 0) {
					fish.set(FISH_RESPAWN_AGED);
					newFishes.add(new AtomicInteger(FISH_RESPAWN_NEW));
				}
				else {
					fish.decrementAndGet();
				}
			}

			fishes.addAll(newFishes);
		}

		System.out.println("number of fish after [" + SIMULATE_DAYS + "] days: " + fishes.size());
	}

}

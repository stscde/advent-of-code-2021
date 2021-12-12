import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Advent of Code 2021/12/07 Part 2
 */
public class AOC202107Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202107Part2.class.getResourceAsStream("AOC202107_input.txt")));
		String[] parts = br.readLine().split(",");
		br.close();

		int[] crabs = new int[parts.length];
		int minPos = Integer.MAX_VALUE;
		int maxPos = 0;
		for (int c = 0; c < crabs.length; c++) {
			Integer crab = Integer.parseInt(parts[c]);
			crabs[c] = crab;
			minPos = Math.min(minPos, crab);
			maxPos = Math.max(maxPos, crab);
		}
		System.out.println("detected crabs: " + crabs.length + ", minPos: " + minPos + ", maxPos: "  +maxPos);

		int bestPosition = 0;
		int leastRequiredFuel = Integer.MAX_VALUE;

		for (int p = minPos; p <= maxPos; p++) {
			int positionRequiredFuel = 0;
			for (int c = 0; c < crabs.length; c++) {
				int n = Math.abs(p - crabs[c]);

				if (n > 0) {
					// gaussian sum formula
					positionRequiredFuel += (n * (n + 1)) / 2;
				}
			}

			if (positionRequiredFuel < leastRequiredFuel) {
				bestPosition = p;
				leastRequiredFuel = positionRequiredFuel;
			}

			System.out.println("position: " + p + ", positionRequiredFuel: " + positionRequiredFuel);
		}

		System.out.println("bestPosition: " + bestPosition + ", leastRequiredFuel: " + leastRequiredFuel);

	}

}

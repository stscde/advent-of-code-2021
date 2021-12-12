import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


/**
 * Advent of Code 2021/12/07 Part 1
 */
public class AOC202107 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202107.class.getResourceAsStream("AOC202107_input.txt")));
		String[] parts = br.readLine().split(",");
		br.close();

		int[] crabs = new int[parts.length];
		Set<Integer> knownPositions = new HashSet<Integer>();
		for (int c = 0; c < crabs.length; c++) {
			Integer crab = Integer.parseInt(parts[c]);
			crabs[c] = crab;
			knownPositions.add(crab);
		}
		System.out.println("detected crabs: " + crabs.length + ", detected positions: " + knownPositions.size());

		int bestPosition = 0;
		int leastRequiredFuel = Integer.MAX_VALUE;

		for (int p : knownPositions) {
			int positionRequiredFuel = 0;
			for (int c = 0; c < crabs.length; c++) {
				positionRequiredFuel += Math.abs(p - crabs[c]);
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

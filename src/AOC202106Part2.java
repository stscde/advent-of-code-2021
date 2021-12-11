import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Advent of Code 2021/12/06 Part 2<br>
 * NOTICE: Requires about 9 GB of RAM<br>
 * Probably there is a smarter solution by creating a growth rate formula but this way was quicker for me :-)
 */
public class AOC202106Part2 {

	private static int SIMULATE_DAYS = 256;

	private static byte FISH_RESPAWN_AGED = 6;

	private static byte FISH_RESPAWN_NEW = 8;

	public static void main(String[] args) throws IOException {

		// calculate 0 day fish
		final int F_COLS = 1000000;
		final int F_ROWS = 7000;

		System.out.println("allocate fish memory: " + new Date());

		byte[][] fishes = new byte[F_ROWS][F_COLS];

		System.out.println("fish memory allocated: " + new Date());

		int fOldR = 0;
		int fOldC = 0;

		int fNewR = 0;
		int fNewC = 0;

		List<Long> fishesPerDay = new ArrayList<Long>();

		for (int day = 0; day < SIMULATE_DAYS; day++) {

			for (int r = 0; r <= fOldR; r++) {
				int maxC = r != fOldR ? F_COLS - 1 : fOldC;
				for (int c = 0; c <= maxC; c++) {
					if (fishes[r][c] == 0) {
						fishes[r][c] = FISH_RESPAWN_AGED;

						// create new fish
						fNewC++;
						if (fNewC >= F_COLS) {
							fNewC = 0;
							fNewR++;
							// System.out.println("fNewR: " + fNewR);
						}

						// System.out.println("fNewC: " + fNewC);
						fishes[fNewR][fNewC] = FISH_RESPAWN_NEW;
					} else {
						fishes[r][c]--;
					}
				}
			}

			long fishCount = (fNewC + 1) + (fNewR > 0 ? fNewR * (long) F_COLS : 0);
			System.out.println("day: " + (day + 1) + ", fOldR: " + fOldR + ", fOldC: " + fOldC + ", fNewR: " + fNewR + ", fNewC: " + fNewC + ", date: " + new Date() + ", fishCount: " + fishCount);
			fishesPerDay.add(fishCount);

			fOldR = fNewR;
			fOldC = fNewC;
		}

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202106.class.getResourceAsStream("AOC202106_input.txt")));
		String[] parts = br.readLine().split(",");
		br.close();

		List<Integer> startFishes = new ArrayList<Integer>();
		Arrays.asList(parts).forEach(a -> startFishes.add(Integer.parseInt(a)));
		System.out.println("detected fishes: " + startFishes.size());

		long totalFishCount = 0;
		for (Integer fish : startFishes) {

			int fishIdx = fishesPerDay.size() - 1 - fish.intValue();
			totalFishCount += fishesPerDay.get(fishIdx);
		}

		System.out.println("total fishes after [" + SIMULATE_DAYS + "] days: " + totalFishCount);
	}

}

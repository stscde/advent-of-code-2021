import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Advent of Code 2021/12/04 Part 1
 */
public class AOC202104Part2 {

	public static void main(String[] args) throws IOException {

		String[] draws = null;
		List<int[][]> fields = new ArrayList<int[][]>();
		List<int[][]> hits = new ArrayList<int[][]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202104Part2.class.getResourceAsStream("AOC202104_input.txt")));

		String line = null;
		int[][] currField = new int[0][0];
		int currFieldLine = 0;
		while ((line = br.readLine()) != null) {

			// line with draws is first line
			if (draws == null) {
				draws = line.split(",");
				continue;
			}

			// new field starts
			if (line.isEmpty()) {
				currFieldLine = 0;
				currField = new int[5][5];
				fields.add(currField);
				hits.add(new int[5][5]);
				continue;
			}

			// line in field
			String[] parts = line.replaceAll("  ", " ").trim().split(" ");
			for (int y = 0; y < parts.length; y++) {
				currField[currFieldLine][y] = Integer.parseInt(parts[y].trim());
			}

			currFieldLine++;
		}
		br.close();

		// play
		int[][] winningField = null;
		int[][] winningHits = null;
		int winningDraw = 0;
		Set<Integer> wonFields = new HashSet<Integer>();

		for (String drawStr : draws) {
			int drawInt = Integer.parseInt(drawStr);

			FIELD: for (int f = 0; f < fields.size(); f++) {
				if (wonFields.contains(Integer.valueOf(f))) {
					System.out.println("field already won: " + f);
					continue;
				}

				int[][] playField = fields.get(f);
				int[][] playHits = hits.get(f);

				for (int y = 0; y < playField.length; y++) {
					for (int x = 0; x < playField[y].length; x++) {

						if (playField[y][x] == drawInt) {
							playHits[y][x]++;
						}

						// victory check
						for (int vcY = 0; vcY < playField.length; vcY++) {
							boolean rowComplete = true;
							for (int vcX = 0; vcX < playField[vcY].length; vcX++) {
								if (playHits[vcY][vcX] < 1) {
									rowComplete = false;
								}
							}

							if (rowComplete) {
								winningField = playField;
								winningHits = playHits;
								wonFields.add(Integer.valueOf(f));
								winningDraw = drawInt;
								continue FIELD;
							}
						}

						for (int vcX = 0; vcX < playField[0].length; vcX++) {
							boolean colComplete = true;
							for (int vcY = 0; vcY < playField.length; vcY++) {
								if (playHits[vcY][vcX] < 1) {
									colComplete = false;
								}
							}

							if (colComplete) {
								winningField = playField;
								winningHits = playHits;
								wonFields.add(Integer.valueOf(f));
								winningDraw = drawInt;
								continue FIELD;
							}
						}
					}
				}
			}
		}

		int sumUnmarkedFields = 0;
		for (int y = 0; y < winningField.length; y++) {
			for (int x = 0; x < winningField[y].length; x++) {
				if (winningHits[y][x] < 1) {
					sumUnmarkedFields += winningField[y][x];
				}
			}
		}

		System.out.println("draw count: " + draws.length + ", fields count: " + fields.size() + ", sumUnmarkedFields: " + sumUnmarkedFields + ", winningDraw: " + winningDraw + ", multiplied: " + winningDraw * sumUnmarkedFields);
	}

}

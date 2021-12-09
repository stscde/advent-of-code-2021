import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/04 Part 1
 */
public class AOC202104 {

	public static void main(String[] args) throws IOException {

		String[] draws = null;
		List<int[][]> fields = new ArrayList<int[][]>();
		List<int[][]> hits = new ArrayList<int[][]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202104.class.getResourceAsStream("AOC202104_input.txt")));

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
		int drawInt = 0;
		DRAW: for (String drawStr : draws) {
			drawInt = Integer.parseInt(drawStr);

			for (int f = 0; f < fields.size(); f++) {
				int[][] playField = fields.get(f);
				int[][] playHits = hits.get(f);

				for (int y = 0; y < playField.length; y++) {
					for (int x = 0; x < playField[y].length; x++) {

						if (playField[y][x] == drawInt) {
							playHits[y][x]++;
						}
					}
				}

				// victory check
				for (int y = 0; y < playField.length; y++) {
					boolean rowComplete = true;
					for (int x = 0; x < playField[y].length; x++) {
						if (playHits[y][x] < 1) {
							rowComplete = false;
						}
					}

					if (rowComplete) {
						winningField = playField;
						winningHits = playHits;
						break DRAW;
					}
				}

				for (int x = 0; x < playField[0].length; x++) {
					boolean colComplete = true;
					for (int y = 0; y < playField.length; y++) {
						if (playHits[y][x] < 1) {
							colComplete = false;
						}
					}

					if (colComplete) {
						winningField = playField;
						winningHits = playHits;
						break DRAW;
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

		System.out.println("draw count: " + draws.length + ", fields count: " + fields.size() + ", sumUnmarkedFields: " + sumUnmarkedFields + ", drawInt: " + drawInt + ", multiplied: " + drawInt * sumUnmarkedFields);
	}

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/08 Part 1
 */
public class AOC202108 {

	public static void main(String[] args) throws IOException {

		// read input
		List<String[][]> lines = new ArrayList<String[][]>();
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202108.class.getResourceAsStream("AOC202108_input.txt")));

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\\|");

			String[] input = parts[0].trim().split(" ");
			String[] output = parts[1].trim().split(" ");

			lines.add(new String[][] {input, output});
		}
		br.close();

		// 2, 3, 4, 7
		int matches = 0;
		for (String[][] lineX : lines) {
			for (String lineOutput : lineX[1]) {
				int l = lineOutput.length();
				if (l == 2 || l == 3 || l == 4 || l == 7) {
					matches++;
				}
			}
		}

		System.out.println("matching digits found: " + matches);
	}

}

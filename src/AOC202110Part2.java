import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Advent of Code 2021/12/10 Part 1
 */
public class AOC202110Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202110Part2.class.getResourceAsStream("AOC202110_input.txt")));
		String line = null;
		List<String[]> lines = new ArrayList<String[]>();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");
			lines.add(parts);
		}
		br.close();

		// open to close char map
		Map<String, String> otc = new HashMap<String, String>();
		otc.put("(", ")");
		otc.put("<", ">");
		otc.put("{", "}");
		otc.put("[", "]");

		// remaining character score map
		Map<String, Integer> rcm = new HashMap<String, Integer>();
		rcm.put(")", Integer.valueOf(1));
		rcm.put("]", Integer.valueOf(2));
		rcm.put("}", Integer.valueOf(3));
		rcm.put(">", Integer.valueOf(4));

		// calculate remaining line scores
		List<Long> lineScores = new ArrayList<Long>();
		LINE: for (String[] currLine : lines) {
			List<String> ec = new ArrayList<String>(); // expected close chars
			for (String c : currLine) {

				// open char?
				if (otc.containsKey(c)) {
					ec.add(otc.get(c));
				}
				// close char?
				else {
					if (ec.get(ec.size() - 1).equals(c)) {
						ec.remove(ec.size() - 1);
					} else {
						// System.out.println("corrupted line: " + String.join("", currLine));
						continue LINE;
					}
				}
			}
			Collections.reverse(ec);
			System.out.println("incomplete line: " + String.join("", currLine) + ", remaining: " + String.join("", ec));

			long lineScore = 0;
			for (String c : ec) {
				lineScore *= 5;
				lineScore += rcm.get(c);
			}
			lineScores.add(Long.valueOf(lineScore));
		}

		// find middle score
		Collections.sort(lineScores);

		System.out.println("line scores: " + lineScores.size());

		int mid = lineScores.size() / 2;

		System.out.println("middle: " + mid + ", middle value: " + lineScores.get(mid));

	}

}

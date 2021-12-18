import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Advent of Code 2021/12/10 Part 1
 */
public class AOC202110 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202110.class.getResourceAsStream("AOC202110_input.txt")));
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

		// illegal character score map
		Map<String, Integer> icm = new HashMap<String, Integer>();
		icm.put(")", Integer.valueOf(3));
		icm.put("]", Integer.valueOf(57));
		icm.put("}", Integer.valueOf(1197));
		icm.put(">", Integer.valueOf(25137));

		long score = 0;
		for (String[] currLine : lines) {
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
						score += icm.get(c);
						break;
					}
				}
			}
		}
		System.out.println("score: " + score);
	}

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Advent of Code 2021/12/02 Part 2
 */
public class AOC202102Part2 {

	// Hints:
	// down X increases your aim by X units.
	// up X decreases your aim by X units.
	// forward X does two things:
	// It increases your horizontal position by X units.
	// It increases your depth by your aim multiplied by X.

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202102Part2.class.getResourceAsStream("AOC202102_input.txt")));
		String line = null;

		long x = 0;
		long y = 0;
		long aim = 0;

		while ((line = br.readLine()) != null) {
			String[] parts = line.split(" ");
			long change = Long.parseLong(parts[1]);

			if ("forward".equals(parts[0])) {
				x += change;
				y += (aim * change);
			} else if ("up".equals(parts[0])) {
				aim -= change;
			} else if ("down".equals(parts[0])) {
				aim += change;
			}

		}
		br.close();

		System.out.println("x: " + x + "; y: " + y + " multiplied: " + (x * y));
	}

}

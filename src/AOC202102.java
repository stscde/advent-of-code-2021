import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Advent of Code 2021/12/02 Part 1
 */
public class AOC202102 {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202102.class.getResourceAsStream("AOC202102_input.txt")));
		String line = null;

		int x = 0;
		int y = 0;

		while ((line = br.readLine()) != null) {
			String[] parts = line.split(" ");
			int change = Integer.parseInt(parts[1]);

			if ("forward".equals(parts[0])) {
				x += change;
			} else if ("up".equals(parts[0])) {
				y -= change;
			} else if ("down".equals(parts[0])) {
				y += change;
			}

		}
		br.close();

		System.out.println("x: " + x + "; y: " + y + " multiplied: " + (x * y));
	}

}

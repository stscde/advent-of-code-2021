import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Advent of Code 2021/12/01 Part 1
 */
public class AOC202101 {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202101.class.getResourceAsStream("AOC202101_input.txt")));
		String line = null;
		int lastValue = Integer.MAX_VALUE;
		int increases = 0;
		while ((line = br.readLine()) != null) {
			int currValue = Integer.parseInt(line);
			if (currValue > lastValue) {
				increases++;
			}
			lastValue = currValue;
		}
		br.close();

		System.out.println("number of increases: " + increases);
	}

}

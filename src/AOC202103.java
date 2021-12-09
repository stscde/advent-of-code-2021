import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Advent of Code 2021/12/03 Part 1
 */
public class AOC202103 {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202103.class.getResourceAsStream("AOC202103_input.txt")));
		String line = null;

		int[] counts0 = null;
		int[] counts1 = null;

		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");

			if (counts0 == null) {
				counts0 = new int[parts.length];
				counts1 = new int[parts.length];
			}

			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equals("0")) {
					counts0[i]++;
				} else {
					counts1[i]++;
				}
			}

		}
		br.close();

		String gammaBinary = "";
		String epsilonBinary = "";
		for (int i = 0; i < counts0.length; i++) {
			gammaBinary += counts0[i] > counts1[i] ? "0" : "1";
			epsilonBinary += counts0[i] > counts1[i] ? "1" : "0";
		}

		int gammaInt = Integer.parseInt(gammaBinary, 2);
		int epsilonInt = Integer.parseInt(epsilonBinary, 2);

		System.out.println("gammaBinary: " + gammaBinary + ", epsilonBinary: " + epsilonBinary + ", gammaInt: " + gammaInt + ", epsilonInt: " + epsilonInt + ", multiplied: " + (gammaInt * epsilonInt));
	}

}

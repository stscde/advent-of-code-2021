import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Advent of Code 2021/12/08 Part 2<br>
 * Completed after a short break for log4shell<br>
 * For me the heaviest task so far
 */
public class AOC202108Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		List<String[][]> lines = new ArrayList<String[][]>();
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202108Part2.class.getResourceAsStream("AOC202108_input.txt")));

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\\|");

			String[] input = parts[0].trim().split(" ");
			String[] output = parts[1].trim().split(" ");

			lines.add(new String[][] {input, output});
		}
		br.close();

		// .1111.
		// 2....3
		// 2....3
		// .4444.
		// 5....6
		// 5....6
		// .7777.

		long total = 0;
		for (String[][] lineX : lines) {
			String one = findByLength(lineX[0], 2).get(0);
			String four = findByLength(lineX[0], 4).get(0);
			String seven = findByLength(lineX[0], 3).get(0);
			String eight = findByLength(lineX[0], 7).get(0);

			List<String> remainingDigits = new ArrayList<String>(Arrays.asList(lineX[0]));
			remainingDigits.remove(one);
			remainingDigits.remove(four);
			remainingDigits.remove(seven);
			remainingDigits.remove(eight);

			// find digit 3, only digit that has 5 segments and is overlapped by 7 completely
			String three = findByOverlap(remainingDigits, 5, seven);
			remainingDigits.remove(three);

			// find digit 9, only digit that has 6 segments and is overlapped by 4 and 3 completely
			String nine = findByOverlap(remainingDigits, 6, four, three);
			remainingDigits.remove(nine);

			// zero? => 6 segments and is overlapped by one completely
			String zero = findByOverlap(remainingDigits, 6, one);
			remainingDigits.remove(zero);

			// six? => 6 segments, only one remaining with 6 segments
			String six = findByLength(remainingDigits, 6).get(0);
			remainingDigits.remove(six);

			// two? => 5 segments, 2 segments overlapped by 4
			// five? => 5 segments, 3 segments overlapped by 4
			int notMatching0 = subtractChars(remainingDigits.get(0), four);
			int notMatching1 = subtractChars(remainingDigits.get(1), four);
			System.out.println("notMatching0: " + notMatching0 + ", notMatching1: " + notMatching1);
			String two = notMatching0 == 3 ? remainingDigits.get(0) : remainingDigits.get(1);
			String five = notMatching0 == 2 ? remainingDigits.get(0) : remainingDigits.get(1);

			if (notMatching0 == notMatching1) {
				throw new RuntimeException("unabled to detect 2 and 5");
			}

			String[] digitMap = new String[] {zero, one, two, three, four, five, six, seven, eight, nine};

			String number = "";
			OUT: for (String output : lineX[1]) {
				for (int d = 0; d < digitMap.length; d++) {
					String digit = digitMap[d];
					if (output.length() == digit.length() && matches(output, digit)) {
						number += String.valueOf(d);
						continue OUT;
					}
				}
				throw new RuntimeException("unable to decode digit");
			}

			System.out.println(String.join(",", lineX[1]) + ", decoded: " + number);
			total += Long.parseLong(number);
		}

		System.out.println("total: " + total);
	}


	/**
	 * Returns first digit which matches length and is overlapped by others in their segments
	 *
	 * @param line
	 * @param length
	 * @param others
	 * @return
	 */
	private static String findByOverlap(List<String> line, int length, String... others) {
		String wiring = null;
		List<String> possibleDigits = findByLength(line, length);
		for (String digitCandidate : possibleDigits) {
			if (matches(digitCandidate, others)) {
				if (wiring != null) {
					throw new RuntimeException("duplicate match found");
				}
				wiring = digitCandidate;
			}
		}
		return wiring;
	}


	private static boolean matches(String d1, String... others) {
		Set<String> s1 = new HashSet<String>(Arrays.asList(d1.split("|")));
		for (String d2 : others) {
			Set<String> s2 = new HashSet<String>(Arrays.asList(d2.split("|")));

			if (!s1.containsAll(s2)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Returns the number of segments NOT matching between d1 and others
	 *
	 * @param d1
	 * @param others
	 * @return
	 */
	private static int subtractChars(String d1, String... others) {
		Set<String> s1 = new HashSet<String>(Arrays.asList(d1.split("|")));
		for (String d2 : others) {
			Set<String> s2 = new HashSet<String>(Arrays.asList(d2.split("|")));
			s1.removeAll(s2);
		}
		return s1.size();
	}


	private static List<String> findByLength(List<String> parts, int length) {
		return findByLength(parts.toArray(new String[] {}), length);
	}


	private static List<String> findByLength(String[] parts, int length) {
		List<String> matches = new ArrayList<String>();
		for (String part : parts) {
			if (part.length() == length) {
				matches.add(part);
			}
		}
		return matches;
	}

}

// ......
// .....3
// .....3
// ......
// .....6
// .....6
// ......

// .1111.
// .....3
// .....3
// .4444.
// 5.....
// 5.....
// .7777.

// .1111.
// .....3
// .....3
// .4444.
// .....6
// .....6
// .7777.

// ......
// 2....3
// 2....3
// .4444.
// .....6
// .....6
// ......

// .1111.
// 2.....
// 2.....
// .4444.
// .....6
// .....6
// .7777.

// .1111.
// 2.....
// 2.....
// .4444.
// 5....6
// 5....6
// .7777.

// .1111.
// .....3
// .....3
// ......
// .....6
// .....6
// ......

// .1111.
// 2....3
// 2....3
// .4444.
// 5....6
// 5....6
// .7777.

// .1111.
// 2....3
// 2....3
// .4444.
// .....6
// .....6
// .7777.

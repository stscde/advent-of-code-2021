import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Advent of Code 2021/12/01 Part 2
 */
public class AOC202101Part2 {

	final static int WINDOWS_SIZE = 3;

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202101Part2.class.getResourceAsStream("AOC202101_input.txt")));
		String line = null;
		int increases = 0;

		List<List<Integer>> groups = new ArrayList<List<Integer>>();

		while ((line = br.readLine()) != null) {
			Integer currValue = Integer.parseInt(line);

			List<Integer> g1 = null;
			List<Integer> g2 = null;

			groups.add(new ArrayList<Integer>());
			for (List<Integer> group : groups) {

				if (group.size() < WINDOWS_SIZE) {
					group.add(currValue);
				}

				if (group.size() == WINDOWS_SIZE)
					if (g1 == null) {
						g1 = group;
					} else if (g2 == null) {
						g2 = group;
					}
			}

			if (g1 != null && g2 != null) {
				IntSummaryStatistics g1Sum = g1.stream().collect(Collectors.summarizingInt(i -> i.intValue()));
				IntSummaryStatistics g2Sum = g2.stream().collect(Collectors.summarizingInt(i -> i.intValue()));

				System.out.println("g1 sum: " + g1Sum.getSum() + ", g2 sum: " + g2Sum.getSum());

				if (g2Sum.getSum() > g1Sum.getSum()) {
					increases++;
				}

				groups.remove(g1);
			}
		}
		br.close();

		System.out.println("number of increases: " + increases);
	}

}

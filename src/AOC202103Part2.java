import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Advent of Code 2021/12/03 Part 2<br><br>

 * Required me to check the forum for hints after two days of failing.<br>
 * Found out that I did not read the instruction correctly to recount the 0/1s in each loop ... :-)<br>
 * Therefore contains lots of debug lines.
 */
public class AOC202103Part2 {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202103Part2.class.getResourceAsStream("AOC202103_input.txt")));
		String line = null;

		List<String[]> lines = new ArrayList<String[]>();

		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");
			lines.add(parts);
		}
		br.close();

		System.out.println("### o2 #####################");
		long o2Rating = getRating(new ArrayList<String[]>(lines), true);

		System.out.println("### co2 #####################");
		long co2Rating = getRating(new ArrayList<String[]>(lines), false);

		System.out.println("o2Rating: " + o2Rating + ", co2Rating: " + co2Rating +  ", multiplied: " + (o2Rating * co2Rating));
	}


	public static long getRating(List<String[]> lines, boolean keepMostCommon) {
		for (int i = 0; i < lines.get(0).length && lines.size() > 1; i++) {

			// recount 0/1
			// this was the tricky part for me - to do this again after removing not matching lines
			int count0 = 0;
			int count1 = 0;

			for (int l = 0; l < lines.size(); l++) {
				if (lines.get(l)[i].equals("0")) {
					count0++;
				} else {
					count1++;
				}
			}

			System.out.println("lines start: " + lines.size());
			int startCount = lines.size();

			String keep = keepMostCommon ? (count0 > count1 ? "0" : "1") : (count0> count1 ? "1" : "0");
			System.out.println("0: " + count0 + ", 1: " + count1  + ", keep: " + keep);

			final int i2 = i; // requirement for lambda in next line to have immutable int
			List<String[]> remove = lines.stream().filter(l -> !l[i2].equals(keep)).collect(Collectors.toList());
			lines.removeAll(remove);

			System.out.println("lines start: " + startCount + "/" + (lines.size() + remove.size()) + ", lines remain: " + lines.size() + ", lines removed: " + remove.size());
			System.out.println("");
		}

		System.out.println("value: "  + String.join("", lines.get(0)));
		return Long.parseLong(String.join("", lines.get(0)), 2);
	}

}

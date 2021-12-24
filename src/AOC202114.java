import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Advent of Code 2021/12/14 Part 1
 */
public class AOC202114 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202114.class.getResourceAsStream("AOC202114_input.txt")));
		String line = null;

		Map<String, String> rules = new HashMap<>();
		String start = "";
		while ((line = br.readLine()) != null) {
			if (start.isEmpty()) {
				start = line;
			}

			if (line.contains("->")) {
				String[] parts = line.split(" -> ");
				rules.put(parts[0], parts[1]);
			}

		}
		br.close();
		System.out.println("start: " + start + ", rule count: " + rules.size());

		// extend polymer
		String last = start;
		for (int i = 0; i < 10; i++) {
			StringBuffer newPoly = new StringBuffer();
			List<String> pairs = new ArrayList<>();

			for (int c = 0; c < last.length() - 1; c++) {
				char v = last.charAt(c);
				char vn = last.charAt(c + 1);
				pairs.add(new String(new char[] {v, vn}));
			}

			for (int p = 0; p < pairs.size(); p++) {
				String pair = pairs.get(p);
				String e = rules.get(pair);
				newPoly.append(pair.charAt(0)).append(e);

				if (p == pairs.size() - 1) {
					newPoly.append(pair.charAt(1));
				}

			}
			last = newPoly.toString();

			System.out.println((i + 1) + ", new poly: " + last + ", poly length: " + last.length());

		}

		// count elements
		Map<String, ElementCount> elementCounts = new HashMap<>();
		for (int c = 0; c < last.length(); c++) {
			String e = String.valueOf(last.charAt(c));
			ElementCount ec = elementCounts.getOrDefault(e, new AOC202114().new ElementCount(e));
			ec.add();
			elementCounts.put(e, ec);
		}

		List<ElementCount> ecs = new ArrayList<>(elementCounts.values());
		Collections.sort(ecs, (a, b) -> Integer.valueOf(b.count).compareTo(Integer.valueOf(a.count)));

		ElementCount e0 = ecs.get(0);
		ElementCount e1 = ecs.get(ecs.size() - 1);

		System.out.println("most common element: " + e0.e + ", count: " + e0.count + ", least common element: " + e1.e + ", count: " + e1.count + ", diff: " + (e0.count - e1.count));

	}

	private class ElementCount {

		String e;

		int count;

		public ElementCount(String e) {
			this.e = e;
		}


		void add() {
			count++;
		}
	}
}

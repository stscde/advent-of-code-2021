import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Advent of Code 2021/12/14 Part 2
 */
public class AOC202114Part2 {

	public static final int DEPTH = 40;

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202114Part2.class.getResourceAsStream("AOC202114_input.txt")));
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

		// calculate how each pair expands in half of the iterations
		Map<String, Pair> pairCounts = new HashMap<>();
		for (String pair : rules.keySet()) {
			Pair p = buildPair(pair, DEPTH / 2, rules);
			pairCounts.put(pair, p);
			System.out.println(new Date() + ", pair calculated: " + p.p);
		}

		// iterate first half of pairs
		Map<String, ElementCount> elementCounts = new HashMap<>();

		for (int c = 0; c < start.length() - 1; c++) {
			char v = start.charAt(c);
			char vn = start.charAt(c + 1);
			String pc = new String(new char[] {v, vn});
			Pair p = pairCounts.get(pc);

			// iterate second half of pairs
			String start2 = p.e;
			for (int c2 = 0; c2 < start2.length() - 1; c2++) {
				char v2 = start2.charAt(c2);
				char vn2 = start2.charAt(c2 + 1);

				String pc2 = new String(new char[] {v2, vn2});
				Pair p2 = pairCounts.get(pc2);

				for (ElementCount ec : p2.counts) {
					ElementCount ecn = elementCounts.getOrDefault(ec.e, new AOC202114Part2().new ElementCount(ec.e));
					elementCounts.put(ec.e, ecn);
					ecn.add(ec.count);
				}

				// remove count of first element because it is already contained in the half two counts
				String v2s = String.valueOf(v2);
				ElementCount ecn = elementCounts.getOrDefault(v2s, new AOC202114Part2().new ElementCount(v2s));
				elementCounts.put(v2s, ecn);
				ecn.remove();

			}
		}

		List<ElementCount> ecs = new ArrayList<>(elementCounts.values());
		Collections.sort(ecs, (a, b) -> Long.valueOf(b.count).compareTo(Long.valueOf(a.count)));

		ElementCount e0 = ecs.get(0);
		ElementCount e1 = ecs.get(ecs.size() - 1);

		System.out.println("most common element: " + e0.e + ", count: " + e0.count + ", least common element: " + e1.e + ", count: " + e1.count + ", diff: " + (e0.count - e1.count));

	}

	private class ElementCount {

		String e;

		long count;

		public ElementCount(String e) {
			this.e = e;
		}


		void add() {
			count++;
		}


		void remove() {
			count--;
		}


		void add(long count) {
			this.count += count;
		}
	}

	/**
	 * Values produced by a pair after n iterations
	 */
	private class Pair {

		/** Two letters of pair */
		String p;

		/** Pair extended after n iterations */
		String e;

		List<ElementCount> counts;

		public Pair(String p, String e, List<AOC202114Part2.ElementCount> counts) {
			super();
			this.p = p;
			this.e = e;
			this.counts = counts;
		}

	}

	private static Pair buildPair(String pair, int depth, Map<String, String> rules) {
		// System.out.println("calculate pair: " + pair + ", depth: " + depth);
		String last = pair;
		for (int i = 0; i < depth; i++) {
			StringBuffer newPoly = new StringBuffer();

			byte[] lastBytes = last.getBytes();
			for (int c = 0; c < lastBytes.length - 1; c++) {
				char v = (char) lastBytes[c];
				char vn = (char) lastBytes[c + 1];
				String p = new String(new char[] {v, vn});

				String e = rules.get(p);
				newPoly.append(v).append(e);

				if (c == lastBytes.length - 2) {
					newPoly.append(vn);
				}
			}

			// System.out.println(new Date() + ", build new poly: " + (i + 1));
			last = newPoly.toString();
		}

		// count elements
		Map<String, ElementCount> elementCounts = new HashMap<>();
		for (int c = 0; c < last.length(); c++) {
			String e = String.valueOf(last.charAt(c));
			ElementCount ec = elementCounts.getOrDefault(e, new AOC202114Part2().new ElementCount(e));
			ec.add();
			elementCounts.put(e, ec);
		}

		List<ElementCount> ecs = new ArrayList<>(elementCounts.values());

		return new AOC202114Part2().new Pair(pair, last, ecs);
	}

}

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Advent of Code 2021/12/15 Part 2<br>
 * Takes about 5 minutes to compute on a CORE i5 7th Gen
 */
public class AOC202115Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202111.class.getResourceAsStream("AOC202115_input.txt")));
		String line = null;
		List<String[]> lines = new ArrayList<String[]>();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");
			lines.add(parts);
		}
		br.close();

		// map
		int[][] map = new int[lines.size()][lines.get(0).length];
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < map[y].length; x++) {
				map[y][x] = Integer.parseInt(lines.get(y)[x]);
			}
		}

		// extend map
		final int extCount = 5;
		int[][] mapExt = new int[map.length * extCount][map[0].length * extCount];

		copyMap(map, new Point(0, 0), new Point(map[0].length, map.length), mapExt, new Point(0, 0), false);

		// copy first tile sidewards
		for (int i = 0; i < extCount - 1; i++) {
			copyMap(mapExt, new Point(i * map[0].length, 0), new Point((i + 1) * map[0].length, map.length), mapExt, new Point((i + 1) * map[0].length, 0), true);
		}

		// copy first row downwards
		for (int i = 0; i < extCount - 1; i++) {
			copyMap(mapExt, new Point(0, i * map.length), new Point(mapExt[0].length, (i + 1) * map.length), mapExt, new Point(0, (i + 1) * map.length), true);
		}

		map = mapExt;

		// build nodes
		AOC202115Dijkstra dijkstra = new AOC202115Dijkstra();
		Map<String, AOC202115Dijkstra.Node> nameToNode = new HashMap<>();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				String nodeName = x + "-" + y;
				AOC202115Dijkstra.Node node = nameToNode.getOrDefault(nodeName, dijkstra.new Node(nodeName));
				nameToNode.put(nodeName, node);

				for (Point np : getNeighbours(new Point(x, y), map, false)) {
					String nodeNameNeighbour = np.x + "-" + np.y;
					AOC202115Dijkstra.Node nodeNeighbour = nameToNode.getOrDefault(nodeNameNeighbour, dijkstra.new Node(nodeNameNeighbour));
					nameToNode.put(nodeNameNeighbour, nodeNeighbour);
					node.neighbourDistance.put(nodeNeighbour, Integer.valueOf(map[np.y][np.x]));
				}
			}
		}

		System.out.println("nodes built");

		AOC202115Dijkstra.Node start = nameToNode.get("0-0");
		AOC202115Dijkstra.Node end = nameToNode.get((map.length - 1) + "-" + (map[0].length - 1));
		List<AOC202115Dijkstra.Node> nodes = new ArrayList<>(nameToNode.values());
		dijkstra.calculate(start, nodes);

		// do some pretty printing for fun
		System.out.println("path calculated:\n");

		AOC202115Dijkstra.Node step = end;
		Set<AOC202115Dijkstra.Node> nodesOnPath = new HashSet<>();
		while (step != start) {
			nodesOnPath.add(step);
			step = step.pre;
		}
		nodesOnPath.add(start);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				AOC202115Dijkstra.Node node = nameToNode.get(x + "-" + y);
				if (nodesOnPath.contains(node)) {
					System.err.print(map[y][x]);
				} else {
					System.out.print(map[y][x]);
				}
				// System.out.print(".");
			}
			System.out.println("");
		}

		System.out.println("path length: " + end.distToThisNode);
	}


	/**
	 * Get horizontal and vertical and diagonal neighbour points of a given point
	 *
	 * @param p
	 * @param map
	 * @return
	 */
	private static List<Point> getNeighbours(Point p, int[][] map, boolean withDiagonals) {
		List<Point> neighbours = new ArrayList<Point>();

		// top value(s)
		if (p.y > 0) {
			neighbours.add(new Point(p.x, p.y - 1));

			if (withDiagonals) {
				// top left value
				if (p.x > 0) {
					neighbours.add(new Point(p.x - 1, p.y - 1));
				}

				// top right value
				if (p.x < map[p.y].length - 1) {
					neighbours.add(new Point(p.x + 1, p.y - 1));
				}
			}
		}

		// bottom value(s)
		if (p.y < map.length - 1) {
			neighbours.add(new Point(p.x, p.y + 1));

			if (withDiagonals) {
				// bottom left value
				if (p.x > 0) {
					neighbours.add(new Point(p.x - 1, p.y + 1));
				}

				// bottom right value
				if (p.x < map[p.y].length - 1) {
					neighbours.add(new Point(p.x + 1, p.y + 1));
				}
			}
		}

		// left value
		if (p.x > 0) {
			neighbours.add(new Point(p.x - 1, p.y));
		}

		// right value
		if (p.x < map[p.y].length - 1) {
			neighbours.add(new Point(p.x + 1, p.y));
		}

		return neighbours;
	}


	/**
	 * Copy parts of a source array to a target array
	 * 
	 * @param source source array
	 * @param sourceStart source array start copy coordinate
	 * @param sourceEnd source array end copy coordinate
	 * @param target target array
	 * @param targetStart target array start insert coordinate
	 * @param add true = add values according to task specification
	 */
	public static void copyMap(int[][] source, Point sourceStart, Point sourceEnd, int[][] target, Point targetStart, boolean add) {
		int xt = targetStart.x;
		int yt = targetStart.y;
		for (int ys = sourceStart.y; ys < sourceEnd.y; ys++) {
			for (int xs = sourceStart.x; xs < sourceEnd.x; xs++) {
				int v = source[ys][xs];

				if (add) {
					v++;
					if (v > 9) {
						v = 1;
					}
				}
				// System.out.println("copy: " + xs + ":" + ys + " => " + xt + ":" + yt);
				target[yt][xt] = v;
				xt++;
			}
			xt = targetStart.x;
			yt++;
		}
	}
}

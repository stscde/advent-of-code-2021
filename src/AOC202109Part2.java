import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Advent of Code 2021/12/09 Part 2
 */
public class AOC202109Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202109Part2.class.getResourceAsStream("AOC202109_input.txt")));
		String line = null;
		List<String[]> lines = new ArrayList<String[]>();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");
			lines.add(parts);
		}
		br.close();

		int[][] heightMap = new int[lines.size()][lines.get(0).length];
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < heightMap[y].length; x++) {
				heightMap[y][x] = Integer.parseInt(lines.get(y)[x]);
			}
		}

		// calculate depths
		List<Integer> basinSizes = new ArrayList<Integer>();
		for (int y = 0; y < heightMap.length; y++) {
			for (int x = 0; x < heightMap[y].length; x++) {
				int h = heightMap[y][x]; // pos height

				int t = y > 0 ? heightMap[y - 1][x] : Integer.MAX_VALUE; // top value
				int b = y < heightMap.length - 1 ? heightMap[y + 1][x] : Integer.MAX_VALUE; // bottom value
				int l = x > 0 ? heightMap[y][x - 1] : Integer.MAX_VALUE; // left value
				int r = x < heightMap[y].length - 1 ? heightMap[y][x + 1] : Integer.MAX_VALUE; // left value
				// System.out.println("h: " + h + ", t: " + t + ", b: " + b + ", l: " + l + ", r: " + r);

				if (h < t && h < b && h < l && h < r) {
					Point p = new Point(x, y);
					System.out.println("point: " + p + ", depth: " + h);
					Set<Point> visitedPoints = new HashSet<Point>();
					calculateBasinSize(p, visitedPoints, heightMap);
					System.out.println("point "  + p + ", basin size: " + visitedPoints.size());
					basinSizes.add(visitedPoints.size());
				}
			}
		}

		int total = 1;
		Collections.sort(basinSizes, (a, b) -> b.compareTo(a));
		for (int i =0; i < 3; i++) {
			total *= basinSizes.get(i);
		}
		System.out.println("multiplied top 3 basin sizes: " + total);


	}


	/**
	 * Calculate basin size for a depth point
	 * @param p
	 * @param visitedPoints
	 * @param heightMap
	 */
	private static void calculateBasinSize(Point p, Set<Point> visitedPoints, int[][] heightMap) {
		if (visitedPoints.contains(p)) {
			return;
		}

		visitedPoints.add(p);
		int v = heightMap[p.y][p.x];

		List<Point> neighbours = getNeighbours(p, heightMap);
		for (Point nP : neighbours) {
			int nV = heightMap[nP.y][nP.x];
			if (nV >= v && nV < 9) {
				calculateBasinSize(nP, visitedPoints, heightMap);
			}
		}
	}


	/**
	 * Get horizontal and vertical neighbour points of a given point
	 * @param p
	 * @param heightMap
	 * @return
	 */
	private static List<Point> getNeighbours(Point p, int[][] heightMap) {
		List<Point> neighbours = new ArrayList<Point>();
		// top value
		if (p.y > 0) {
			neighbours.add(new Point(p.x, p.y - 1));
		}

		// bottom value
		if (p.y < heightMap.length - 1) {
			neighbours.add(new Point(p.x, p.y + 1));
		}

		// left value
		if (p.x > 0) {
			neighbours.add(new Point(p.x-1, p.y));
		}

		// right value
		if (p.x < heightMap[p.y].length - 1) {
			neighbours.add(new Point(p.x+1, p.y));
		}

		return neighbours;
	}
}

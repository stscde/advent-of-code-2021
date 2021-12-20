import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/11 Part 1<br>
 * Example produces 1656 flashes.
 */
public class AOC202111 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202111.class.getResourceAsStream("AOC202111_input.txt")));
		String line = null;
		List<String[]> lines = new ArrayList<String[]>();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("|");
			lines.add(parts);
		}
		br.close();

		// octopus power map
		int[][] pMap = new int[lines.size()][lines.get(0).length];
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < pMap[y].length; x++) {
				pMap[y][x] = Integer.parseInt(lines.get(y)[x]);
			}
		}

		long totalFlashes = 0;
		for (int s = 0; s < 100; s++) {
			// octo flash map 1 = already flashed
			int[][] fMap = new int[lines.size()][lines.get(0).length];

			// first increase energy of EVERY octopus by 1
			for (int y = 0; y < pMap.length; y++) {
				for (int x = 0; x < pMap[y].length; x++) {
					pMap[y][x]++;
				}
			}

			// handle flashes
			while (true) {
				int currFlashes = 0;

				for (int y = 0; y < pMap.length; y++) {
					for (int x = 0; x < pMap[y].length; x++) {
						if (pMap[y][x] > 9 && fMap[y][x] == 0) {

							currFlashes++;
							fMap[y][x] = 1; // note that octo flashed

							// power up nearby octos
							for (Point n : getNeighbours(new Point(x, y), pMap)) {
								pMap[n.y][n.x]++;
							}
						}
					}
				}

				if (currFlashes == 0) {
					break;
				}

				totalFlashes += currFlashes;
			}

			// power down flashed octos
			for (int y = 0; y < pMap.length; y++) {
				for (int x = 0; x < pMap[y].length; x++) {
					if (fMap[y][x] != 0) {
						pMap[y][x] = 0;
					}
				}
			}

		}

		System.out.println("total flashes: " + totalFlashes);

	}


	/**
	 * Get horizontal and vertical and diagonal neighbour points of a given point
	 *
	 * @param p
	 * @param map
	 * @return
	 */
	private static List<Point> getNeighbours(Point p, int[][] map) {
		List<Point> neighbours = new ArrayList<Point>();

		// top value(s)
		if (p.y > 0) {
			neighbours.add(new Point(p.x, p.y - 1));

			// top left value
			if (p.x > 0) {
				neighbours.add(new Point(p.x - 1, p.y - 1));
			}

			// top right value
			if (p.x < map[p.y].length - 1) {
				neighbours.add(new Point(p.x + 1, p.y - 1));
			}
		}

		// bottom value(s)
		if (p.y < map.length - 1) {
			neighbours.add(new Point(p.x, p.y + 1));

			// bottom left value
			if (p.x > 0) {
				neighbours.add(new Point(p.x - 1, p.y + 1));
			}

			// bottom right value
			if (p.x < map[p.y].length - 1) {
				neighbours.add(new Point(p.x + 1, p.y + 1));
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

}

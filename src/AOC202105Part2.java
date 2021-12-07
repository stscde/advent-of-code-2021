import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/05 Part 2
 */
public class AOC202105Part2 {

	// false = solve part 1; true = solve part 2
	static final boolean ENABLE_PART_2 = true;

	public static void main(String[] args) throws IOException {
		int maxX = 0;
		int maxY = 0;

		// read input
		List<Point[]> points = new ArrayList<Point[]>();
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202105Part2.class.getResourceAsStream("AOC202105_input.txt")));
		String line = null;
		while ((line = br.readLine()) != null) {
			String lineParts[] = line.replaceAll(" -> ", ",").split(",");
			Point p1 = new Point(Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[1]));
			Point p2 = new Point(Integer.parseInt(lineParts[2]), Integer.parseInt(lineParts[3]));

			if (p1.getX() == p2.getX() || p1.getY() == p2.getY() || ENABLE_PART_2) {
				points.add(new Point[] {p1, p2});
				maxX = Math.max(maxX, (int) p1.getX());
				maxX = Math.max(maxX, (int) p2.getX());
				maxY = Math.max(maxY, (int) p1.getY());
				maxY = Math.max(maxY, (int) p2.getY());
			}
		}
		br.close();

		// find tube crossings
		int[][] crossings = new int[maxX + 1][maxY + 1];

		int p = 1;
		for (Point[] currPoints : points) {
			System.out.println("p " + p + " of " + points.size());
			p++;

			Point p1 = currPoints[0];
			Point p2 = currPoints[1];

			int x = (int) p1.getX();
			int y = (int) p1.getY();
			int toX = (int) p2.getX();
			int toY = (int) p2.getY();

			crossings[x][y]++;

			while (x != toX || y != toY) {
				x += x == toX ? 0 : (x > toX ? -1 : 1);
				y += y == toY ? 0 : (y > toY ? -1 : 1);
				crossings[x][y]++;
			}
			// }
		}

		// count crossings
		int danger = 0;
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				if (crossings[x][y] > 1) {
					danger++;
				}
			}
		}

		System.out.println("number of dangerous crossings found: " + danger);
	}

}

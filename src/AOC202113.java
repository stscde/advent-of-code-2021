import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/13 Part 1
 */
public class AOC202113 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202113.class.getResourceAsStream("AOC202113_input.txt")));
		String line = null;
		List<Point> points = new ArrayList<>();
		List<String[]> folds = new ArrayList<>();
		int maxX = 0;
		int maxY = 0;
		while ((line = br.readLine()) != null) {
			if (line.contains(",")) {
				String[] parts = line.split(",");
				Point p = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
				maxX = Math.max(maxX, p.x);
				maxY = Math.max(maxY, p.y);
				points.add(p);
			}
			if (line.contains("fold")) {
				String[] parts = line.replaceFirst("fold along ", "").split("=");
				folds.add(new String[] {parts[0], parts[1]});
			}

		}
		br.close();

		// init paper
		int[][] dots = new int[maxY + 1][maxX + 1];
		for (Point p : points) {
			dots[p.y][p.x] = 1;
		}

		// fold
		for (int fl = 0; fl < 1; fl++) {
			String axis = folds.get(fl)[0];
			int w = Integer.parseInt(folds.get(fl)[1]);

			if (axis.equals("x")) {
				dots = foldX(dots, w);
			} else {
				dots = foldY(dots, w);
			}
		}

		// count dots
		int dotCount = 0;
		for (int y = 0; y < dots.length; y++) {
			for (int x = 0; x < dots[y].length; x++) {
				if (dots[y][x] == 1) {
					dotCount++;
				}
			}
		}

		System.out.println("dots on paper: " + dotCount);
	}


	private static int[][] foldX(int[][] dots, int f) {

		// copy left part
		int[][] newDots = new int[dots.length][f];
		for (int y = 0; y < dots.length; y++) {
			for (int x = 0; x < f; x++) {
				newDots[y][x] = dots[y][x];
			}
		}

		// mirror right part
		for (int y = 0; y < dots.length; y++) {
			for (int x = f + 1; x < dots[y].length; x++) {
				int newX = f - (x - f);
				if (dots[y][x] == 1) {
					newDots[y][newX] = dots[y][x];
				}
			}
		}

		return newDots;
	}


	private static int[][] foldY(int[][] dots, int f) {
		return null;
	}

}

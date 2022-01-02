import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/17 Part 2
 */
public class AOC202117Part2 {

	public static void main(String[] args) throws IOException {

		// Puzzle input: target area: x=241..275, y=-75..-49
		int xMin = 241;
		int xMax = 275;
		int yMin = -75;
		int yMax = -49;

		// example values
//		xMin = 20;
//		xMax = 30;
//		yMin = -10;
//		yMax = -5;

		final int maxXV = 10000;
		final int maxYV = 10000;

		// most stylish flight path values
		int styleXV = 0;
		int styleYV = 0;
		int styleMaxY = Integer.MIN_VALUE;
		
		int validInitSettings = 0;

		for (int xv = 0; xv < maxXV; xv++) {
			for (int yv = -maxYV; yv < maxYV; yv++) {

				int currXv = xv;
				int currYv = yv;
				int maxY = 0;
				Point p = new Point(0, 0);
				List<Point> path = new ArrayList<>();
				while(true) {
					maxY = Math.max(p.y, maxY);

					path.add(new Point(p));
					if (isInTargetArea(xMin, xMax, yMin, yMax, p)) {
						System.out.println("\ntarget area hit with xv/yv: " + xv + "/" + yv + ", at point: " + p);
						for (Point px : path) {
							System.out.print(px.x + "/" + px.y + ", ");
						}
						System.out.println();

						if (maxY > styleMaxY) {
							styleMaxY = maxY;
							styleXV = xv;
							styleYV = yv;
						}
						
						validInitSettings++;

						break;
					}
					
					// break if over target area
					if (p.x > xMax || p.y < yMin) {
						break;
					}

					// move probe
					p.x = p.x + currXv;
					p.y = p.y + currYv;

					// adapt velocities
					if (currXv != 0) {
						currXv += currXv > 0 ? -1 : 1;
					}

					currYv -= 1;
				}
			}
		}

		System.out.println("\n==> most stylish flight path with xv/yv: " + styleXV + "/" + styleYV + ", highest Y: " + styleMaxY + ", validInitSettings: " + validInitSettings);
	}


	private static boolean isInTargetArea(int xMin, int xMax, int yMin, int yMax, Point p) {
		return p.x >= xMin && p.x <= xMax && p.y >= yMin && p.y <= yMax;
	}
}

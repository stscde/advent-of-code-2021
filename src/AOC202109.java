import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/09 Part 1
 */
public class AOC202109 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202109.class.getResourceAsStream("AOC202109_input.txt")));
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

		// calculate risk level
		int riskLevelSum = 0;
		for (int y = 0; y < heightMap.length; y++) {
			for (int x = 0; x < heightMap[y].length; x++) {
				int h = heightMap[y][x]; // pos height

				int t = y > 0 ? heightMap[y - 1][x] : Integer.MAX_VALUE; // top value
				int b = y < heightMap.length - 1 ? heightMap[y + 1][x] : Integer.MAX_VALUE; // bottom value
				int l = x > 0 ? heightMap[y][x - 1] : Integer.MAX_VALUE; // left value
				int r = x < heightMap[y].length - 1 ? heightMap[y][x + 1] : Integer.MAX_VALUE; // left value

				System.out.println("h: " + h + ", t: " + t + ", b: " + b + ", l: " + l + ", r: " + r);

				if (h < t && h < b && h < l && h < r) {
					System.out.println("found depth");
					riskLevelSum += h + 1;
				}
			}
		}
		System.out.println("risk level sum: " + riskLevelSum);
	}

}

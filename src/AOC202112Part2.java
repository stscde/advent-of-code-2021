import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Advent of Code 2021/12/12 Part 2
 */
public class AOC202112Part2 {

	public static void main(String[] args) throws IOException {

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202112Part2.class.getResourceAsStream("AOC202112_input.txt")));
		String line = null;

		Map<String, Location> locations = new HashMap<String, AOC202112Part2.Location>();

		while ((line = br.readLine()) != null) {
			String[] parts = line.split("-");

			String l1 = parts[0];
			String l2 = parts[1];

			Location loc1 = locations.getOrDefault(l1, new Location(l1));
			locations.put(l1, loc1);

			Location loc2 = locations.getOrDefault(l2, new Location(l2));
			locations.put(l2, loc2);

			loc1.connect(loc2);
			loc2.connect(loc1);
		}
		br.close();

		// find routes
		Location start = locations.get("start");
		System.out.println("start: " + start);

		List<List<String>> routes = travel(new ArrayList<String>(), null, start);
		for (List<String> route : routes) {
			System.out.println("route: " + String.join(",", route));
		}

		System.out.println("routes: " + routes.size());
	}


	public static List<List<String>> travel(List<String> currRoute, String from, Location currLocation) {
		currRoute.add(currLocation.name);

		List<List<String>> routes = new ArrayList<List<String>>();
		if (currLocation.name.equals("end")) {
			routes.add(currRoute);
		} else {
			for (Location n : currLocation.neighbours) {

				// do not go back to start
				if (n.name.equals("start")) {
					continue;
				}

				// it is only allowed to visit one small cave twice
				if (n.isSmallCave()) {
					if (currRoute.contains(n.name)) {
						if (smallCaveVisitedTwice(currRoute)) {
							continue;
						}
					}
				}

				routes.addAll(travel(new ArrayList<String>(currRoute), currLocation.name, n));
			}
		}
		return routes;
	}


	/**
	 * Is there the same small cave twice on the route already?
	 *
	 * @param route
	 * @return
	 */
	private static boolean smallCaveVisitedTwice(List<String> route) {
		Set<String> smallCaves = new HashSet<String>();
		for (String c : route) {
			if (c.matches("^[a-z]+$")) {
				if (smallCaves.contains(c)) {
					System.out.println("route " + String.join(",", route) + ", contains twice: " + c);

					return true;
				}
				smallCaves.add(c);
			}
		}
		return false;
	}

	private static class Location {

		String name;

		Set<Location> neighbours = new HashSet<AOC202112Part2.Location>();

		public Location(String name) {
			this.name = name;
		}


		public void connect(Location neighbour) {
			neighbours.add(neighbour);
		}


		public String toString() {
			List<String> neighbourNames = neighbours.stream().map(n -> n.name).collect(Collectors.toList());
			return "name: " + name + ", neighbours: " + String.join(", ", neighbourNames);
		}


		public boolean isSmallCave() {
			return name.matches("^[a-z]+$");
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Location other = (Location) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}
}

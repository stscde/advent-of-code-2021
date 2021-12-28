import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Dijkstra implementation for AOC 2021/12/15
 */
public class AOC202115Dijkstra {

	public void calculate(Node start, List<Node> open) {
		start.distToThisNode = 0;

		Set<Node> done = new HashSet<>();

		while (!open.isEmpty()) {
			Collections.sort(open);
			Node curr = open.get(0);
			open.remove(curr);
			done.add(curr);

			if (open.size() % 100 == 0) {
				System.out.println("nodes left: " + open.size());
			}

			for (Node neigh : curr.neighbourDistance.keySet()) {

				if (done.contains(neigh)) {
					continue;
				}

				if (neigh.distToThisNode == null || (neigh.distToThisNode != null && neigh.distToThisNode > (curr.distToThisNode + curr.neighbourDistance.get(neigh)))) {
					neigh.pre = curr;
					neigh.distToThisNode = (curr.neighbourDistance.get(neigh) + curr.distToThisNode);
				}

			}

		}

	}

	public class Node implements Comparable<Node> {

		/** Node name - has to be unique */
		String name;

		/** Neighbours with distances */
		Map<Node, Integer> neighbourDistance = new HashMap<>();

		/** Predecessor Node */
		Node pre;

		/** Distance from start node to this node - null means unkown */
		Integer distToThisNode = null;

		Integer getCompareDist() {
			return distToThisNode == null ? Integer.MAX_VALUE : distToThisNode;
		}


		public Node(String name) {
			this.name = name;
		}


		@Override
		public int compareTo(AOC202115Dijkstra.Node o) {
			return getCompareDist().compareTo(o.getCompareDist());
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(name);
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
			Node other = (Node) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(name, other.name);
		}


		private AOC202115Dijkstra getEnclosingInstance() {
			return AOC202115Dijkstra.this;
		}


		@Override
		public String toString() {
			return "Node [name=" + name + ", distToThisNode=" + distToThisNode + "]";
		}

	}

}

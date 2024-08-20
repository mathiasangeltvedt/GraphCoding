package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import graph.*;

public class ProblemSolver implements IProblem {

	@Override
	public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) {
		PriorityQueue<Edge<V>> queue = new PriorityQueue<>(g);
		HashSet<V> visited = new HashSet<>();
		V node = g.getFirstNode();
		visited.add(node);
		for (Edge<V> edge : g.adjacentEdges(node)) {
			queue.add(edge);
		}
		return prims(queue, visited, g);
	}

	/**
	 * A helpermethod to perform Prim's algorithm
	 * 
	 * @param <V>     type for nodes
	 * @param <E>     type for edges
	 * @param queue   a priority queue for the weight of the edges
	 * @param visited a list of visited nodes
	 * @param g       the graph
	 * @return the mst created by prims algorithm
	 */
	private <V, E extends Comparable<E>> ArrayList<Edge<V>> prims(PriorityQueue<Edge<V>> queue, HashSet<V> visited,
			WeightedGraph<V, E> g) {

		ArrayList<Edge<V>> mst = new ArrayList<>();
		while (!queue.isEmpty()) {
			Edge<V> edge = queue.poll();
			if (visited.contains(edge.a) && visited.contains(edge.b)) {
				continue;
			}
			V node = !visited.contains(edge.a) ? edge.a : edge.b;
			visited.add(node);
			for (Edge<V> adjacentEdge : g.adjacentEdges(node)) {
				if (!visited.contains(adjacentEdge.b)) {
					queue.add(adjacentEdge);
				}
			}
			mst.add(edge);
			if (mst.size() == g.numVertices() - 1) {
				break;
			}
		}
		return mst;
	}

	@Override
	public <V> V lca(Graph<V> g, V root, V u, V v) {
		if (root == null) {
			return root;
		} else if (root == u || root == v) {
			return root;
		}
		ArrayList<V> path1 = new ArrayList<>();
		ArrayList<V> path2 = new ArrayList<>();
		findPath(root, u, g, path1, new HashSet<V>());
		findPath(root, v, g, path2, new HashSet<V>());
		int i;
		for (i = 0; i < path1.size() && i < path2.size(); i++) {
			if (!path1.get(i).equals(path2.get(i))) {
				break;
			}
		}
		return path1.get(i - 1);
	}

	/**
	 * A helpermethod to find the path from the root node to the target node we want
	 * to find
	 * 
	 * @param <V>         type for nodes
	 * @param currentNode current visited node
	 * @param targetNode  node we want to reach
	 * @param g           the graph
	 * @param path        a list of the path to the target node
	 * @param visited     a list of the visited nodes
	 * @return a boolean to state if we found the target node or not
	 */
	private <V> boolean findPath(V currentNode, V targetNode, Graph<V> g, ArrayList<V> path, HashSet<V> visited) {
		visited.add(currentNode);
		path.add(currentNode);
		if (currentNode.equals(targetNode)) {
			return true;
		}
		for (V vertice : g.neighbours(currentNode)) {
			if (!visited.contains(vertice)) {
				if (findPath(vertice, targetNode, g, path, visited)) {
					return true;
				}
			}
		}
		path.remove(path.size() - 1);
		return false;
	}

	@Override
	public <V> Edge<V> addRedundant(Graph<V> g, V root) {
		HashMap<V, Integer> subTrees = new HashMap<>();
		getSubTreeSizes(g, root, subTrees, new HashSet<>());
		if (g.degree(root) == 1) {
			V subtreeRoot = g.neighbours(root).iterator().next();
			V leaf = bstLeaf(g, root, subtreeRoot, subTrees, new HashSet<V>());
			return new Edge<>(root, leaf);
		}
		ArrayList<V> BSTroots = bstOfRoot(g, root, subTrees);
		V node1 = bstLeaf(g, root, BSTroots.get(0), subTrees, new HashSet<V>());
		V node2 = bstLeaf(g, root, BSTroots.get(1), subTrees, new HashSet<V>());
		return new Edge<>(node1, node2);
	}

	private <V> void getSubTreeSizes(Graph<V> g, V current, HashMap<V, Integer> subTrees, HashSet<V> visited) {
		visited.add(current);
		int size = 1;
		for (V neighbour : g.neighbours(current)) {
			if (!visited.contains(neighbour)) {
				getSubTreeSizes(g, neighbour, subTrees, visited);
				size += subTrees.get(neighbour);
			}
		}
		subTrees.put(current, size);
	}

	private <V> ArrayList<V> bstOfRoot(Graph<V> g, V root, HashMap<V, Integer> subTrees) {
		V bstR1 = null;
		int bst1 = 0;
		V bstR2 = null;
		int bst2 = 0;
		for (V node : g.neighbours(root)) {
			if (subTrees.get(node) > bst1) {
				if (bst1 > bst2) {
					bst2 = bst1;
					bstR2 = bstR1;
				}
				bstR1 = node;
				bst1 = subTrees.get(node);
			} else if (subTrees.get(node) > bst2) {
				if (bst2 > bst1) {
					bst1 = bst2;
					bstR1 = bstR2;
				}
				bstR2 = node;
				bst2 = subTrees.get(node);
			}
		}
		return new ArrayList<V>(Arrays.asList(bstR1, bstR2));
	}

	private <V> V bstLeaf(Graph<V> g, V root, V current, HashMap<V, Integer> subTrees, HashSet<V> visited) {
		if (g.degree(current) < 2) {
			return current;
		}
		if (!visited.contains(root)) {
			visited.add(root);
		}
		visited.add(current);
		V bstR = current;
		int bst = 0;
		for (V neighbour : g.neighbours(current)) {
			if (!visited.contains(neighbour)) {
				if (subTrees.get(neighbour) > bst) {
					bstR = neighbour;
					bst = subTrees.get(neighbour);
				}
			}
		}
		return bstLeaf(g, root, bstR, subTrees, visited);
	}
}

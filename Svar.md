# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst
For the mst I simply implemented prims algorithm. It is a simple path creating algorithm, that starts at a random node, checks each edge of that node and finds the minimum weight edge. It then creates a connection with this edge, and moves on to the node on the other side. We then do the same on the other side, but since we check if a neighbour has been visited, we won't go back to the previous node, or a node thats already connected so we don't create loops. It then repeats this process until we've found n-1 edges as we know we've connected all edges. 

## Task 2 - lca
For lca I used a a simple dfs to find the path from the root to each node. I use a recursive algorithm to this where I add the node I visit, but if the target node is not within this node's subtree the node is removed from the list when the method traces back. I then save these two paths, and go through them one node by one, until I found a mismatching node in the path. When this happens I know that the i-1 node is the lca for the two target nodes u and v, and that's the node I want to return. 

## Task 3 - addRedundant
For the addRedundant method it becomes a bit more complicated as there is not just a simple algorithm to use to solve the problem. We want to find the two nodes we can connect, that keeps power to the most amount of nodes if any edge is removed. To do this, we want to find the biggest subtrees of the power source, and then go through the biggest subtrees of subtrees until we find a leaf. If we connect these two leaves, we know that we will always have a connection between the most amount of edges no matter what edge is removed, as we've connected the biggest subtrees. 
To find the biggest subtrees, I first used a dfs where I saved the amount of nodes within a nodes subtree in a HashMap. 
I then found the two biggest subtrees of the root by going through the root's neighbours, and always keep the two largest values for the neighbours subtrees and the root node of this subtree.
I then go through the biggest subtrees of these two subtrees using a recursive algorithm that runs as long as the amount of neighbours of the current node is more than 1. If the degree of a neighbour is 1 we've found a leaf. 
This recursive algorithm keeps the node that has the biggest subtree, which is the value I get from the HashMap I created earlier. I also keep track of the nodes we've visited, so I don't go back up to the root. 
We then want to create an edge between these two leaves we've found. 

# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

* **``mst(WeightedGraph<T, E> g)``: O(m*log(n))**
    - `g.getFirstNode()` has a runtime of O(1).
    - `visited.add()` has a runtime of O(1).
    - For-loop that goes through all edges of a node has a runtime of O(m). Inside this for-loop I use ``queue.add()`` which has a runtime of O(log(n)).
    - The for-loop therefore has a runtime of O(m*log(n)). 
    - I also use `prims` which has a runtime of (m*log(n)).
    - Hence, the runtime of this method is O(m*log(n)).

* **``prims(PriorityQueue<Edge<V>> queue, HashSet<V> visited, WeightedGraph<V, E> g)``: O(m*log(n))**
    - `while (!queue.isEmpty())` itself doesn't actually have a runtime, the while loop only runs as long as the requirements are fulfilled. 
    - Inside the while-loop I use `queue.poll()` which has a runtime of O(log(n)), because O(log(m)) <= O(log(n^2)) = O(2log(n)) = O(log(n)), so here you could say either O(log(m)) or O(log(n)), but for worst case it's best to say O(log(n)) as this will be the highest value of o(log(m)). I also use `visited.contains()` and `visited.add()` which both have a runtime of O(1). I also have a for-loop which goes through all the edges of a node, which has a runtime of O(m), because the runtime in this case is O(n*deg(node)) -> n*deg(node) after handshake theorem becomes m. Therefore we can say that it become O(n*deg(node)) = O(m). Because this for-loop only will run the same amount of times as the while loop allows it to run, it stays as O(m). This is because of our if-checks where we check if we are making loops or not, therefore it won't go any higher.
    - Inside the for-loop I use `queue.add()` which has a runtime of log(n) for the same reason as `queue.poll()`.
    - O(m) * O(log(n)) = O(m*log(n)).
    - Hence, the runtime of this method is O(m*log(n)).

* **``lca(Graph<T> g, T root, T u, T v)``: O(n)**
    - `findPath` has a runtime of O(n)
    - `for (i = 0; i < path1.size() && i < path2.size(); i++)` has a runtime of O(n), if the path is through all the nodes in the graph.
    - Hence, this method has a runtime of O(n).

* **``findPath(V currentNode, V targetNode, Graph<V> g, ArrayList<V> path, HashSet<V> visited)``: O(n)**
    - `visited.add()` has a runtime of O(1), and `path.add()` has a runtime of O(1).
    - `for (V vertice : g.neighbours(currentNode))` has a recursive call to findPath, making it work just as a dfs. A standard dfs has a runtime of O(n), therefore the runtime here become O(n). 
    - `path.remove()` has a runtime of O(1).
    - Hence, the runtime of this method is O(n).



* **``addRedundant(Graph<T> g, T root)``: O(n)**
    - `getSubtreeSizes()` has a runtime of O(n).
    - If the graph only has one subtree I get the root of this subtree by using `g.neighbours(root).iterator().next()` which has a runtime of O(1) and then I use `bstLeaf()`on this subtree which has a runtime of O(m), so we get O(n) because we still need to use the `getSubtreeSizes` method which has a runtime of O(n).
    - `bstRoot()` has a runtime of O(m).
    - `bstLeaf()` has a runtime of O(m).
    - Hence, the runtime of this method ends up being O(n) in all cases because of the `getSubtreeSizes` method.

* **``getSubtreeSizes(Graph<V> g, V current, HashMap<V, Integer> subTrees, HashSet<V> visited)``: O(n)**
    - `for (V vertice : g.neighbours(currentNode))` has a recursive call to `getSubtreeSizes` which makes it work just as a standard dfs. A standard dfs has a runtime of O(n), therefore we can say that the runtime of here becomes O(n).
    - HashMap's `put() and get()` method and HashSet's `get()` method has a runtime of O(1). 
    - Hence, the runtime of this method ends up being O(m).

* **``bstOfRoot(Graph<V> g, V root)``: O(m)**
    - `for (V neighbour : g.neighbours(root))` has a runtime of O(m) because worst case is that the root has all other nodes as neighbour, then it has to go through n-1 = m nodes, hence the runtime is O(m). 
    - HashMap's `get()` method has a runtime of O(1).
    - Hence, the runtime of this method ends up being O(m).

* **``bstLeaf(Graph<V> g, V graphRoot, V current, HashSet<V> bstVisited)``: O(m)**
    - `for (V vertice : g.neighbours(currentNode))` has a runtime of O(degree(currentNode)) -> O(m) because we dont actually do anything inside this if we've already visited this node. So from handshaking theorem if one node is connected to all others, then we go through n-1 nodes, and then we've already visited the n'th node so we get O(n-1) -> O(degree(currentNode)) -> O(m).
    - HashMap's `get()` method and HashSet's `add() and contains()` method has a runtime of O(1).
    - At the end of the method we make a recursive call to `bstLeaf`. This recursive call could in worst case be made n-1 = m times. Therefore we can say that the runtime of the whole method until it finds a leaf is O(m). 
    - Hence, the runtime of this method ends up being O(m).
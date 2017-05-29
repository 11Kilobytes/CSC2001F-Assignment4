/** This class represents a weighted directed graph
 * In a graph (V, E), nodes are represented with the integers 0..(|V| - 1).
 */
import java.util.*;

public class DiGraph {
    /* A map from nodes to the edges connected to that node */
    private HashMap<Integer, LinkedList<DirectedEdge>> g;

    /** Creates an empty <code>DiGraph</code> with no vertices or edges */
    public DiGraph() {
        this.g = new HashMap<Integer, LinkedList<DirectedEdge>>();
    }

    /** Adds the given edges to this graph */
    public void addEdge(DirectedEdge e) {
        if (g.containsKey(e.from()))
            g.get(e.from()).add(e);
        else
            g.put(e.from(), new LinkedList<DirectedEdge>(Collections.singletonList(e)));

        if (!g.containsKey(e.to()))
            g.put(e.to(), new LinkedList<DirectedEdge>());
    }

    /** Gets the neighbouring edges of the given node. */
    public List<DirectedEdge> edgesFrom(int node) {
        return g.get(node);
    }

    /** Return the number of vertices in this graph */
    public int V() {
        return g.size();
    }

    /** Return the number of edges in this graph */
    public int E() {
        return g.values().stream().mapToInt(LinkedList::size).sum();
    }

    /** A class for storing all the shortest paths in this
     * graph that start from a given node */
    public final class PathsFrom {
        private int source;
        private DirectedEdge[] edgeTo;
        private double[] distTo;
        private IndexMinPQ<Double> nodePQ;
        private boolean[] multiplePathsTo;

        /** Returns the shortest paths from the given source
         * using Dijkstra's Algorithm.
         * @param source Must be between  0..(V() - 1) Assumes that all edge
         * costs are non-negative. */
        private PathsFrom(int source) {
            this.source = source;
            edgeTo = new DirectedEdge[V()];
            distTo = new double[V()];
            multiplePathsTo = new boolean[V()];
            nodePQ = new IndexMinPQ<>(V());

            Arrays.fill(distTo, Double.POSITIVE_INFINITY);
            distTo[source] = 0;
            Arrays.fill(multiplePathsTo, false);
            nodePQ.insert(source, 0.0);
            while (!nodePQ.isEmpty())
                relax(nodePQ.delMin());
        }

        private void relax(Integer node) {
            for (DirectedEdge e: edgesFrom(node)) {
                if (nodePQ.contains(e.to()))
                    multiplePathsTo[e.to()] = true;
                if (distTo[e.to()] > distTo[node] + e.cost()) {
                    edgeTo[e.to()] = e;
                    distTo[e.to()] = distTo[node] + e.cost();
                    if (nodePQ.contains(e.to())) {
                        nodePQ.changeKey(e.to(), distTo[e.to()]);
                    }
                    else
                        nodePQ.insert(e.to(), distTo[e.to()]);
                }
            }
        }

        /** Returns the total cost of the optimal path to v from the source
         * given in the constructor, assuming that a path exists between
         * the source and v. If one does not exist, <code>Double.POSITIVE_INFINITY</code>
         * is returned.
         */
        public double distTo(int v) { return distTo[v]; }


        /** Returns whether there exists a path between the source
         * and a given node.
         * @param v the endpoint to search for
         */
        public boolean hasPathTo(int v) { return distTo[v] < Double.POSITIVE_INFINITY ;}

        /** Returns a path to the given edge if one exists, otherwise <code>null</code>is returned.
         * @param v  the endpoint to search for
         * @return a collection of the edges connecting the source to v
         * as List of <code>DirectedEdge</code> objects. The path gives a natural ordering on the edges,
         * this is the order used in the List. The node v itself is not included.
         *
         */
        public List<DirectedEdge> pathTo(int v) {
            if (hasPathTo(v)) {
                LinkedList<DirectedEdge> stack = new LinkedList<DirectedEdge>();
                int currentNode = v;
                while (currentNode != source) {
                    stack.push(edgeTo[currentNode]);
                    currentNode = edgeTo[currentNode].from();
                }
                return stack;
            } else
                return null;
        }

        /** Checks whether multiple paths to the given node
         * were found, assuming that at least one exists.
         */
        public boolean multiplePathsTo(int node) {
            return multiplePathsTo[node];
        }
    }

    /** Returns a representation of the paths from the given
     *  node to any other node it is connected to in the graph.
     * @param node
     */
    public PathsFrom pathsFrom(int node) {
        return new PathsFrom(node);
    }

}

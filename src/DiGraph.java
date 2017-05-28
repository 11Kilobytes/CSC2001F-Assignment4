/** This class represents a weighted directed graph
 * In a graph (V, E), nodes are represented with the integers 0..(|V| - 1).
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.PriorityQueue;

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
    public final class pathsFrom {
         
    }
}

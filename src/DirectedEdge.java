import java.util.LinkedList;

/** This class represents an edge of a weighted directed graph.
 * Nodes in a graph (V, E) are represented by the integers 0..(|V| - 1)
 */

public final class DirectedEdge {
    private final int from;
    private final int to;
    private final int cost;

    /** Constructs a directed edge.
     * @param from the node where the edge should start.
     * @param to the node where the edge should end.
     * @param cost the cost (weight) of this edge.
     */
    public DirectedEdge(int from, int to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    /** Returns the node where this edge starts
     */
    public int from() {
        return this.from;
    }

    /** Returns the node where this edge ends. */
    public int to() {
        return this.to;
    }

    /** Returns the cost associated with traversing this edge */
    public int cost() {
        return this.cost;
    }

    /** Returns whether this directed edge is equal to the
     * given Object */
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass())
            return false;
        else  {
            DirectedEdge otherE = (DirectedEdge) other;
            return this.from() == otherE.from()
                && this.to() == otherE.to()
                && this.cost() == otherE.cost();
        }
    }
}

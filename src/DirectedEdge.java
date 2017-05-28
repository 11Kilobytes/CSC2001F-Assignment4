public final class DirectedEdge {
    private final int from;
    private final int to;
    private final int cost;
    public DirectedEdge(int from, int to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public int from() {
        return this.from;
    }

    public int to() {
        return this.to;
    }

    public int cost() {
        return this.cost;
    }
}

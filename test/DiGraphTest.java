import org.junit.Test;
import static org.junit.Assert.*;

import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DiGraphTest {
    @Test
    public void shouldMakeEmptyGraph() {
        DiGraph g = new DiGraph();
        assertEquals(0, g.V());
        assertEquals(0, g.E());
    }

    @Test
    public void shouldMakeGraphWithNode() {
        DiGraph g = new DiGraph();
        g.addEdge(new DirectedEdge(0, 0, 5));
        assertEquals(1, g.V());
        assertEquals(1, g.E());
        assertEquals(1, g.edgesFrom(0).size());
        assertEquals(g.edgesFrom(0).get(0), new DirectedEdge(0, 0, 5));
    }

    @Test
    public void testNonTrivialGraph() {
        DiGraph g = new DiGraph();
        g.addEdge(new DirectedEdge(0, 1, 3));
        assertEquals(2, g.V());

        g.addEdge(new DirectedEdge(1, 0, 3));
        assertEquals(2, g.V());
        assertEquals(2, g.E());
        g.addEdge(new DirectedEdge(0, 2, 3));

        List<DirectedEdge> edges = g.edgesFrom(0);
        assertTrue(edges.containsAll(Stream.of(new DirectedEdge(0, 1, 3),
                                               new DirectedEdge(0, 2, 3))
                                     .collect(Collectors.toList())));

        assertEquals(2, edges.size());

        edges = g.edgesFrom(1);
        assertTrue(edges.containsAll(Stream.of(new DirectedEdge(1, 0, 3))
                                     .collect(Collectors.toList())));
        assertEquals(1, edges.size());
    }
}

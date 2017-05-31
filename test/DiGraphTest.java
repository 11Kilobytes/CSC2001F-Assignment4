import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
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

    @Test
    public void testDijkstraSimple() {
        DiGraph g = new DiGraph();
        g.addEdge(new DirectedEdge(0, 1, 5));
        g.addEdge(new DirectedEdge(0, 2, 2));
        g.addEdge(new DirectedEdge(2, 1, 1));
        DiGraph.PathsFrom pathsFrom0 = g.pathsFrom(0);
        assertTrue(pathsFrom0.hasPathTo(1));
        assertFalse(pathsFrom0.multiplePathsTo(1));
        List<DirectedEdge> pathTo1 = pathsFrom0.pathTo(1);
        assertNotNull(pathTo1);
        assertTrue(pathTo1.size() == 2);
        assertEquals(new DirectedEdge(0, 2, 2), pathTo1.get(0));
        assertEquals(new DirectedEdge(2, 1, 1), pathTo1.get(1));
    }

    @Test
    public void testMultiplePaths() {
        DiGraph g = new DiGraph();
        g.addEdge(new DirectedEdge(0, 1, 5));
        g.addEdge(new DirectedEdge(0, 2, 2));
        g.addEdge(new DirectedEdge(2, 1, 1));
        g.addEdge(new DirectedEdge(0,3,1));
        g.addEdge(new DirectedEdge(3, 4,1));
        g.addEdge(new DirectedEdge(4,1,1));
        DiGraph.PathsFrom pathsFrom0 = g.pathsFrom(0);
        assertTrue(Math.abs(pathsFrom0.distTo(1) - 3) < 1E-6);
        assertTrue(pathsFrom0.multiplePathsTo(1));

    }
}

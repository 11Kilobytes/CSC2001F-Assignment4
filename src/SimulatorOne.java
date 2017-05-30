import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by kabelomoiloa on 29/05/2017.
 */
public class SimulatorOne {
    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            if (args[0] != null) {
                System.setIn(new FileInputStream(args[0]));
            }
        }
        DiGraph g = new DiGraph();
        Set<Integer> victims = new HashSet<>();
        Set<Integer> hospitals = new HashSet<>();

        Scanner s = new Scanner(System.in);
        int V = s.nextInt();
        s.nextLine();

        for (int i = 0; i < V; i++) {
            String line = s.nextLine();
            List<Integer> nodeData =
                    Arrays.stream(line.trim().split(" "))
                    .map(Integer::parseInt).collect(Collectors.toList());
            int node = nodeData.get(0);
            for (int j = 1; j < nodeData.size() - 1; j+=2) {
                g.addEdge(new DirectedEdge(node, nodeData.get(j), nodeData.get(j + 1)));
            }

        }
        int numHospitals = s.nextInt();
        for (int i = 0; i < numHospitals; i++) {
            int hospital = s.nextInt();
            hospitals.add(hospital);
        }

        int numVictims = s.nextInt();
        for (int i = 0; i < numVictims; i++) {
            int victim = s.nextInt();
            victims.add(victim);
        }

        Map<Integer, DiGraph.PathsFrom> pathsFrom = new HashMap<>();
        for (int i = 0; i < V; i++) {
            pathsFrom.put(i, g.pathsFrom(i));
        }

        Function<Integer, Function<Integer, Boolean>> roundTripExists =
                x -> (y -> pathsFrom.get(x).hasPathTo(y) && pathsFrom.get(y).hasPathTo(x));

        Function<Integer, Boolean> saveable =
                victim -> hospitals.stream().anyMatch(h -> roundTripExists.apply(victim).apply(h));

        Function<Integer, Function<Integer, Double>> roundTripCost =
                x -> (y -> pathsFrom.get(x).distTo(y) + pathsFrom.get(y).distTo(x));

        Function<List<DirectedEdge>, String> pathToStr =
                path -> {
                    if (path.isEmpty())
                        return "";
                    else
                        return path.stream().reduce("", (str, edge) -> str + " " + edge.to(), String::concat);
                };

        for (Integer v : victims) {
            System.out.println("victim " + v);
            if (saveable.apply(v)) {
                Integer closestHospital = hospitals.stream().min(Comparator.comparing(roundTripCost.apply(v))).get();
                Double closestDist = roundTripCost.apply(v).apply(closestHospital);
                Stream<Integer> bestHospitals = hospitals.stream()
                        .filter(h -> {
                            Double distToH = roundTripCost.apply(v).apply(h);
                            return Math.abs(distToH - closestDist) < 1E-6;
                        });
                bestHospitals.forEach(h -> {
                    System.out.println("hospital " + h);
                    boolean multiplePaths = pathsFrom.get(h).multiplePathsTo(v) || pathsFrom.get(v).multiplePathsTo(h);
                    if (multiplePaths) {
                        System.out.println("multiple solutions cost " + Math.round(closestDist));
                    } else {
                        String roundTripPath = pathToStr.apply(pathsFrom.get(h).pathTo(v));
                        roundTripPath += pathToStr.apply(pathsFrom.get(v).pathTo(h));
                        System.out.print(roundTripPath);
                    }
                });
            } else {
                System.out.println("victim cannot be helped");
            }
        }
    }
}



package esame;

import java.util.List;
import java.util.Map;

import esame.graph.Edge;

public record ProblemInput(int V, int E, List<Edge> edges, int source, int destination,
                List<Integer> stops, List<Edge> updates, Map<String, Integer> cityMapping, String[] inverseMapping) {
}
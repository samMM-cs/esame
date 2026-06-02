package esame;

import java.util.List;
import java.util.Map;

import esame.graph.Edge;

public record ProblemInput(int V, int E, List<Edge> edges, Integer partenza, Integer destinazione,
    List<Integer> tappe, List<Edge> aggiornamenti, Map<String, Integer> cityMapping) {
}
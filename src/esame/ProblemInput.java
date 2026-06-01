package esame;

import java.util.List;

import esame.graph.Edge;

public record ProblemInput(int V, int E, List<Edge<String>> edges, String partenza, String destinazione,
        List<String> tappe, List<Edge<String>> aggiornamenti) {
}
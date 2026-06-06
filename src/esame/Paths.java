package esame;

import java.util.ArrayList;
import java.util.List;

import esame.graph.Dijkstra;
import esame.graph.Edge;
import esame.graph.Graph;

public class Paths {
  private final String[] cities;
  private final int destination;
  private final List<Integer> stops;
  private final int source;
  private final Iterable<Edge>[] paths;
  private double cost;
  private List<String> steps;

  @SuppressWarnings("unchecked")
  public Paths(String[] cities, Graph graph, int source, List<Integer> stops, int destination) {
    this.cities = cities;
    this.source = source;
    this.stops = stops;
    this.destination = destination;
    this.paths = (Iterable<Edge>[]) new Iterable[stops.size() + 1];
    this.cost = Double.NEGATIVE_INFINITY;
    this.paths(graph);
  }

  private void paths(Graph graph) {
    if (stops.isEmpty()) {
      this.paths[0] = new Dijkstra(source, graph).pathTo(destination);
    } else {
      this.paths[0] = new Dijkstra(source, graph).pathTo(stops.get(0));
      for (int i = 0; i < stops.size() - 1; i++)
        this.paths[i + 1] = new Dijkstra(stops.get(i), graph).pathTo(stops.get(i + 1));
      this.paths[stops.size()] = new Dijkstra(stops.get(stops.size() - 1), graph).pathTo(destination);
    }
  }

  public List<String> steps() {
    if (this.steps == null) {
      this.cost = 0;
      this.steps = new ArrayList<>();
      this.steps.add(this.cities[this.source]);
      for (Iterable<Edge> edges : this.paths) {
        if (edges == null) {
          this.cost = Double.POSITIVE_INFINITY;
          this.steps.clear();
          this.steps.add("Nessun percorso disponibile");
          return this.steps;
        }
        for (Edge edge : edges) {
          this.steps.add(cities[edge.to()]);
          this.cost += edge.weight();
        }
      }
    }
    return this.steps;
  }

  public double cost() {
    if (Double.isInfinite(this.cost))
      this.steps();
    return cost;

  }

  @Override
  public String toString() {
    return String.format("Paths[source=%d, destination=%d, stops=%s, paths=%d, cost=%s, steps=%s]",
        source, destination, stops, paths.length, cost, steps);
  }
}

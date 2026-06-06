package esame;

import java.util.ArrayList;
import java.util.List;

import esame.graph.Dijkstra;
import esame.graph.Edge;
import esame.graph.Graph;

public class Paths {
  private final String[] cities; // cities[i] = name of city associated with vertex i
  private final int source;
  private final int destination;
  private final List<Integer> stops;
  private final Iterable<Edge>[] paths;
  // cache the cost and steps calculations
  private double cost;
  private final List<String> steps;

  /**
   * Initialize the paths calculator and compute the segments
   * 
   * @param cities      Mapping vertices to names
   * @param graph       Graph representing the network
   * @param source      Starting vertex
   * @param stops       Intermediate stops
   * @param destination Destination vertex
   */
  @SuppressWarnings("unchecked")
  public Paths(String[] cities, Graph graph, int source, List<Integer> stops, int destination) {
    this.cities = cities;
    this.source = source;
    this.stops = stops;
    this.destination = destination;
    this.paths = (Iterable<Edge>[]) new Iterable[stops.size() + 1];
    this.cost = Double.NEGATIVE_INFINITY;
    this.steps = new ArrayList<>();
    this.paths(graph);
  }

  /**
   * Calculates shortest paths for each segment of the journey
   * 
   * @param graph Graph to traverse
   */
  private void paths(Graph graph) {
    if (stops.isEmpty()) {
      // if no stops, just go from source to destination
      this.paths[0] = new Dijkstra(source, graph).pathTo(destination);
    } else {
      // otherwise calculate each segment in order
      this.paths[0] = new Dijkstra(source, graph).pathTo(stops.get(0));
      for (int i = 0; i < stops.size() - 1; i++)
        this.paths[i + 1] = new Dijkstra(stops.get(i), graph).pathTo(stops.get(i + 1));
      this.paths[stops.size()] = new Dijkstra(stops.get(stops.size() - 1), graph).pathTo(destination);
    }
  }

  /**
   * Assemble the full path with city names and compute total cost
   * Cache computed values on newer calculations
   * 
   * @return List of city names in the full route,
   *         singleton list with error message otherwise
   */
  public List<String> steps() {
    if (this.steps.isEmpty()) {
      this.cost = 0;
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

  /**
   * Compute total cost of the route
   * 
   * @return Total cost, or Double.POSITIVE_INFINITY if the path is impossible
   */
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

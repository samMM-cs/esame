package esame.graph;

import java.util.Arrays;

import esame.utils.IndexedPriorityQueue;
import esame.utils.Stack;

public class Dijkstra {
  private final int source; // starting vertex for shortest path calculations
  private final Graph graph;
  private final IndexedPriorityQueue<Double> minDistances;
  private final double[] distTo; // distTo[v] = total cost of shortest path from source to v
  private final Edge[] edgeTo; // edgeTo[v] = last edge in the shortest path from source to v

  /**
   * Initialize data structures and compute shortest paths
   * 
   * @param source Starting vertex
   * @param graph  Graph to traverse
   */
  public Dijkstra(int source, Graph graph) {
    this.source = source;
    this.graph = graph;
    this.minDistances = new IndexedPriorityQueue<Double>(graph.getV());
    this.distTo = new double[graph.getV()];
    this.edgeTo = new Edge[graph.getV()];
    this.dijkstra();
  }

  /**
   * Run Dijkstra's algorithm to find shortest path from source
   */
  private void dijkstra() {
    // Initialize distances to infinity
    for (int i = 0; i < distTo.length; i++)
      this.distTo[i] = Double.POSITIVE_INFINITY;
    // source is at distance 0 from itself
    this.distTo[source] = 0;
    // start with a 0-distance source
    minDistances.decreaseKey(source, 0.0);

    // Process all vertices in the queue
    while (!minDistances.isEmpty()) {
      // Pull vertex with lowest distance
      int min = minDistances.deleteMin();
      // Relax all the edges leaving this vertex and
      // update distances and paths
      for (Edge edge : graph.adj(min)) {
        int v = edge.to();
        double newWeight = distTo[min] + edge.weight();
        if (newWeight < distTo[v]) {
          distTo[v] = newWeight;
          edgeTo[v] = edge;
          minDistances.decreaseKey(v, newWeight);
        }
      }
    }
  }

  /**
   * Construct path from source to v
   * 
   * @param v Destination vertex
   * @return Collection of edges forming the path, null if no path exists
   */
  public Iterable<Edge> pathTo(int v) {
    if (Double.isInfinite(distTo[v]))
      return null;
    Stack<Edge> path = new Stack<>();
    // Push edges in opposite order
    for (Edge edge = edgeTo[v]; edge != null; edge = edgeTo[edge.from()])
      path.push(edge);
    return path;
  }

  @Override
  public String toString() {
    return String.format("Dijkstra[source=%d, distTo=%s, edgeTo=%s]",
        source, Arrays.toString(distTo), Arrays.toString(edgeTo));
  }
}

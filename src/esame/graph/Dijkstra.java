package esame.graph;

import java.util.Arrays;

import esame.utils.IndexedPriorityQueue;
import esame.utils.Stack;

public class Dijkstra {
  private final int source;
  private final Graph graph;
  private final IndexedPriorityQueue<Double> minDistances;
  private final double[] distTo;
  private final Edge[] edgeTo;

  public Dijkstra(int source, Graph graph) {
    this.source = source;
    this.graph = graph;
    this.minDistances = new IndexedPriorityQueue<Double>(graph.getV());
    this.distTo = new double[graph.getV()];
    this.edgeTo = new Edge[graph.getV()];
    this.dijkstra();
  }

  private void dijkstra() {
    for (int i = 0; i < distTo.length; i++)
      this.distTo[i] = Double.POSITIVE_INFINITY;
    this.distTo[source] = 0;
    for (int i = 0; i < graph.getV(); i++)
      minDistances.insert(i, Double.POSITIVE_INFINITY);
    minDistances.decreaseKey(source, 0.0);

    while (!minDistances.isEmpty()) {
      int min = minDistances.deleteMin();
      for (Edge edge : graph.adj(min)) {
        if (minDistances.decreaseKey(edge.to(), distTo[min] + edge.weight())) {
          distTo[edge.to()] = distTo[min] + edge.weight();
          edgeTo[edge.to()] = edge;
        }
      }
    }
  }

  public double[] getDistTo() {
    return distTo;
  }

  public Edge[] getEdgeTo() {
    return edgeTo;
  }

  public Iterable<Edge> pathTo(int v) {
    if (Double.isInfinite(distTo[v]))
      return null;
    Stack<Edge> path = new Stack<>();
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

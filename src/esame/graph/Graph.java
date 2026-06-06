package esame.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
  // number of vertices in the graph
  private int V;

  // adjacency list of edges
  private List<Edge>[] adjacencyList;

  @SuppressWarnings("unchecked")
  public Graph(int V, int E, List<Edge> edges) {
    this.V = V;

    // cast necessary because cannot create generic arrays
    this.adjacencyList = (List<Edge>[]) new List[this.V];
    for (int i = 0; i < adjacencyList.length; i++)
      adjacencyList[i] = new ArrayList<>();

    for (Edge edge : edges) {
      int u = edge.from();
      int v = edge.to();

      // add bidirectional link
      adjacencyList[u].add(new Edge(u, v, edge.weight()));
      adjacencyList[v].add(new Edge(v, u, edge.weight()));
    }
  }

  public void applyUpdate(Edge update) {
    int from = (update.from());
    int to = (update.to());
    double weight = update.weight() < 0 ? Double.POSITIVE_INFINITY : update.weight();
    for (Edge edge : adjacencyList[from]) {
      if (edge.to() == to) {
        edge.setWeight(weight);
        break;
      }
    }
    for (Edge edge : adjacencyList[to]) {
      if (edge.to() == from) {
        edge.setWeight(weight);
        break;
      }
    }
  }

  public void applyUpdates(List<Edge> updates) {
    for (Edge update : updates) {
      applyUpdate(update);
    }
  }

  public int getV() {
    return V;
  }

  public int getE() {
    return Arrays.stream(adjacencyList).mapToInt(List::size).sum();
  }

  public List<Edge> adj(int v) {
    return this.adjacencyList[v];
  }

  @Override
  public String toString() {
    return String.format("Graph[V=%d, E=%d, adj=%s]",
        V, this.getE(), Arrays.toString(adjacencyList));
  }
}

package esame.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
  // number of vertices in the graph
  private int V;

  // adjacency list of edges
  private List<Edge>[] adjacencyList;

  /**
   * Construct a bidirectional graph
   * 
   * @param V     Number of vertices
   * @param E     Initial number of edges
   * @param edges Collection of initial edges
   */
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

  /**
   * Apply inplace update to the graph
   * 
   * @param update New edge representing values to update
   */
  public void applyUpdate(Edge update) {
    int from = update.from();
    int to = update.to();
    // Interpret negative weights as disconnection
    double weight = update.weight() < 0 ? Double.POSITIVE_INFINITY : update.weight();
    // Update edge in both ways
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

  /**
   * Apply more than one update
   * 
   * @param updates List of edge updates
   */
  public void applyUpdates(List<Edge> updates) {
    for (Edge update : updates) {
      applyUpdate(update);
    }
  }

  /**
   * Return vertex count
   * 
   * @return Node count
   */
  public int getV() {
    return V;
  }

  /**
   * Return amount of current directed edges in the graph
   * 
   * @return Edge count
   */
  public int getE() {
    return Arrays.stream(adjacencyList).mapToInt(List::size).sum();
  }

  /**
   * Return adjacency of a vertex
   * 
   * @param v Source vertex of adjacency
   * @return Adjacency list of v
   */
  public List<Edge> adj(int v) {
    return this.adjacencyList[v];
  }

  @Override
  public String toString() {
    return String.format("Graph[V=%d, E=%d, adj=%s]",
        V, this.getE(), Arrays.toString(adjacencyList));
  }
}

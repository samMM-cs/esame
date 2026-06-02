package esame.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
  // number of vertices in the graph
  private int V;

  // number of edges
  private int E;

  // adjacency list of edges
  private List<Edge>[] adjacencyList;

  @SuppressWarnings("unchecked")
  public Graph(int V, int E, List<Edge> edges) {
    this.V = V;
    this.E = E;

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

  public void applyUpdates(List<Edge> updates) {
    for (Edge update : updates) {
      int from = (update.from());
      int to = (update.to());
      double weight = update.weight();
      // find edges with matching vertices and update weight
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
  }

  public int getV() {
    return V;
  }

  public int getE() {
    return E;
  }

  @Override
  public String toString() {
    return String.format("Graph[V=%d, E=%d, adj=%s]",
        V, E, Arrays.toString(adjacencyList));
  }
}

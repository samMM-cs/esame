package esame.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
  // number of vertices in the graph
  private int V;
  // number of edges
  private int E;
  // association of names -> indices
  private Map<String, Integer> nodeMapping;
  // adjacency list of edges
  private List<Edge<Integer>>[] adjacencyList;

  @SuppressWarnings("unchecked")
  public Graph(int V, int E, List<Edge<String>> edges) {
    this.V = V;
    this.E = E;
    this.nodeMapping = new HashMap<>(this.V);

    // cast necessary because cannot create generic arrays
    this.adjacencyList = (List<Edge<Integer>>[]) new List[this.V];
    for (int i = 0; i < adjacencyList.length; i++)
      adjacencyList[i] = new ArrayList<>();

    for (Edge<String> edge : edges) {
      String node1 = edge.from();
      String node2 = edge.to();
      // if nodes are not present in the dictionary,
      // add them with the next available index
      nodeMapping.putIfAbsent(node1, nodeMapping.size());
      nodeMapping.putIfAbsent(node2, nodeMapping.size());
      int u = nodeMapping.get(node1);
      int v = nodeMapping.get(node2);
      // add bidirectional link
      adjacencyList[u].add(new Edge<Integer>(u, v, edge.weight()));
      adjacencyList[v].add(new Edge<Integer>(v, u, edge.weight()));
    }
  }

  public void applyUpdates(List<Edge<String>> updates) {
    for (Edge<String> update : updates) {
      int from = nodeMapping.get(update.from());
      int to = nodeMapping.get(update.to());
      double weight = update.weight();
      // find edges with matching vertices and update weight
      for (Edge<Integer> edge : adjacencyList[from]) {
        if (edge.to() == to) {
          edge.setWeight(weight);
          break;
        }
      }
      for (Edge<Integer> edge : adjacencyList[to]) {
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
    return String.format("Graph[V=%d, E=%d, nodes=%s, adj=%s]",
        V, E, nodeMapping, Arrays.toString(adjacencyList));
  }
}

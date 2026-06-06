package esame.graph;

public class Edge {
  private final int from;
  private final int to;
  private double weight;

  public Edge(int from, int to, double weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  public int from() {
    return from;
  }

  public int to() {
    return to;
  }

  public double weight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "Edge[from=" + from + ", to=" + to + ", weight=" + weight + "]";
  }
}
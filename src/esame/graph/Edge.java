package esame.graph;

import java.util.Objects;

public final class Edge {
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
  public boolean equals(Object obj) {
    return (this == obj)
        || (obj instanceof Edge other)
            && weight == other.weight
            && Objects.equals(from, other.from)
            && Objects.equals(to, other.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, weight);
  }

  @Override
  public String toString() {
    return "edge[from=" + from + ", to=" + to + ", weight=" + weight + "]";
  }
}
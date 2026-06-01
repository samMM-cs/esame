package esame.graph;

import java.util.Objects;

public final class Edge<V> {
  private final V from;
  private final V to;
  private double weight;

  public Edge(V from, V to, double weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  public V from() {
    return from;
  }

  public V to() {
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
        || (obj instanceof Edge<?> other)
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
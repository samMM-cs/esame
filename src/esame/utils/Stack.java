package esame.utils;

import java.util.Iterator;

public class Stack<T> implements Iterable<T> {
  private Node<T> head;
  private int size;

  private static class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T item, Node<T> next) {
      this.item = item;
      this.next = next;
    }
  }

  public Stack() {
    this.head = null;
    this.size = 0;
  }

  public boolean isEmpty() {
    return this.head == null;
  }

  public int size() {
    return size;
  }

  public void push(T item) {
    Node<T> old = this.head;
    this.head = new Node<>(item, old);
    this.size++;
  }

  public T pop() {
    if (this.isEmpty())
      return null;
    Node<T> old = this.head;
    this.head = old.next;
    this.size--;
    return old.item;
  }

  @Override
  public Iterator<T> iterator() {
    return new StackIterator<T>(this.head);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Stack[size=").append(size).append(", items=[");
    Node<T> current = head;
    while (current != null) {
      if (current != head)
        sb.append(", ");
      sb.append(current.item);
      current = current.next;
    }
    sb.append("]]");
    return sb.toString();
  }

  private static class StackIterator<T> implements Iterator<T> {
    private Node<T> node;

    public StackIterator(Node<T> node) {
      this.node = node;
    }

    @Override
    public boolean hasNext() {
      return this.node != null;
    }

    @Override
    public T next() {
      Node<T> old = this.node;
      this.node = old.next;
      return old.item;
    }

  }

}

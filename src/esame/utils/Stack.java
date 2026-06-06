package esame.utils;

import java.util.Iterator;

public class Stack<T> implements Iterable<T> {
  private Node<T> head; // Top element of the stack
  private int size; // Current number of elements in the stack

  /**
   * Inner class representing elements in the stack as singly-linked list
   */
  private static class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T item, Node<T> next) {
      this.item = item;
      this.next = next;
    }
  }

  /**
   * Initializes the empty stack
   */
  public Stack() {
    this.head = null;
    this.size = 0;
  }

  /**
   * Checks if stack is empty
   * 
   * @return true if the stack is empty
   */
  public boolean isEmpty() {
    return this.head == null;
  }

  /**
   * Returns current size of the stack
   * 
   * @return Size of the stack
   */
  public int size() {
    return size;
  }

  /**
   * Push new item at the top of the stack
   * 
   * @param item item to be added
   */
  public void push(T item) {
    Node<T> old = this.head;
    this.head = new Node<>(item, old);
    this.size++;
  }

  /**
   * Removes item at the top of the stack
   * 
   * @return item removed from the stack, null if the stack is empty
   */
  public T pop() {
    if (this.isEmpty())
      return null;
    Node<T> old = this.head;
    this.head = old.next;
    this.size--;
    return old.item;
  }

  /**
   * Returns the element at the top of the stack without popping it
   * 
   * @return The element at the top of the stack
   */
  public T peek() {
    if (this.isEmpty())
      return null;
    return this.head.item;
  }

  /**
   * Clears the stack
   */
  public void clear() {
    this.head = null;
    this.size = 0;
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

  /**
   * Custom iterator over the linked list nodes
   */
  private static class StackIterator<T> implements Iterator<T> {
    private Node<T> node;

    /**
     * Initialize the iterator from a specific node
     * 
     * @param node Node to start the iteration from
     */
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

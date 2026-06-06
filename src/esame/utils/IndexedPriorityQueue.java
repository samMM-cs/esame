package esame.utils;

import java.util.Arrays;

public class IndexedPriorityQueue<K extends Comparable<K>> {
  private final int maxSize;
  private int size = 0; // current number of elements in queue
  private final K[] keys; // keys[i] = priority of element i
  private final int[] pq; // indices of elements in priority queue, represents binary heap
  private final int[] qp; // inverse mapping of pq

  @SuppressWarnings("unchecked")
  public IndexedPriorityQueue(int maxSize) {
    this.maxSize = maxSize;
    // unchecked cast from raw array because java doesnt have generic arrays
    this.keys = (K[]) new Comparable[maxSize];
    this.pq = new int[maxSize + 1]; // pq is 1-based
    this.qp = new int[maxSize];
    // initialize empty qp
    for (int i = 0; i < qp.length; i++)
      this.qp[i] = -1;
  }

  /**
   * Checks if the queue is empty
   * 
   * @return true if queue is empty
   */
  public boolean isEmpty() {
    return this.size == 0;
  }

  /**
   * Checks if index is present in the queue
   * 
   * @param i index to look up
   * @return true if index is in valid range and exists in the queue
   */
  public boolean contains(int i) {
    return this.validIndex(i) && qp[i] != -1;
  }

  /**
   * Gets the number of elements stored in the queue
   * 
   * @return number of elements
   */
  public int size() {
    return this.size;
  }

  /**
   * Associates a key with an index and inserts it into the queue. *
   * 
   * @param i   index to insert associate with the key
   * @param key key to insert
   * @return true if the key was successfully inserted
   */
  public boolean insert(int i, K key) {
    if (!this.validIndex(i) || this.contains(i))
      return false;
    this.size++;
    this.qp[i] = this.size;
    this.pq[this.size] = i;
    this.keys[i] = key;
    this.bubbleUp(this.size);
    return true;
  }

  /**
   * Gets the index associated with the lowest priority in the queue
   * 
   * @return index of the minimum element, -1 if the queue is empty
   */
  public int minIndex() {
    if (this.size == 0)
      return -1;
    return this.pq[1];
  }

  /**
   * Gets the minimum key stored in the queue
   * 
   * @return minimum key, null if the queue is empty
   */
  public K minKey() {
    if (this.size == 0)
      return null;
    return this.keys[this.pq[1]];
  }

  /**
   * Removes the minimum element from the queue and returns its index
   * Cleans up internal mappings
   * 
   * @return index of deleted minimum, -1 if the queue is empty
   */
  public int deleteMin() {
    if (this.size == 0)
      return -1;
    int min = this.pq[1];
    this.exchange(1, this.size--);
    this.qp[min] = -1;
    this.keys[min] = null;
    this.pq[this.size + 1] = -1;
    this.bubbleDown(1);
    return min;
  }

  /**
   * Gets the key associated to an index
   * 
   * @param i index to lookup
   * @return key associated to index i, null if the index is not valid or missing
   */
  public K get(int i) {
    if (!this.validIndex(i) || !this.contains(i))
      return null;
    return this.keys[i];
  }

  /**
   * Modifies the priority key associated with an index
   * Adjusts the element's position to mantain heap properties
   * 
   * @param i   index of item to modify
   * @param key new value of key
   */
  public void changeKey(int i, K key) {
    if (this.validIndex(i) && this.contains(i)) {
      this.keys[i] = key;
      this.bubbleUp(this.qp[i]);
      this.bubbleDown(this.qp[i]);
    }
  }

  /**
   * Lowers the priority key associated with an index
   * 
   * @param i   index of item to modify
   * @param key new, smaller value of key
   * @return true if the key is successfully decreased
   */
  public boolean decreaseKey(int i, K key) {
    if (!this.validIndex(i) || !this.contains(i) || this.keys[i].compareTo(key) <= 0)
      return false;
    this.keys[i] = key;
    this.bubbleUp(this.qp[i]);
    return true;
  }

  /**
   * Raises the priority key associated with an index
   * 
   * @param i   index of item to modify
   * @param key new, larger value of key
   * @return true if the key is successfully increased
   */
  public boolean increaseKey(int i, K key) {
    if (!this.validIndex(i) || !this.contains(i) || this.keys[i].compareTo(key) >= 0)
      return false;
    this.keys[i] = key;
    this.bubbleDown(this.qp[i]);
    return true;
  }

  /**
   * Deletes item associated with an index
   * 
   * @param i index of the item to delete
   */
  public void delete(int i) {
    if (this.validIndex(i) && this.contains(i)) {
      int index = this.qp[i];
      this.exchange(index, this.size--);
      if (index <= this.size) {
        this.bubbleUp(index);
        this.bubbleDown(index);
      }
      this.keys[i] = null;
      this.qp[i] = -1;
      this.pq[this.size + 1] = -1;
    }
  }

  /**
   * Bubbles an element up the heap until heap properties are restored
   * 
   * @param i index of the item to move up
   */
  private void bubbleUp(int i) {
    while (i > 1 && this.hasGreaterKey(i / 2, i)) {
      this.exchange(i, i / 2);
      i /= 2;
    }
  }

  /**
   * Swaps two elements in the heap and updates the mappings
   * 
   * @param i the heap position of the first element
   * @param j the heap position of the second element
   */
  private void exchange(int i, int j) {
    int tmp = this.pq[i];
    this.pq[i] = this.pq[j];
    this.pq[j] = tmp;
    this.qp[this.pq[i]] = i;
    this.qp[this.pq[j]] = j;
  }

  /**
   * Pushes an element down until heap properties are restored
   * 
   * @param i position of element to sink down
   */
  private void bubbleDown(int i) {
    while (2 * i <= this.size) {
      int j = 2 * i;
      if (j < this.size && this.hasGreaterKey(j, j + 1))
        j++;
      if (!this.hasGreaterKey(i, j))
        break;
      this.exchange(i, j);
      i = j;
    }
  }

  /**
   * Compares the priority keys of two elements based on their heap indices
   * 
   * @param i the position of the first element to compare
   * @param j the position of the second element to compare
   * @return true if the key at index i compares higher than key at index j
   */
  private boolean hasGreaterKey(int i, int j) {
    return this.keys[this.pq[i]].compareTo(this.keys[this.pq[j]]) > 0;
  }

  /**
   * Checks if an index is in the valid range of the heap
   * 
   * @param i the index to check
   * @return true if the index is valid
   */
  private boolean validIndex(int i) {
    return i >= 0 && i < this.maxSize;
  }

  @Override
  public String toString() {
    return String.format("IndexedPriorityQueue[maxSize=%d, size=%d, keys=%s, pq=%s, qp=%s]",
        maxSize, size, Arrays.toString(keys), Arrays.toString(pq), Arrays.toString(qp));
  }
}

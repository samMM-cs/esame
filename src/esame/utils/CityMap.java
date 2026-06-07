package esame.utils;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CityMap implements Map<String, Integer> {
  // Value used to mark a cell as deleted
  private static final String DELETED_MARKER = new String("###");
  // Mask to clear the first bit, used to force positive values
  // in the hash functions
  private static final int SIGN_MASK = ~(1 << 31);

  private final int M; // Size of the hashtable, must be prime
  private final int HASH2_PRIME; // Prime number used in the second hash function, must be smaller than M
  private int size; // Amount of items currently in hashtable
  private final String[] keys;
  private final Integer[] values;
  private final String[] inverseIDs; // Array for constant-time reverse lookup inverseIDs[i] = string currently
                                     // mapping to i

  /**
   * Constructs a new CityMap
   * 
   * @param V Maximum number of unique cities
   */
  public CityMap(int V) {
    // Use approximately double the space to keep the load factor
    // low and guarantee faster operations
    this.M = nextPrime(V * 2);
    // Guarantee that the second hash function uses a number
    // coprime with M, avoiding wrong behaviour
    this.HASH2_PRIME = nextPrime(M / 2);
    this.size = 0;
    this.keys = new String[this.M];
    this.values = new Integer[this.M];
    this.inverseIDs = new String[V];
  }

  /**
   * Primary hash function to get the starting index
   * 
   * @param key String to calculate hash of
   * @return Primary hash of key
   */
  private int hash1(String key) {
    return (key.hashCode() & SIGN_MASK) % this.M;
  }

  /**
   * Secondary hash function used to calculate step size
   * to deal with collisions
   * 
   * @param key String to calculate hash of
   * @return Secondary hash of key
   */
  private int hash2(String key) {
    return HASH2_PRIME - (key.hashCode() & SIGN_MASK) % HASH2_PRIME;
  }

  /**
   * Utility function used for finding the first prime above n
   * 
   * @param n Starting point for prime search
   * @return Smallest prime above n
   */
  private static int nextPrime(int n) {
    if (n <= 1)
      return 1;
    int prime = n + 1;
    while (!isPrime(prime))
      prime++;
    return prime;
  }

  /**
   * Utility naive primality test
   * 
   * @param n Number to check primality of
   * @return true if n is prime
   */
  private static boolean isPrime(int n) {
    // skip small primes
    if (n <= 1)
      return false;
    if (n <= 3)
      return true;
    if (n % 2 == 0 || n % 3 == 0)
      return false;
    // only check numbers up to the square root of n and
    // having modulo +1 or -1 with 6
    for (int i = 5; i * i < n; i += 6)
      if (n % i == 0 || n % (i + 2) == 0)
        return false;
    return true;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    for (int i = 0; i < M; i++)
      // skip empty slots and deleted values
      if (keys[i] != null && keys[i] != DELETED_MARKER && Objects.equals(values[i], value))
        return true;
    return false;
  }

  @Override
  public Integer get(Object keyObj) {
    if (!(keyObj instanceof String key))
      return null;
    int h1 = hash1(key);
    int h2 = hash2(key);
    int i = h1;
    // scan along the table until an empty slot is found
    while (this.keys[i] != null) {
      // element found if element is not deleted and keys match
      if (keys[i] != DELETED_MARKER && keys[i].equals(key))
        return values[i];
      // element was deleted or a collision occurred
      // jump ahead by the second hash size
      i = (i + h2) % M;
    }
    // element not found
    return null;
  }

  @Override
  public Integer put(String key, Integer value) {
    if (key == null)
      return null;

    int h1 = this.hash1(key);
    int h2 = this.hash2(key);
    int firstDeleted = -1; // keep track of the first deleted slot, if any, to replace it
    int i = h1;

    // scan along the list until the key or an empty slot is found
    while (this.keys[i] != null) {
      if (this.keys[i] == DELETED_MARKER) {
        // found a deleted spot, save the index to replace it
        if (firstDeleted == -1)
          firstDeleted = i;
      } else if (this.keys[i].equals(key)) {
        // found the key, replace the value
        Integer old = this.values[i];
        this.values[i] = value;
        // clear old reverse lookup
        if (old != null && !old.equals(value) && old >= 0 && old < this.inverseIDs.length)
          this.inverseIDs[old] = null;
        // update the reverse lookup if the value is a valid id
        if (value != null && value >= 0 && value < this.inverseIDs.length)
          this.inverseIDs[value] = key;
        return old;
      }
      // jump ahead according to double hashing
      i = (i + h2) % this.M;
    }
    // insert into the first deleted spot or the first empty spot
    int insert = (firstDeleted != -1) ? firstDeleted : i;
    this.keys[insert] = key;
    this.values[insert] = value;

    // add a reverse lookup if the value is valid
    if (value != null && value >= 0 && value < this.inverseIDs.length)
      this.inverseIDs[value] = key;
    this.size++;
    return null;
  }

  @Override
  public Integer remove(Object keyObj) {
    if (!(keyObj instanceof String key))
      return null;
    int h1 = hash1(key);
    int h2 = hash2(key);
    int i = h1;

    // scan along until a match is found or the key does not exist
    while (this.keys[i] != null) {
      // if a match is found, perform the deletion
      if (this.keys[i] != DELETED_MARKER && keys[i].equals(key)) {
        // mark the slot as deleted and delete the value
        Integer old = values[i];
        keys[i] = DELETED_MARKER;
        values[i] = null;
        // clear the reverse lookup, if old value is valid
        if (old != null && old >= 0 && old < inverseIDs.length)
          inverseIDs[old] = null;
        size--;
        return old;
      }
      // double hashing jump ahead
      i = (i + h2) % this.M;
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends String, ? extends Integer> m) {
    m.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
  }

  @Override
  public void clear() {
    Arrays.fill(this.keys, null);
    Arrays.fill(this.values, null);
    Arrays.fill(this.inverseIDs, null);
    this.size = 0;
  }

  @Override
  public Set<String> keySet() {
    return IntStream.range(0, this.M)
        .filter(i -> keys[i] != null && keys[i] != DELETED_MARKER)
        .mapToObj(i -> keys[i])
        .collect(Collectors.toSet());
  }

  @Override
  public Collection<Integer> values() {
    return IntStream.range(0, M)
        .filter(i -> keys[i] != null && keys[i] != DELETED_MARKER)
        .mapToObj(i -> values[i])
        .toList();
  }

  @Override
  public Set<Entry<String, Integer>> entrySet() {
    return IntStream.range(0, M)
        .filter(i -> keys[i] != null && keys[i] != DELETED_MARKER)
        .mapToObj(i -> new AbstractMap.SimpleEntry<>(keys[i], values[i]))
        .collect(Collectors.toSet());
  }

  /**
   * Return the inverse mapping used for constant reverse lookups
   * 
   * @return The inverse mapping
   */
  public String[] getInverseIds() {
    return this.inverseIDs;
  }

}

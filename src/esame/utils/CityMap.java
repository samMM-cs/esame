package esame.utils;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CityMap implements Map<String, Integer> {
  private static final String DELETED_MARKER = "💀💀💀";
  private static final int SIGN_MASK = ~(1 << 31);

  private final int M;
  private final int HASH2_PRIME;
  private int size;
  private final String[] keys;
  private final Integer[] values;
  private final String[] inverseIDs;

  public CityMap(int V) {
    // keep load factor low, will waste a bit of space, but will guarantee faster
    // operations
    this.M = nextPrime(V * 2);
    this.HASH2_PRIME = nextPrime(M / 2);
    this.size = 0;
    this.keys = new String[this.M];
    this.values = new Integer[this.M];
    this.inverseIDs = new String[V];
  }

  private int hash1(String key) {
    return (key.hashCode() & SIGN_MASK) % this.M;
  }

  private int hash2(String key) {
    // workaround to avoid loops when M < HASH2_PRIME
    int h2 = (HASH2_PRIME - (key.hashCode() & SIGN_MASK) % HASH2_PRIME) % this.M;
    return h2 == 0 ? 1 : h2;
  }

  private static int nextPrime(int n) {
    if (n <= 1)
      return 1;
    int prime = n + 1;
    while (!isPrime(prime))
      prime++;
    return prime;
  }

  private static boolean isPrime(int n) {
    if (n <= 1)
      return false;
    if (n <= 3)
      return true;
    if (n % 2 == 0 || n % 3 == 0)
      return false;
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
    for (int i = 0; i < M; i++) {
      if (keys[i] != null && keys[i] != DELETED_MARKER && values[i].equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Integer get(Object keyObj) {
    if (!(keyObj instanceof String key))
      return null;
    int h1 = hash1(key);
    int h2 = hash2(key);
    int i = h1;
    while (this.keys[i] != null) {
      if (keys[i] != DELETED_MARKER && keys[i].equals(key))
        return values[i];
      i = (i + h2) % M;
    }
    return null;
  }

  @Override
  public Integer put(String key, Integer value) {
    if (key == null)
      return null;

    int h1 = this.hash1(key);
    int h2 = this.hash2(key);
    int firstDeleted = -1;
    int i = h1;

    while (this.keys[i] != null) {
      if (this.keys[i] == DELETED_MARKER) {
        if (firstDeleted == -1)
          firstDeleted = i;
      } else if (this.keys[i].equals(key)) {
        Integer old = this.values[i];
        this.values[i] = value;
        if (value != null && value >= 0 && value < this.inverseIDs.length)
          this.inverseIDs[value] = key;
        return old;
      }
      i = (i + h2) % this.M;
    }
    int insert = (firstDeleted != -1) ? firstDeleted : i;
    this.keys[insert] = key;
    this.values[insert] = value;
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
    while (this.keys[i] != null) {
      if (this.keys[i] != DELETED_MARKER && keys[i].equals(key)) {
        Integer old = values[i];
        keys[i] = DELETED_MARKER;
        values[i] = null;
        if (old != null && old >= 0 && old < inverseIDs.length)
          inverseIDs[old] = null;
        size--;
        return old;
      }
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
    for (int i = 0; i < this.M; i++) {
      keys[i] = null;
      values[i] = null;
    }
    for (int i = 0; i < inverseIDs.length; i++) {
      inverseIDs[i] = null;
    }
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

  public String getCity(int id) {
    if (id >= 0 && id < inverseIDs.length)
      return inverseIDs[id];
    return null;
  }

  public String[] getInverseIds() {
    return this.inverseIDs;
  }

}

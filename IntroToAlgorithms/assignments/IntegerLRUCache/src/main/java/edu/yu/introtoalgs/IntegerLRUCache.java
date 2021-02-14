package edu.yu.introtoalgs;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

/** A class that provides a cache associating Integer-valued keys and values.
 * The implementation is constrained to a given capacity such that if the
 * number of entries exceeds the capacity, entries are removed to to maintain
 * the "does not exceed capacity" constraint.  When removing elements to
 * maintain the capacity constraint, the implementation follows the
 * "least-recently-used" policy.
 *
 */

public class IntegerLRUCache {

    public class Node {

        public Integer key;
        public Node next;
        public Node prev;

        public Node(Integer val, Node prev, Node next) {
            this.key = val;
            this.next = next;
            this.prev = prev;
        }

        public Integer getKey() {
            return this.key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(next, node.next) &&
                    Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, next, prev);
        }
    }

  /** Constructs an empty cache with the specified capacity.  The cache
   * implementation is forbidden from exceeding this capacity, but may choose
   * to use less than this capacity.
   *
   * @param initialCapacity the initial capacity
   */
  public final int initialCapacity;
  public Map<Integer, Integer> map = new HashMap<>();
  public Map<Integer, Node> keyToNode = new HashMap<>();
  public Node front = new Node (null,null, null);
  public Node end = new Node (null, null,null);
  public int sizeOfLRU = 0;

  /*
    public static void main(String[] args){
        IntegerLRUCache c = new IntegerLRUCache (3);
        c.put(1,2);
        c.put(2,4);
        System.out.println(c);
        c.remove(2);
        System.out.println(c);
        c.put(3, 6);
        System.out.println(c);
        c.get(1);
        c.put(4, 8);
        System.out.println(c);
        c.put(4, 7);
        System.out.println(c);
    }
   */

  public IntegerLRUCache(final int capacity) {
      if (capacity < 0){
          throw new IllegalArgumentException();
      }
    this.initialCapacity = capacity;
    front.next = end;
    end.prev = front;
  }

  /** Returns the cached value associated with the specified key, null if not
   * cached previously
   *
   * @param key the key whose associated value is to be returned
   * @return the previously cached value, or null if not previously cached
   * @throws IllegalArgumentException if the key is null
   */
  public Integer get(final Integer key) {
    if (key == null){
      throw new IllegalArgumentException("Null argument");
    }
     Integer x = this.map.get(key);
     if (x != null){
         Node node = this.keyToNode.get(key);
         Node prev = node.prev;
         Node next = node.next;
         //remove node
        prev.next = next;
        next.prev = prev;
        //place node at end of LRU
         Node last = end.prev;
         node.prev = last;
         node.next = end;
         last.next = node;
         end.prev = node;
     }
     return x;
  }

  /** Associates the specified value with the specified key. If the cache
   * previously contained an association for this key, the old value is
   * replaced, and the key is now associated with the specified value.  If
   * inserting this item will cause the cache to exceed its capacity, the
   * method will evict some other item to maintain the capacity constraint.
   * The item selected is the least-recently-used ("accessed") item.
   *
   * @param key key with which the specified value is to be associated
   * @param value value to be cached
   * @return the value associated with this key if previously cached, otherwise
   * null
   * @throws IllegalArgumentException if either the key or value is null
   */
  public Integer put (final Integer key, final Integer value) {
      if (key == null || value == null){
        throw new IllegalArgumentException("Null argument");
      }
      Integer x = this.map.put(key, value);

      //If this key is already in cache in earlier location, remove it
      if (keyToNode.get(key) != null){
          Node node = this.keyToNode.get(key);
          Node prev = node.prev;
          Node next = node.next;
          prev.next = next;
          next.prev = prev;
      }

      //Enter the key into the cache
      Node last = end.prev;
      Node node = new Node(key, last, end);
      node.prev = last;
      node.next = end;
      last.next = node;
      end.prev = node;
      keyToNode.put(key, end.prev);
      sizeOfLRU++;

      //Remove least recently used key
      if (map.size() > this.initialCapacity && sizeOfLRU > 0){
          Node lru = front.next;
          Node second = lru.next;
          front.next = second;
          second.prev = front;
          map.remove(lru.key);
          keyToNode.remove(lru.key);
          sizeOfLRU--;
      }
      return x;
  }

  /** Removes the specified key-value mapping if present (returning the value
   * previously associated with the key), no-op returns null if no previous
   * association.
   *
   * @param key key whose mapping is to be removed
   * @return previous value associated with key, otherwise null
   * @throws IllegalArgumentException if the key is null
   */
  public Integer remove(Object key) {
      if (key == null){
        throw new IllegalArgumentException("Null argument");
      }
      Integer x = this.map.remove(key);
      if (x != null){
          Node node = this.keyToNode.get(key);
          Node before = node.prev;
          Node after = node.next;
          before.next = after;
          after.prev = before;
          this.keyToNode.remove(key);
      }
      return x;
  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerLRUCache that = (IntegerLRUCache) o;
        return initialCapacity == that.initialCapacity &&
                Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialCapacity, map);
    }

    @Override
    public String toString(){
      String string = "Initial Capacity: " + this.initialCapacity + "\nMap: " + map;
      return string;
    }
}

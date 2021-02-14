import java.util.LinkedList;

public class BitVectorTrie<Value> {
    
    private static final int R = 2;      
    private Node root;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean isRoutable(BitVector key) {
        return get(key) != null;
    }

   /****************************************************
    * get needs the most changes since its result depends
    * not on the entire key but on its longest matching 
    * prefix
    ****************************************************/
    public Value get(BitVector key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Value x = get(root, key, 0, null);
        if (x == null) return null;
        return x;
    }

    private Value get(Node x, BitVector key, int d, Value bestSoFar) {
        if (x == null) return null;
        /*
        if (d == key.size()){
            Value val = (Value) x.val;
        }
         */
        int c = key.get(d);
        if (x.val != null){
            bestSoFar = (Value) x.val;
        }
        if (x.next[c] == null){
            return bestSoFar;
        }
        return get(x.next[c], key, d+1, bestSoFar);
   }

   /****************************************************
    * Insert Value value into the prefix Trie.
    * If a different value exists for the same key
    * throw an IllegalArgumentException
    ****************************************************/
    public void put(BitVector key, Value port) {
        root = put(root, key, port, 0);
    }

    private Node put(Node x, BitVector key, Value port, int d) {
        if (x == null) x = new Node();
        if (d == key.size()) {
            if (x.val != null && !(x.val.equals(port))) {
                throw new IllegalArgumentException("Value");
            }
            x.val = port;
            return x;
        }
        int c = key.get(d);
        x.next[c] = put(x.next[c], key, port, d+1);
        return x;//just so it compiles
    }

   /****************************************************
    * Delete the value for a key.
    * If no value exists for this key
    * throw and IllegalArgumentException
    ****************************************************/
    public void delete(BitVector key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (get(key) == null) throw new IllegalArgumentException("No value exists for this key");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, BitVector key, int d) {
        if (x == null) return null;
        if (d == key.size()) {
            x.val = null;
        }
        else {
            int c = key.get(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }
}

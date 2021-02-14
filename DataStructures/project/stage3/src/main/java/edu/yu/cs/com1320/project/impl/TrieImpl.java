package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value> {

    private static final int alphabetSize = 256; // extended ASCII
    private Node root; // root of trie
    private List<Value> prefixList = new ArrayList<>();

    public class Node {
        protected List<Value> list = new ArrayList<>();
        protected Node[] links = new TrieImpl.Node[TrieImpl.alphabetSize];
    }

    public TrieImpl(){
    }

    @Override
    public void put(String key, Value val) {
        key = key.toUpperCase();
        //deleteAll the value from this key
        if (val == null) {
            return;
        }
        else {
            this.root = put(this.root, key, val, 0);
        }
    }
    /**
     *
     * @param x
     * @param key
     * @param val
     * @param d
     * @return
     */

    private TrieImpl.Node put(TrieImpl.Node x, String key, Value val, int d) {
        key = key.toUpperCase();
        if (x == null) {
            x = new TrieImpl.Node();
        }

        if (d == key.length()) {
            boolean hasAlready = false;
            for (Object v: x.list){
                if (v.equals(val)){
                    hasAlready = true;
                }
            }
            if (!hasAlready){
                x.list.add(val);
            }
            return x;
        }

        char c = key.charAt(d);
        x.links[c] = this.put(x.links[c], key, val, d + 1);
        return x;
    }

    @Override
    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        key = key.toUpperCase();
        ArrayList<Value> list = getList(key);
        if (list != null){
            Collections.sort(list, comparator);
        }
        return list;
    }

    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        prefix = prefix.toUpperCase();
        this.prefixList = new ArrayList<Value>();
        Node x = this.get(this.root, prefix, 0);
        getPrefix(x);
        if (x == null) {
            return prefixList;
        }
        if (prefixList != null){
            Collections.sort(prefixList, comparator);
        }
        Set<Value> set = new HashSet<>(prefixList);
        prefixList = new ArrayList<>(set);
        return this.prefixList;
    }

    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        Node x = this.get(this.root, prefix, 0);
        getPrefix(x);
        ArrayList<Value> list = (ArrayList<Value>) this.prefixList;
        Set<Value> set = new HashSet<>();
        if (list != null){
            set = new HashSet (list);
        }
        deleteAllWithPrefix(this.root, prefix, 0);
        return set;
    }

    @Override
    public Set<Value> deleteAll(String key) {
        key = key.toUpperCase();
        ArrayList<Value> list = getList(key);
        Set<Value> set = new HashSet<>();
        if (list != null){
            set = new HashSet (list);
        }
        deleteAll(this.root, key, 0);
        return set;
    }

    @Override
    public Value delete(String key, Value val) {
        key = key.toUpperCase();
        ArrayList<Value> list = getList(key);
        if (list != null){
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).equals(val)){
                    this.delete(this.root, key, 0, val);
                    return val;
                }
            }
        }
        return null;
    }

    private Node delete(Node x, String key, int d, Value val){
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            for (int i = 0; i < x.list.size(); i++){
                if (x.list.get(i).equals(val)){
                    x.list.remove(val);
                }
            }
        }
        else {
            char c = key.charAt(d);
            d++;
            x.links[c] = this.delete(x.links[c], key, d, val);
        }
        if (x.list.size() > 0){
            return x;
        }
        for (int c = 0; c < x.links.length; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        return null;
    }

    private Node deleteAll(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            x.list = new ArrayList<Value>();
        }
        else {
            char c = key.charAt(d);
            d++;
            x.links[c] = this.deleteAll(x.links[c], key, d);
        }

        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c < x.links.length; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        return null;
    }

    private Node deleteAllWithPrefix(Node x, String key, int d){
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
           // x.list = new ArrayList<Value>();
            x.links[key.charAt(d-1)] = new Node();
        }
        else {
            char c = key.charAt(d);
            x.links[c] = this.deleteAllWithPrefix(x.links[c], key, d+1);
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c < x.links.length; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        return null;
    }

    protected ArrayList<Value> getList (String string){
        Node x = this.get(this.root, string, 0);
        if (x == null) {
            return null;
        }
        return (ArrayList<Value>) x.list;
    }

    protected Node get(Node x, String key, int d) {
        //link was null - return null, indicating a miss
        if (x == null)
        {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length())
        {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.links[c], key, d + 1);
    }

    protected void getPrefix(Node x){
        if (x == null){
            return;
        }
        if (x.list != null){
            ListIterator<Value> itr = x.list.listIterator();
            while (itr.hasNext()){
                this.prefixList.add(itr.next());
            }
        }
        for (int i = 0; i < x.links.length; i++){
            getPrefix(x.links[i]);
        }
    }
}

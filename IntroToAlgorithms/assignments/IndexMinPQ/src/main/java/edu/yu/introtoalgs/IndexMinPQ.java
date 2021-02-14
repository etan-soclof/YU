package edu.yu.introtoalgs;
import java.util.*;

public class IndexMinPQ <Key extends Comparable <Key>> implements Iterable <Integer> {

    public static void main (String[] args){
        int result = 0;
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < i; j++){
                result++;
            }
        }
        System.out.println(result);
    }


    private int[] pq;
    private int N = 0;
    private int[]qp;
    private Key[] keys;

    public IndexMinPQ(int maxN){
        if (maxN <= 0){
            throw new IllegalArgumentException("invalid size");
        }
        keys = (Key[]) new Comparable[maxN+1];
        pq = new int[maxN+1];
        qp = new int[maxN+1];
        for (int i = 0; i <= maxN; i++){
            qp[i] = -1;
        }
    }

    public boolean isEmpty(){
        return N == 0;
    }

    public boolean contains(int i){
        if (i > pq.length || i < 0) {
            return false;
        }
        return qp[i] != -1;
    }

    public int size(){
        return N;
    }

    public void insert(int k, Key key){
        if (k < 0 || k > pq.length - 1){
            throw new IllegalArgumentException("Invalid Input");
        }
        N++;
        qp[k] = N;
        pq[N] = k;
        keys[k] = key;
        swim(N);
    }

    public int delMin(){
        int indexOfMin = pq[1];
        exch(1, N--);
        sink(1);
        keys[pq[N+1]] = null;
        qp[pq[N+1]] = -1;
        return indexOfMin;
    }

    public int minIndex(){
        return pq[1];
    }

    public Key minKey(){
        return keys[pq[1]];
    }

    public void changeKey(int i, Key key){
        if (i > keys.length - 1 || i < 0){
            throw new IllegalArgumentException("Out of bounds");
        }
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    public void delete(int i){
        if (i > keys.length - 1 || i < 0){
            throw new IllegalArgumentException("Out of bounds");
        }
        int index = qp[i];
        exch(index, N--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    private boolean less(int i, int j){
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j){
        int t = pq[i];
        int s = pq[j];
        pq[i] = pq[j];
        pq[j] = t;

        int r = qp[t];
        qp[t] = qp[s];
        qp[s] = r;
    }

    private void swim(int k){
        while (k > 1 && less(k/2, k)){
            exch(k/2, k);
            k = k/2;
        }
    }

    private void sink(int k){
        while (2*k <= N){
            int j = 2*k;
            if (j < N && less(j, j+1)){
                j++;
            }
            if (!less(k, j)){
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    public Key keyOf(int i){
        if (i > keys.length - 1 || i < 0){
            throw new IllegalArgumentException("Out of bounds");
        }
        return keys[i];
    }

    public Iterator<Integer> iterator() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < N+1; i++){
            list.add(pq[i]);
        }
        list.sort((Integer o1, Integer o2) -> keys[o1].compareTo(keys[o2]));
        return list.iterator();
    }
}

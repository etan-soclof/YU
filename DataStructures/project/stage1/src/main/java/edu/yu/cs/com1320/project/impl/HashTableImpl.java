package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key,Value> implements HashTable<Key, Value> {

    private int num;
    private Object[] table =  new Object[5];

    private class Element {

        public Object k;
        public Object v;
        public Element next;

        public Element(Object key, Object val, Element next)  {
            this.k  = key;
            this.v  = val;
            this.next = next;
        }
    }

    public HashTableImpl (){
        for (int i = 0; i < table.length; i++){
            Element tail = new Element (null, null, null);
            table[i] = new Element (null, null, tail);
        }
    }

    private int hash (Key key){
        int hash = (key.hashCode() & 0x7fffffff) % 5;
        return hash;
    }

    @Override
    public Value get(Key k) {
        int hash = hash(k);
        for (Element e = (Element) table[hash]; e != null; e = e.next){
            if (k.equals(e.k)){
                return (Value) e.v;
            }
        }
        return null;
    }

    @Override
    public Value put(Key k, Value v) {
        int hash = hash(k);
        Element previous = (Element) table [hash];

        if (v == null) {
            for (Element e = previous.next; e != null; e = e.next) {
                if (k.equals(e.k)) {
                    previous.next = e.next;
                    return (Value) e.v;
                }
                previous = e;
            }
            return null;
        }

        for (Element e = (Element) table[hash]; e != null; e = e.next) {
            if (k.equals(e.k)) {
                Value hold = (Value) e.v;
                e.v = v;
                return hold;
            }
            previous = e;
        }
        if (v!= null){
            num++;
            Element head = (Element) table[hash];
            head.next = new Element(k, v, head.next);
        }
        return null;
    }

    private void tableDoubler(){
        Object[] temp = this.table;
        int hash;
        this.table = new Object[this.table.length*2];
        for (int i = 0; i < table.length; i++){
            Element tail = new Element (null, null, null);
            table[i] = new Element (null, null, tail);
        }
        for (int i = 0; i < temp.length; i++){
            for (Element e = (Element) table[i]; e != null; e = e.next){
                hash = hash((Key) e.k);
                e.next = (Element) this.table[hash];
                this.table[hash] = e;
            }
        }
    }
}





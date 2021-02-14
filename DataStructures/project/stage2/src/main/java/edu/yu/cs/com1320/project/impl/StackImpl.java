package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T> {

    private Element tail;
    private Element first;
    private int amount = 0;

    private class Element {

        public Object v;
        public Element next;

        public Element(Object val, Element next) {
            this.v  = val;
            this.next = next;
        }

        public Object getObject (){
            return this.v;
        }
    }

    public StackImpl(){
        this.tail = new Element (null, null);
        this.first = tail;
    }

    @Override
    public void push(T element) {
        Element holder = this.first;
        Element node = new Element (element, holder);
        this.first = node;
        amount++;
    }

    @Override
    public T pop() {
        if (this.peek() == null){
            return null;
        }
        T t = (T) this.first.v;
        this.first = this.first.next;
        amount--;
        return t;
    }

    @Override
    public T peek() {
        return (T) this.first.v;
    }

    @Override
    public int size() {
        return amount;
    }
}

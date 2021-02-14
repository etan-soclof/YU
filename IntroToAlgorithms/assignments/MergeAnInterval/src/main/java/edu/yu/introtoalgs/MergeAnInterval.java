package edu.yu.introtoalgs;

import java.util.*;

public class MergeAnInterval {

/*
  public static void main(String[] args) {
    Interval i5 = new Interval(1, 2);
    Interval i1 = new Interval(3, 4);
    Interval i3 = new Interval(5, 6);
    Interval i4 = new Interval(7, 8);
    Set<Interval> set = new HashSet<>();
    set.add(i1);
    set.add(i3);
    set.add(i5);
    set = merge(set, i4);
    System.out.println("Test 1:");
    for (Interval i: set){
      System.out.println(i.left + " - " + i.right);
    }
    System.out.println();
  }

 */






  /** An immutable class, holds a left and right integer-valued pair that
   * defines a closed interval
   */
  public static class Interval implements Comparable<Interval>{
    /** Constructor
     *
     * @param left the left endpoint of the interval
     * @param right the right endpoint of the interval
     */
    public final int left;
    public final int right;

    public Interval(int l, int r) {
      this.left = l;
      this.right = r;
    }

    public int getLeft() {
      return this.left;
    }

    public int getRight() {
      return this.right;
    }

    @Override
    public int compareTo(Interval o) {
      if (this.left < o.left){
        return -1;
      }
      if (this.left > o.left){
        return 1;
      }
      if (this.left == o.left){
        return 0;
      }
      return -1;
    }

    @Override
    public boolean equals(Object obj){
      if(this == obj){
        return true;
      }
      if(obj == null || obj.getClass()!= this.getClass()){
        return false;
      }
      Interval interval = (Interval) obj;
      return this.left == interval.left && this.right == interval.right;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.left, this.right);
    }

    @Override
    public String toString(){
      return this.left + " - " + this.right;
    }
  } // Interval class

  /** Merges the new interval into an existing set of disjoint intervals.
   *
   * @param intervals the existing set of intervals
   * @param newInterval the interval to be added
   * @return a new set of disjoint intervals containing the original intervals
   * and the new interval, merging the new interval if necessary into existing
   * interval(s), to preseve the "disjointedness" property.
   */
  public static Set<Interval> merge(final Set<Interval> intervals, Interval newInterval){

    //Check for invalid input
    if (intervals == null || newInterval == null){
      throw new IllegalArgumentException("Null input");
    }
    if (newInterval.right < newInterval.left){
      throw new IllegalArgumentException("Invalid Input: left is larger than right");
    }

    //If intervals is size=0
    if (intervals.size() == 0){
      intervals.add(newInterval);
      return intervals;
    }

    intervals.add(newInterval);
    ArrayList<Interval> list = new ArrayList<Interval>(intervals);
    ArrayList<Interval> auxList = new ArrayList<Interval>();
    Collections.sort(list);
    auxList.add(list.get(0));
    if (list.get(0).right < list.get(0).left){
      throw new IllegalArgumentException("Invalid Input: left is larger than right");
    }
    for (int i = 1; i < list.size(); i++) {
      Interval second = list.get(i);
      if (second.right < second.left){
        throw new IllegalArgumentException("Invalid Input: left is larger than right");
      }
      Interval first = auxList.get(auxList.size() - 1);
      if (second.left <= first.right) {
        Interval aux = null;
        if (second.right >= first.right){
          aux = new Interval(first.left, second.right);
        }
        else {
          aux = new Interval(first.left, first.right);
        }
        auxList.remove(auxList.size() - 1);
        auxList.add(aux);
      }
      else{
        auxList.add(second);
      }
    }

    Set<Interval> set = new HashSet<>();
    // Add each element of list into the set
    for (Interval i : auxList)
      set.add(i);

    // return the set
    return set;
  }
}

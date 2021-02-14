package edu.yu.da;

/**
 *
 * @author Avraham Leff
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class MedianFinding {
  /*
  public static void main(String[] args) {
    Account a1 = new Account(1);
    Account a2 = new Account(2);
    Account a3 = new Account(5);
    Account a4 = new Account(7);
    Account a5 = new Account(13);
    Account a6 = new Account(18);
    Account a7 = new Account(19);
    Account a8 = new Account(20);
    Account a9 = new Account(21);
    Account a10 = new Account(22);

    Account[] set1 = {a1, a3, a5, a7};
    Account[] set2 = {a2, a4, a6, a8};
    System.out.println(findMedian(set1, set2).getIncome());
  }
  */

  /** Immutable account class for pedagogic purposes only since it's not very
   * useful.
   */
  public static class Account implements Comparable<Account> {
    /** Initializes the account with an income field.
     */
    public Account(final double accountIncome) {
      income = accountIncome;
    }

    public double getIncome() { return income; }

    public int compareTo(final Account that) {
      if (this.income() > that.income()){
        return 1;
      } 
      if (this.income() < that.income()){
        return -1;
      } 
      return 0;
    }

    private final double income;
  } // static inner class

  /** Finds the median account (with respect to account income) over two sets
   * of accounts.  The two sets are of the same length.
   *
   * @param set1 A sorted, in ascending order, with respect to account
   * income, non-null array of accounts.  If the array is not sorted correctly,
   * the results of the method are undefined.
   * @param set2 A sorted, in ascending order, with respect to account
   * income, non-null array of accounts.  If the array is not sorted correctly,
   * the results of the method are undefined.
   * @return Account record that has the median income with respect to
   * all accounts in the union of set1 and set2.
   */
  public static Account findMedian(final Account[] set1, final Account[] set2) {
    int start1 = 0;
    int start2 = 0;
    int end1 = set1.length - 1;
    int end2 = set1.length - 1;
    return findMedian(start1, start2, end1, end2, set1, set2);
  } // findMedian

  private static Account findMedian(int start1, int start2, int end1, int end2, final Account[] set1, final Account[] set2){

    if (end1 - start1 > 1){
      int median1 = (int) (start1 + Math.ceil((end1 - start1)/2));
      int median2 = (int) (start2 + Math.ceil((end2 - start2)/2));

      if ((end1 - start1)%2 == 0){
        if (set1[median1].getIncome() > set2[median2].getIncome()){
          end1 = median1;
          start2 = median2;
        }
        else {
          end2 = median2;
          start1 = median1;
        }
      }
      else {
        if (set1[median1].getIncome() > set2[median2].getIncome()){
          end1 = median1 + 1;
          start2 = median2;
        }
        else {
          end2 = median2 + 1;
          start1 = median1;
        }
      }
      return findMedian(start1, start2, end1, end2, set1, set2);
    }
    else{
      if (set1[start1].getIncome() > set2[start2].getIncome()){
        return set1[start1];
      }
      else{
        return set2[start2];
      }
    }
  }

  private final static Logger logger = LogManager.getLogger(MedianFinding.class);
}

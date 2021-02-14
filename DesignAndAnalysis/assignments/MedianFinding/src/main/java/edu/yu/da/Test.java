package edu.yu.da;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Test {

    public static void main (String[] args){
        int[] array1 = {1, 22, 24, 25, 26};
        int[] array2 = {2, 13, 17, 21, 45};
        int start1 = 0, start2 = 0;
        int end1 = array1.length -1 , end2 = array1.length -1;
        System.out.println(findMedian(start1, start2, end1, end2, array1, array2));
    }

    public static int findMedian(int start1, int start2, int end1, int end2, int[] array1, int[] array2){
        if (end1 - start1 > 1){
            int median1 = (int) (start1 + Math.ceil((end1 - start1)/2));
            int median2 = (int) (start2 + Math.ceil((end2 - start2)/2));
            if ((end1 - start1)%2 == 0){
                if (array1[median1] > array2[median2]){
                    end1 = median1;
                    start2 = median2;
                }
                else {
                    end2 = median2;
                    start1 = median1;
                }
            }
            else {
                if (array1[median1] > array2[median2]){
                    end1 = median1 + 1;
                    start2 = median2;
                }
                else {
                    end2 = median2 + 1;
                    start1 = median1;
                }
            }
            return findMedian(start1, start2, end1, end2, array1, array2);
        }
        else{
            return Math.max(array1[start1], array2[start2]);
        }
    }
}

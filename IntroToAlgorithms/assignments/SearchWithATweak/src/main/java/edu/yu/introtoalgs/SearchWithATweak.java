package edu.yu.introtoalgs;
import java.util.List;
import java.util.Arrays;
public class SearchWithATweak {

    /*
    public static void main(String[] args) {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 3, 3, 3, 3, 4, 6, 7, 19, 24, 36, 42, 52, 345);
        List<Integer> list2 = Arrays.asList(-5, -4, 0, 2, 4, 6, 7, 89);
        List<Integer> list3 = Arrays.asList(345, 52, 42, 36, 24, 19, 7, 6, 4, 3, 3, 3, 3, -9, -13);
        List<Integer> list4 = Arrays.asList(4, 4, 4, 4, 4, 4, 4);
        List<Integer> list5 = Arrays.asList(89, 79, 40, 30, 32, 20, 19, 18, 16, 9, 8, 4, 0, -1, -45);
        System.out.println(findFirstInstance(list2, 0));
        System.out.println(elementEqualToItsIndex(list5));
    }
     */

    /**
     * Searches the specified sorted list of Integers for
     * the specified key. The list must be sorted prior to
     * making this call: otherwise , the results are
     * undefined. If the list contains multiple elements
     * with the specified value , will return the index of
     * the first instance of that value.
     *
     * @param list the list to be searched: the list is
     *             assumed to be sorted
     * @param key  the value to be searched for
     * @return Index of the search key, if it is contained
     * in the list; otherwise , returns -1.
     */
    public static int findFirstInstance(final List<Integer> list, final int key) {
        int first = 0;
        int last = list.size() - 1;
        int middle;
        boolean increasing = true;
        //check if list is increasing or decreasing
        if (list.get(last) < list.get(first)){
            increasing = false;
        }
        //if it is increasing
        if (increasing){
            while (last - first > 1) {
                middle = (int) ((last + first) / 2);
                if (list.get(middle) == key) {
                    while (list.get(middle - 1) == key) {
                        middle -= 1;
                        if (middle == 0){
                            break;
                        }
                    }
                    return middle;
                } else if (list.get(middle) < key) {
                    first = middle;
                } else if (list.get(middle) > key) {
                    last = middle;
                }
            }
            if (list.get(first) == key) {
                return first;
            }
            if (list.get(last) == key) {
                return last;
            }
        }
        //if it is decreasing
        else {
            while (last - first > 1) {
                middle = (int) ((last + first) / 2);
                if (list.get(middle) == key) {
                    while (list.get(middle - 1) == key) {
                        middle -= 1;
                    }
                    return middle;
                } else if (list.get(middle) > key) {
                    first = middle;
                } else if (list.get(middle) < key) {
                    last = middle;
                }
            }
            if (list.get(first) == key) {
                return first;
            }
            if (list.get(last) == key) {
                return last;
            }
        }

        return -1;
    }

    /**
     * Searched the specified sorted list of distinct
     * Integers and returns an index i with the property
     * that the value of the ith element is itself i.
     *
     * @param list the list to be searched: the list is
     *             assumed to be sorted and contains distinct values
     * @return Index satisfying the specified property if any
     * such elements exist; otherwise , return -1
     */
    public static int elementEqualToItsIndex(final List<Integer> list) {
        int first = 0;
        int last = list.size() - 1;
        int middle;
        boolean increasing = true;
        //check if list is increasing or decreasing
        if (list.get(last) < list.get(first)){
            increasing = false;
        }
        if (increasing){
            while (last - first > 1) {
                middle = (int) ((last + first) / 2);
                if (list.get(middle) == middle) {
                    return middle;
                } else if (list.get(middle) < middle) {
                    first = middle;
                } else if (list.get(middle) > middle) {
                    last = middle;
                }
            }
            if (list.get(first) == first) {
                return first;
            }
            if (list.get(last) == last) {
                return last;
            }
        }
        else {
            while (last - first > 1) {
                middle = (int) ((last + first) / 2);
                if (list.get(middle) == middle) {
                    return middle;
                } else if (list.get(middle) > middle) {
                    first = middle;
                } else if (list.get(middle) < middle) {
                    last = middle;
                }
            }
            if (list.get(first) == first) {
                return first;
            }
            if (list.get(last) == last) {
                return last;
            }
        }

        return -1;
    }
}

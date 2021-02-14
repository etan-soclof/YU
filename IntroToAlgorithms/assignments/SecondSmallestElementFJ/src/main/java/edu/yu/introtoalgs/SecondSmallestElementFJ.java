package edu.yu.introtoalgs;
import java.lang.Math;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class SecondSmallestElementFJ {

	public final int[] array;
	public final double fraction;
	public int threshold = 0;
	public boolean hasTwoUniqueInts = false;
	public int firstInt;

	public class Task extends RecursiveTask<int[]> {

		private int high;
		private int low;

		public Task(int low, int high) {
			this.high = high;
			this.low = low;
		}

		protected int[] compute() {

			//if work is above threshold, break tasks up into smaller tasks
			if((high - low) > threshold) {
				//System.out.println("Splitting workLoad : " + (high - low));

				List<Task> subtasks = new ArrayList<Task>();
				subtasks.addAll(createSubtasks());

				for(Task subtask : subtasks){
					subtask.fork();
				}

				int[] temp = new int[4];
				System.arraycopy(subtasks.get(0).join(), 0, temp, 0, 2);
				for(int i = 1; i < subtasks.size(); i++) {
					System.arraycopy(subtasks.get(i).join(), 0, temp, 2, 2);
					temp = process(temp);
				}
				return temp;

			} else {
				//System.out.println("Doing workLoad myself: " + (high - low));
				return process();
			}
		}

		private List<Task> createSubtasks() {
			List<Task> subtasks =
					new ArrayList<Task>();

			Task subtask1 = new Task( low , ( high + low ) /2);
			Task subtask2 = new Task( ( high + low ) /2 , high);

			subtasks.add(subtask1);
			subtasks.add(subtask2);

			return subtasks;
		}

		private int[] process(){
			int smallest = Integer.MAX_VALUE;
			int secondSmallest = Integer.MAX_VALUE;
			int temp = 0;

			for (int i = low; i < high; i++){
				if (array[i] != firstInt){
					hasTwoUniqueInts = true;
				}
				if (array[i] == smallest){
					continue;
				}
				if (array[i] < secondSmallest){
					secondSmallest = array[i];
					if(secondSmallest < smallest){
						temp = secondSmallest;
						secondSmallest = smallest;
						smallest = temp;
					}
				}
			}
			if (high - low == 1){
				return new int[]{array[low], array[low]};
			}
			int[] result = {smallest, secondSmallest};
			return result;
		}

		private int[] process(int[] arr){
			int smallest = Integer.MAX_VALUE;
			int secondSmallest = Integer.MAX_VALUE;
			int temp = 0;


			for (int i = 0; i < arr.length; i++){
				if (arr[i] == smallest){
					continue;
				}
				if (arr[i] < secondSmallest){
					secondSmallest = arr[i];
					if(secondSmallest < smallest){
						temp = secondSmallest;
						secondSmallest = smallest;
						smallest = temp;
					}
				}
			}
			int[] result = {smallest, secondSmallest};
			return result;
		}
	}
	/** Constructor .
	*
	* @param array input that we ’ll search
	* for the second smallest element ,
	* cannot be null .
	* @param fractionToApplySequentialCutoff
	* a fraction of the original number of
	* array elements : when the remaining
	* elements dip below this fraction , the
	* fork - join algorithm will process using
	* a sequential algorithm . Cannot be
	* less than 0.0 (fork - join processing
	* for all but arrays of size 1) and
	* cannot exceed 1.0 (no fork - join
	* processing will take place at all).
	*/
	public SecondSmallestElementFJ( final int [] array , final double fractionToApplySequentialCutoff ) {
		if (fractionToApplySequentialCutoff > 1 || fractionToApplySequentialCutoff < 0){
			throw new IllegalArgumentException("Invalid Fraction");
		}
		if (array.length < 2){
			throw new IllegalArgumentException("Does not have two elements");
		}
		this.array = array;
		this.firstInt = array[0];
		this.fraction = fractionToApplySequentialCutoff;
		this.threshold = (int) (array.length*fractionToApplySequentialCutoff);
		if (this.threshold == 0){
			this.threshold = 1;
		}
	}
	/** Returns the second smallest
	* unique element of the input array .
	*
	* Example : if array is [1 , 7 , 4 , 3 , 6] ,
	* then secondSmallest ( array ) == 3.
	*
	* Example : if array is [6 , 1 , 4 , 3 , 5 ,
	* 2 , 1] , secondSmallest ( array ) == 2.
	2
	*
	* @return second smallest unique element
	* of the input
	* @throws IllegalArgumentException if
	* the input doesn’t contain a minimum of
	* two unique elements .
	*/
	public int secondSmallest () {
		Task task = new Task(0, this.array.length);
		int [] temp = task.compute();
		if (!this.hasTwoUniqueInts){
			throw new IllegalArgumentException("Does not have two unique ints");
		}
		return temp[1];
	}
/*
	public static void main(String[] args){
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("CPU cores: " + processors);
		int[] array = new int[1000000*2*2*2];
		for (int i = 0; i < array.length; i = i + 1){
			array[i] = (int) (Math.random()*10000);
		}
		int count = 0;
		while( count < 100){
			SecondSmallestElementFJ task = new SecondSmallestElementFJ(array, 0);
			long start = System.nanoTime();
			task.secondSmallest();
			long end = System.nanoTime();
			System.out.println(end - start);
			count++;
		}



	}

 */
}
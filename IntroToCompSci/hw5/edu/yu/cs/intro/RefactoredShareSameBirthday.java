package edu.yu.cs.intro;

public class RefactoredShareSameBirthday {

    /** Runs "birthday problem" scenarios for hard-coded
     * population sizes and hard-coded number of
     * experiments.  Prints the observed "shared
     * birthday" frequency for each of the specified
     * population sizes.
     */
    public static void main (String[] args){

    	System.out.printf("Number of experiments per population size: 10,000%nPopulation size: %d. Shared Birthday Frequency: %f%n", 10, runExperiments(10, 10000));
		System.out.printf("Population size: %d. Shared Birthday Frequency: %f%n", 23, runExperiments(23, 10000));
		System.out.printf("Population size: %d. Shared Birthday Frequency: %f%n", 70, runExperiments(70, 10000));

    }

    /** Runs a single experiment for the specified
     * population size.
     *
     * @param populationSize the number of people whose
     * birthdays we'll check to determine if any two
     * share a birthday.
     * @return true iff we observe a shared shared
     * birthday in that population, false otherwise.
     */
    public static boolean runSingleExperiment(int populationSize) throws IllegalArgumentException {

    	if (populationSize <= 1){
    		throw new IllegalArgumentException ("Population size must be greater than 1");
    	}

    	boolean[] birthdays = new boolean[365];
    	int index =0;

    	for (int i =0; i < populationSize; i++){
				

				index = (int)(Math.random() * 365);
				index = index < 365 ? index : 364;

				if(birthdays[index] == false){
					birthdays[index] = true;
				}
				else {
					return true;
				}
			}

        return false;
    }

    /** Runs a simulation for the specified population
     * size, returns the simulated frequency of observed
     * shared birthdays in that population relative to
     * the total number of experiments.
     *
     * @param populationSize the number of people whose
     * birthdays we'll check to determine if any two
     * share a birthday, must be greater than 1.
     * @param nExperiments number of experiments to run,
     * must be greater than 0.
     * @return Frequency of observed shared birthday in
     * that population.
     */
    public static double runExperiments (int populationSize, int nExperiments) throws IllegalArgumentException {
        
    	if (populationSize <= 1){
    		throw new IllegalArgumentException ("Population size must be greater than 1");
    	}

    	if (nExperiments <= 0){
    		throw new IllegalArgumentException ("Number of experiments must be greater than 0");
    	}

    	int trials = 0;
    	int count = 0;

        while (trials <= nExperiments){

        	if (runSingleExperiment(populationSize)) {
        		count++;
        	}

			trials++;
		}

        return (double)count/(double)nExperiments;
    }
}
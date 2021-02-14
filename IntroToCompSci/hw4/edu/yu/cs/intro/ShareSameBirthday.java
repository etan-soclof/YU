package edu.yu.cs.intro;
public class ShareSameBirthday {

	public static void main (String[] args){
		
		boolean[] birthdays = new boolean[365];

		int count10 = 0;
		int count23 = 0;
		int count70 = 0;
		int trials = 0;
		int index = 0;

		while (trials <=10000){

			//set all to false
			for (int z = 0; z < 365; z++){
					birthdays[z] = false;
				}

			//test for 10
			for (int i =0; i < 10; i++){
				

				index = (int)(Math.random() * 365);
				index = index < 365 ? index : 364;

				if(birthdays[index] == false){
					birthdays[index] = true;
				}
				else {
					count10++;
					break;
				}
			}

			//set all to false
			for (int z = 0; z < 365; z++){
					birthdays[z] = false;
				}

			//test for 23
			for (int i =0; i < 23; i++){
				

				index = (int)(Math.random() * 365);
				index = index < 365 ? index : 364;

				if(birthdays[index] == false){
					birthdays[index] = true;
				}
				else {
					count23++;
					break;
				}
			}

			//set all to false
			for (int z = 0; z < 365; z++){
					birthdays[z] = false;
				}

			//test for 70
			for (int i = 0; i < 70; i++){
				

				index = (int)(Math.random() * 365);
				index = index < 365 ? index : 364;

				if(birthdays[index] == false){
					birthdays[index] = true;
				}
				else {
					count70++;
					break;
				}
			}
			
			trials++;

		}

	System.out.printf("Number of experiments per population size: 10,000%nPopulation size: %d. Shared Birthday Frequency: %f%n", 10, (double)(count10)/(double)(10000));
	System.out.printf("Population size: %d. Shared Birthday Frequency: %f%n", 23, (double)(count23)/(double)(10000));
	System.out.printf("Population size: %d. Shared Birthday Frequency: %f%n", 70, (double)(count70)/(double)(10000));	
	}
}
package edu.yu.introtoalgs;


public class EstimateSecretAlgorithmsClient {

    public static void main (String[] args){

        BigOMeasurable alg1 = new SecretAlgorithm1();
        BigOMeasurable alg2 = new SecretAlgorithm2();
        BigOMeasurable alg3 = new SecretAlgorithm3();
        BigOMeasurable alg4 = new SecretAlgorithm4();
        long startTime;
        long endTime;
        int input = 500;

        System.out.println("Alg2");
        while (true){
            alg2.setup(input);
            startTime = System.nanoTime();
            alg2.execute();
            endTime = System.nanoTime();
            System.out.println(endTime - startTime);
            input*=2;
            if (input > 65536001){
                input = 500;
                break;
            }
        }
        
        System.out.println("Alg4");
        while (true){
            alg4.setup(input);
            startTime = System.nanoTime();
            alg4.execute();
            endTime = System.nanoTime();
            System.out.println(endTime - startTime);
            input*=2;
            if (input > 65536001){
                input = 500;
                break;
            }
        }
        System.out.println("Alg3");
        while (true){
            alg3.setup(input);
            startTime = System.nanoTime();
            alg3.execute();
            endTime = System.nanoTime();
            System.out.println(endTime - startTime);
            input*=2;
            if (input > 819200){
                input = 500;
                break;
            }
        }


        System.out.println("Alg1");
        while (true){
            alg1.setup(input);
            startTime = System.nanoTime();
            alg1.execute();
            endTime = System.nanoTime();
            System.out.println(endTime - startTime);
            input*=2;
            if (input > 64001){
                input = 500;
                break;
            }
        }

    }



}

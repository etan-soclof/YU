package edu.yu.introtoalgs;

public class Square {

    public static void main (String[] args){
        int[][] matrix = {  {1, 1, 1, 1, 0, 0, 1, 1},
                            {1, 0, 1, 1, 0, 0, 1, 1},
                            {1, 1, 1, 1, 0, 0, 1, 1},
                            {1, 1, 1, 0, 1, 1, 1, 1},
                            {1, 0, 1, 1, 1, 1, 0, 1},
                            {1, 1, 1, 0, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 0},
                            {0, 1, 1, 1, 1, 0, 1, 0},
                        };
        boolean check = true;
        int max = 0;

        for (int i = 1; i <= matrix.length; i++){
            for (int j = 0; j <= matrix.length - i; j++) {
                for (int c = 0; c <= matrix.length - i; c++) {
                    for (int x = j; x < j + i; x++) {
                        for (int y = c; y < c + i; y++) {
                            if (matrix[x][y] == 1) {
                                check = true;
                            } else {
                                check = false;
                                break;
                            }
                        }
                        if (!check) {
                            break;
                        }
                    }
                    if (check) {
                        max = i;
                        break;
                    }
                }
            }
        }
        System.out.println(max);
    }
}

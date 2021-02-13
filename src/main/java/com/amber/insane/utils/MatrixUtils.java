package com.amber.insane.utils;

public class MatrixUtils {
    public static long[][] createBackpackMatrix(int rows, int columns) {
        long[][] matrix = new long[rows][columns];


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = -1;
            }
        }

        return matrix;
    }

    public static void printMatrix(long[][] matrix) {
        for (long[] aMatrix : matrix) {
            for (long anAMatrix : aMatrix) {
                System.out.printf("%6d", anAMatrix);
            }
            System.out.println();
        }

    }
}

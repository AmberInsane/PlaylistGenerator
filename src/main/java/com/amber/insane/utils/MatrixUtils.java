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

    private static String getArrayPrint(long[] array) {
        StringBuilder sb= new StringBuilder();

        for (long arrayOfDuration : array) {
            sb.append(String.format("%6d", arrayOfDuration));
        }

        return sb.toString();
    }

    private static String getMatrixPrint(long[][] matrix) {
        StringBuilder sb= new StringBuilder();

        for (long[] aMatrix : matrix) {
            for (long anAMatrix : aMatrix) {
                sb.append(String.format("%6d", anAMatrix));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getTablePrint(long[] columnNames, long[][] matrix) {
        return getArrayPrint(columnNames) + "\n" + getMatrixPrint(matrix);
    }
}

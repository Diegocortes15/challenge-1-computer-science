package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static int[][] mountainMap = {{3, 8, 7, 4}, {4, 7, 6, 3}, {2, 9, 5, 2}, {-1, 5, 1, 2}};
    static boolean[][] initPointMap = new boolean[4][4];
    static boolean[][] endPointMap = new boolean[4][4];

    static boolean isInMap(int[] posF) {
        return posF[0] >= 0 && posF[0] < mountainMap.length && posF[1] >= 0 && posF[1] < mountainMap.length;
    }

    static void revealEndPointMap() {
        for (int x = 0; x < mountainMap.length; x++) {
            for (int y = 0; y < mountainMap.length; y++) {

                // West
                if (isInMap(new int[]{x, y + 1})) {
                    if (mountainMap[x][y] < mountainMap[x][y + 1]) continue;
                }

                // South
                if (isInMap(new int[]{x + 1, y})) {
                    if (mountainMap[x][y] < mountainMap[x + 1][y]) continue;
                }

                // East
                if (isInMap(new int[]{x, y - 1})) {
                    if (mountainMap[x][y] < mountainMap[x][y - 1]) continue;
                }

                // North
                if (isInMap(new int[]{x - 1, y})) {
                    if (mountainMap[x][y] < mountainMap[x - 1][y]) continue;
                }
                endPointMap[x][y] = true;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Challenge 1 Endava");
        revealEndPointMap();
        System.out.println(Arrays.deepToString(endPointMap).replace("],", "\n").replace(",", " |").replaceAll("[\\[\\]]", " "));
    }
}

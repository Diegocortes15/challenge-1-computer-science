package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static int[][] mountainMap = {{3, 8, 7, 4}, {4, 7, 6, 3}, {2, 9, 5, 2}, {-1, 5, 1, 2}};
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
                findPaths(String.valueOf(mountainMap[x][y]), new int[]{x, y});
            }
        }
    }

    static boolean hasSmallerNeighbor(int[] currentPos) {

        // West
        if (isInMap(new int[]{currentPos[0], currentPos[1] + 1})) {
            return mountainMap[currentPos[0]][currentPos[1]] > mountainMap[currentPos[0]][currentPos[1] + 1];
        }

        // South
        if (isInMap(new int[]{currentPos[0] + 1, currentPos[1]})) {
            return mountainMap[currentPos[0]][currentPos[1]] > mountainMap[currentPos[0] + 1][currentPos[1]];
        }

        // East
        if (isInMap(new int[]{currentPos[0], currentPos[1] - 1})) {
            return mountainMap[currentPos[0]][currentPos[1]] > mountainMap[currentPos[0]][currentPos[1] - 1];
        }

        // North
        if (isInMap(new int[]{currentPos[0] - 1, currentPos[1]})) {
            return mountainMap[currentPos[0]][currentPos[1]] > mountainMap[currentPos[0] - 1][currentPos[1]];
        }

        return false;
    }

    static boolean isAllowed(int[] initPos, int[] finalPos) {
        return mountainMap[finalPos[0]][finalPos[1]] < mountainMap[initPos[0]][initPos[1]] && mountainMap[finalPos[0]][finalPos[1]] != -1;
    }

    static ArrayList<String> foundPaths = new ArrayList<>();

    static void findPaths(String path, int[] pos) {

        if (!hasSmallerNeighbor(pos)) {
            System.out.println("I'm here: " + mountainMap[pos[0]][pos[1]] + "(" + pos[0] + "," + pos[1] + ")");
//            return String.valueOf(mountainMap[pos[0]][pos[1]]);
            foundPaths.add(path);
        }

        // West
        if (isInMap(new int[]{pos[0], pos[1] + 1})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0], pos[1] + 1})) {
                findPaths(path + "-" + mountainMap[pos[0]][pos[1] + 1], new int[]{pos[0], pos[1] + 1});
            }
        }

        // South
        if (isInMap(new int[]{pos[0] + 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] + 1, pos[1]})) {
                findPaths(path + "-" + mountainMap[pos[0] + 1][pos[1]], new int[]{pos[0] + 1, pos[1]});
            }
        }

        // East
        if (isInMap(new int[]{pos[0], pos[1] - 1})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0], pos[1] - 1})) {
                findPaths(path + "-" + mountainMap[pos[0]][pos[1] - 1], new int[]{pos[0], pos[1] - 1});
            }
        }

        // North
        if (isInMap(new int[]{pos[0] - 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] - 1, pos[1]})) {
                findPaths(path + "-" + mountainMap[pos[0] - 1][pos[1]], new int[]{pos[0] - 1, pos[1]});
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Challenge 1 Endava");

        revealEndPointMap();
        System.out.println(foundPaths);
    }
}

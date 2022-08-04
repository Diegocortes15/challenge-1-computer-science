package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static int xBoundary;
    static int yBoundary;
    static int[][] mountainMap = new int[xBoundary][yBoundary];
    static boolean[][] endPointMap;

    static boolean isInMap(int[] posF) {
        return posF[0] >= 0 && posF[0] < mountainMap.length && posF[1] >= 0 && posF[1] < mountainMap.length;
    }

    //5x5
/*    static int[][] mountainMap = {
            {-1, 10, 5, 6, 4},
            {3, 8, 7, 4, -1},
            {4, 7, 6, 3, 4},
            {2, 9, 5, 2, 5},
            {-1, 5, 1, 2, 3}
    };*/

    //4x4
/*
    static int[][] mountainMap = {
            {3, 8, 7, 4},
            {4, 7, 6, 3},
            {2, 9, 5, 2},
            {-1, 5, 1, 2}
    };*/

    //3x3
    /*static int[][] mountainMap = {
            {4, 7, 6},
            {2, 9, 5},
            {-1, 5, 1}
    };*/

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
                findPaths(mountainMap[x][y], new int[]{x, y}, String.valueOf(mountainMap[x][y]));
            }
        }
    }

    static boolean isAllowed(int[] initPos, int[] finalPos) {
        return mountainMap[finalPos[0]][finalPos[1]] < mountainMap[initPos[0]][initPos[1]] && mountainMap[finalPos[0]][finalPos[1]] != -1;
    }

    static boolean hasSmallerNeighbor(int[] currentPos) {

        // West
        if (isInMap(new int[]{currentPos[0], currentPos[1] + 1})) {
            if (isAllowed(new int[]{currentPos[0], currentPos[1]}, new int[]{currentPos[0], currentPos[1] + 1})) {
                return true;
            }
        }

        // South
        if (isInMap(new int[]{currentPos[0] + 1, currentPos[1]})) {
            if (isAllowed(new int[]{currentPos[0], currentPos[1]}, new int[]{currentPos[0] + 1, currentPos[1]})) {
                return true;
            }
        }

        // East
        if (isInMap(new int[]{currentPos[0], currentPos[1] - 1})) {
            if (isAllowed(new int[]{currentPos[0], currentPos[1]}, new int[]{currentPos[0], currentPos[1] - 1})) {
                return true;
            }
        }

        // North
        if (isInMap(new int[]{currentPos[0] - 1, currentPos[1]})) {
            if (isAllowed(new int[]{currentPos[0], currentPos[1]}, new int[]{currentPos[0] - 1, currentPos[1]})) {
                return true;
            }
        }
        return false;
    }


    static ArrayList<String> foundPaths = new ArrayList<>();
    static String bestPath;
    static int maxSteps = 0;
    static int deltaHeight = 0;

    static void findPaths(int pathHead, int[] pos, String path) {

        if (!hasSmallerNeighbor(pos)) {
            foundPaths.add(path);
            int lastPosition = mountainMap[pos[0]][pos[1]];
            int steps = path.replaceAll("[^-]", "").length();
            if (steps > maxSteps) {
                maxSteps = steps;
                bestPath = path;
                deltaHeight = pathHead - lastPosition;
            } else if (steps == maxSteps) {
                if ((pathHead - lastPosition) > deltaHeight) {
                    bestPath = path;
                    deltaHeight = pathHead - lastPosition;
                }
            }
            return;
        }

        // West
        if (isInMap(new int[]{pos[0], pos[1] + 1})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0], pos[1] + 1})) {
                findPaths(pathHead, new int[]{pos[0], pos[1] + 1}, path + "-" + mountainMap[pos[0]][pos[1] + 1]);
            }
        }

        // South
        if (isInMap(new int[]{pos[0] + 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] + 1, pos[1]})) {
                findPaths(pathHead, new int[]{pos[0] + 1, pos[1]}, path + "-" + mountainMap[pos[0] + 1][pos[1]]);
            }
        }

        // East
        if (isInMap(new int[]{pos[0], pos[1] - 1})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0], pos[1] - 1})) {
                findPaths(pathHead, new int[]{pos[0], pos[1] - 1}, path + "-" + mountainMap[pos[0]][pos[1] - 1]);
            }
        }

        // North
        if (isInMap(new int[]{pos[0] - 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] - 1, pos[1]})) {
                findPaths(pathHead, new int[]{pos[0] - 1, pos[1]}, path + "-" + mountainMap[pos[0] - 1][pos[1]]);
            }
        }
    }

    static Scanner readMapTxt(String txtFile) throws FileNotFoundException {
        // pass the path to the file as a parameter
        File file = new File(txtFile);
        return new Scanner(file);
    }

    static void scanMap(String txtFile) throws FileNotFoundException {

        Scanner sc = readMapTxt(txtFile);
        int[] mapBoundaries = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        xBoundary = mapBoundaries[0];
        yBoundary = mapBoundaries[1];

        System.out.println(Arrays.deepToString(mountainMap));

        int[][] mapValues = new int[mapBoundaries[0]][mapBoundaries[1]];

        System.out.println(xBoundary);
        System.out.println(yBoundary);

        for (int j = 0; j < yBoundary; j++) {
            mapValues[j] = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        mountainMap = mapValues;
        endPointMap = new boolean[xBoundary][yBoundary];

    }

    public static void main(String[] args) throws FileNotFoundException {

        scanMap("testMap.txt");
        System.out.println((Arrays.deepToString(mountainMap)));


        System.out.println("Challenge 1 Endava");
        revealEndPointMap();
        System.out.println("Traveled length: " + maxSteps);
        System.out.println("Traveled altitude: " + deltaHeight);
        System.out.println("Best path found: " + bestPath);
        System.out.println("Number of Paths found: " + foundPaths.size());
        System.out.println("Best paths found: " + "\n\t" + String.join("\n\t", foundPaths));
    }
}

package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DroneMapAnalysis {
    private String map;
    private int xBoundary;
    private int yBoundary;
    private int[][] mountainMap;
    private boolean[][] endPointMap;

    private int maxSteps = 0;
    private int deltaHeight = 0;
    private String bestPath;
    private final ArrayList<String> foundPaths = new ArrayList<>();
    private final Integer numberPathsFound;

    public DroneMapAnalysis(String fileMap) throws FileNotFoundException {
        this.map = fileMap;
        scanFileMap();
        revealEndPointMap();
        findPathsMap();
        this.numberPathsFound = foundPaths.size();
    }

    public DroneMapAnalysis(int[][] map) throws FileNotFoundException {
        this.mountainMap = map;
        scanMap();
        revealEndPointMap();
        findPathsMap();
        this.numberPathsFound = foundPaths.size();
    }

    private Scanner readMapTxt() throws FileNotFoundException {
        File file = new File(this.map);
        return new Scanner(file);
    }

    private void scanFileMap() throws FileNotFoundException {

        Scanner sc = readMapTxt();
        int[] mapBoundaries = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        this.xBoundary = mapBoundaries[0];
        this.yBoundary = mapBoundaries[1];

        this.mountainMap = new int[xBoundary][yBoundary];
        for (int j = 0; j < yBoundary; j++) {
            mountainMap[j] = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        this.endPointMap = new boolean[xBoundary][yBoundary];
    }

    private void scanMap() {

        this.xBoundary = mountainMap.length;
        this.yBoundary = mountainMap[0].length;
        this.endPointMap = new boolean[xBoundary][yBoundary];
    }

    private boolean isInMap(int[] position) {
        return position[0] >= 0 && position[0] < xBoundary && position[1] >= 0 && position[1] < yBoundary;
    }

    private boolean isAllowed(int[] initPos, int[] finalPos) {
        return mountainMap[finalPos[0]][finalPos[1]] < mountainMap[initPos[0]][initPos[1]] && mountainMap[finalPos[0]][finalPos[1]] != -1;
    }

    private boolean hasSmallerNeighbor(int[] currentPos) {

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

    private void revealEndPointMap() {

        for (int i = 0; i < xBoundary; i++) {
            for (int j = 0; j < yBoundary; j++) {

                // West
                if (isInMap(new int[]{i, j + 1})) {
                    if (mountainMap[i][j] < mountainMap[i][j + 1]) continue;
                }

                // South
                if (isInMap(new int[]{i + 1, j})) {
                    if (mountainMap[i][j] < mountainMap[i + 1][j]) continue;
                }

                // East
                if (isInMap(new int[]{i, j - 1})) {
                    if (mountainMap[i][j] < mountainMap[i][j - 1]) continue;
                }

                // North
                if (isInMap(new int[]{i - 1, j})) {
                    if (mountainMap[i][j] < mountainMap[i - 1][j]) continue;
                }
                endPointMap[i][j] = true;
            }
        }
    }

    private void findPath(int pathHead, int[] pos, String path) {

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
                findPath(pathHead, new int[]{pos[0], pos[1] + 1}, path + "-" + mountainMap[pos[0]][pos[1] + 1]);
            }
        }

        // South
        if (isInMap(new int[]{pos[0] + 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] + 1, pos[1]})) {
                findPath(pathHead, new int[]{pos[0] + 1, pos[1]}, path + "-" + mountainMap[pos[0] + 1][pos[1]]);
            }
        }

        // East
        if (isInMap(new int[]{pos[0], pos[1] - 1})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0], pos[1] - 1})) {
                findPath(pathHead, new int[]{pos[0], pos[1] - 1}, path + "-" + mountainMap[pos[0]][pos[1] - 1]);
            }
        }

        // North
        if (isInMap(new int[]{pos[0] - 1, pos[1]})) {
            if (isAllowed(new int[]{pos[0], pos[1]}, new int[]{pos[0] - 1, pos[1]})) {
                findPath(pathHead, new int[]{pos[0] - 1, pos[1]}, path + "-" + mountainMap[pos[0] - 1][pos[1]]);
            }
        }
    }

    private void findPathsMap() {
        for (int i = 0; i < xBoundary; i++) {
            for (int j = 0; j < yBoundary; j++) {
                if (!endPointMap[i][j]) continue;
                findPath(mountainMap[i][j], new int[]{i, j}, String.valueOf(mountainMap[i][j]));
            }
        }
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getDeltaHeight() {
        return deltaHeight;
    }

    public String getBestPath() {
        return bestPath;
    }

    public String getFoundPaths() {
        return "Best paths found: " + "\n\t" + String.join("\n\t", foundPaths);
    }

    public Integer getNumberPathsFound() {
        return numberPathsFound;
    }

    public String printSummary() {
        return "Drone Map Analysis" + "\n" +
                "Traveled length: " + maxSteps + "\n" +
                "Traveled altitude: " + deltaHeight + "\n" +
                "Best path found: " + bestPath + "\n" +
                "Number of Paths found: " + numberPathsFound + "\n";
    }

    public void printAllData() throws FileNotFoundException {
        String data = "Drone Map Analysis" + "\n" +
                "Traveled length: " + maxSteps + "\n" +
                "Traveled altitude: " + deltaHeight + "\n" +
                "Best path found: " + bestPath + "\n" +
                "Number of Paths found: " + numberPathsFound + "\n" +
                "Best paths found: " + "\n\t" + String.join("\n\t", foundPaths);

        PrintWriter out = new PrintWriter("output.txt");
        out.println(data);
        out.close();

        System.out.println("\n\t" + "output.txt has been created" + "\n\t");
    }
}

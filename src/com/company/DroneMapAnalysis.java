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

    public DroneMapAnalysis(int[][] map) {
        this.mountainMap = map;
        scanMap();
        revealEndPointMap();
        findPathsMap();
        this.numberPathsFound = foundPaths.size();
    }


    /**
     * Scanner readMapTxt()
     * Read file.txt
     * @return Scanner object
     * */
    private Scanner readMapTxt() throws FileNotFoundException {
        File file = new File(this.map);
        return new Scanner(file);
    }

    /**
     * scanFileMap
     * 1. Use scanner object to save height and width
     * 2. Set height and width in mountainMap array
     * 3. Set height and width in endPointMap
     * 4. Use scanner object to save data in the file in mountainMap array.
     * */
    private void scanFileMap() throws FileNotFoundException {

        Scanner sc = readMapTxt();
        int[] mapBoundaries = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        this.xBoundary = mapBoundaries[0];
        this.yBoundary = mapBoundaries[1];

        this.mountainMap = new int[xBoundary][yBoundary];
        this.endPointMap = new boolean[xBoundary][yBoundary];

        for (int j = 0; j < yBoundary; j++) {
            mountainMap[j] = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }
    }

    /**
     * scanMap
     * 1. Save height and width
     * 2. Set height and width in endPointMap
     * */
    private void scanMap() {
        this.xBoundary = mountainMap.length;
        this.yBoundary = mountainMap[0].length;
        this.endPointMap = new boolean[xBoundary][yBoundary];
    }

    /**
     * isInMap
     * @param position the point in the map to check it.
     * Check if the position is inside the map
     * @return True or False
     * */
    private boolean isInMap(int[] position) {
        return position[0] >= 0 && position[0] < xBoundary && position[1] >= 0 && position[1] < yBoundary;
    }

    /**
     * isAllowed
     * @param currentPosition current position
     * @param nextPosition position I want to go
     * @return True or False if nextPosition is available to go or not
     * */
    private boolean isAllowed(int[] currentPosition, int[] nextPosition) {
        return mountainMap[nextPosition[0]][nextPosition[1]] < mountainMap[currentPosition[0]][currentPosition[1]] && mountainMap[nextPosition[0]][nextPosition[1]] != -1;
    }

    /**
     * isInMap
     * @param currentPos current position
     * @return True or False if currentPos has neighbors or not
     * */
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

    /**
     * revealEndPointMap
     * Find the peak points in the map.
     * If currentPosition doesn't have neighbours smaller than him, then it is a peak of path.
     * */
    private void revealEndPointMap() {

        for (int i = 0; i < xBoundary * yBoundary; i++) {
            int row = i / xBoundary;
            int column = i % yBoundary;

            // Check if WEST neighbour is bigger than current position
            if (isInMap(new int[]{row, column + 1})) {
                if (mountainMap[row][column] < mountainMap[row][column + 1]) continue;
            }

            // Check if SOUTH neighbour is bigger than current position
            if (isInMap(new int[]{row + 1, column})) {
                if (mountainMap[row][column] < mountainMap[row + 1][column]) continue;
            }

            // Check if EAST neighbour is bigger than current position
            if (isInMap(new int[]{row, column - 1})) {
                if (mountainMap[row][column] < mountainMap[row][column - 1]) continue;
            }

            // Check if NORTH neighbour is bigger than current position
            if (isInMap(new int[]{row - 1, column})) {
                if (mountainMap[row][column] < mountainMap[row - 1][column]) continue;
            }

            // Set current position as peak of path.
            endPointMap[row][column] = true;
        }
    }

    /**
     * findPath
     * @param pathHead peak of path.
     * @param currentPosition current position.
     * @param path it will store path found.
     * */
    private void findPath(int pathHead, int[] currentPosition, String path) {

        // Base condition
        // If currentPosition doesn't have smaller neighbours, then add path.
        if (!hasSmallerNeighbor(currentPosition)) {
            foundPaths.add(path);
            int lastPosition = mountainMap[currentPosition[0]][currentPosition[1]];
            int steps = path.replaceAll("[^-]", "").length();

            // Check if a larger was found.
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

        // Looking for a route to the WEST
        if (isInMap(new int[]{currentPosition[0], currentPosition[1] + 1})) {
            if (isAllowed(new int[]{currentPosition[0], currentPosition[1]}, new int[]{currentPosition[0], currentPosition[1] + 1})) {
                findPath(pathHead, new int[]{currentPosition[0], currentPosition[1] + 1}, path + "-" + mountainMap[currentPosition[0]][currentPosition[1] + 1]);
            }
        }

        // Looking for a route to the SOUTH
        if (isInMap(new int[]{currentPosition[0] + 1, currentPosition[1]})) {
            if (isAllowed(new int[]{currentPosition[0], currentPosition[1]}, new int[]{currentPosition[0] + 1, currentPosition[1]})) {
                findPath(pathHead, new int[]{currentPosition[0] + 1, currentPosition[1]}, path + "-" + mountainMap[currentPosition[0] + 1][currentPosition[1]]);
            }
        }

        // Looking for a route to the EAST
        if (isInMap(new int[]{currentPosition[0], currentPosition[1] - 1})) {
            if (isAllowed(new int[]{currentPosition[0], currentPosition[1]}, new int[]{currentPosition[0], currentPosition[1] - 1})) {
                findPath(pathHead, new int[]{currentPosition[0], currentPosition[1] - 1}, path + "-" + mountainMap[currentPosition[0]][currentPosition[1] - 1]);
            }
        }

        // Looking for a route to the NORTH
        if (isInMap(new int[]{currentPosition[0] - 1, currentPosition[1]})) {
            if (isAllowed(new int[]{currentPosition[0], currentPosition[1]}, new int[]{currentPosition[0] - 1, currentPosition[1]})) {
                findPath(pathHead, new int[]{currentPosition[0] - 1, currentPosition[1]}, path + "-" + mountainMap[currentPosition[0] - 1][currentPosition[1]]);
            }
        }
    }

    /**
     * findPathsMap
     * This method will take route peaks and
     * will start to search paths from them.
     * */
    private void findPathsMap() {
        for (int i = 0; i < xBoundary * yBoundary; i++) {
            int row = i / xBoundary;
            int column = i % yBoundary;
            if (!endPointMap[row][column]) continue;
            findPath(mountainMap[row][column], new int[]{row, column}, String.valueOf(mountainMap[row][column]));
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

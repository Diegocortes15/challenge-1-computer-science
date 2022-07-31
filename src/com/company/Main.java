package com.company;

import java.util.Arrays;

public class Main {

    static int[][] mountainMap = {{3, 8, 7, 4}, {4, 7, 6, 3}, {2, 9, 5, 2}, {-1, 5, 1, 2}};
    static int[] dronePosition = {0, 0};

    static void moveWest() {
        dronePosition[1] = dronePosition[1] - 1;
        System.out.println("Drone has moved West");
        System.out.println(Arrays.toString(dronePosition) + ": " + mountainMap[dronePosition[0]][dronePosition[1]]);
    }

    static void moveNorth() {
        dronePosition[0] = dronePosition[0] - 1;
        System.out.println("Drone has moved North");
        System.out.println(Arrays.toString(dronePosition) + ": " + mountainMap[dronePosition[0]][dronePosition[1]]);
    }

    static void moveEast() {
        dronePosition[1] = dronePosition[1] + 1;
        System.out.println("Drone has moved East");
        System.out.println(Arrays.toString(dronePosition) + ": " + mountainMap[dronePosition[0]][dronePosition[1]]);
    }

    static void moveSouth() {
        dronePosition[0] = dronePosition[0] + 1;
        System.out.println("Drone has moved South");
        System.out.println(Arrays.toString(dronePosition) + ": " + mountainMap[dronePosition[0]][dronePosition[1]]);
    }

    public static void main(String[] args) {
        System.out.println("Challenge 1 Endava");

        System.out.println(Arrays.deepToString(mountainMap)
                .replace("],", "\n").replace(",", " |").replaceAll("[\\[\\]]", " "));

        moveEast();
        moveSouth();
        moveWest();
        moveNorth();
    }
}

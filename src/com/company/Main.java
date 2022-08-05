package com.company;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        int[][] mountainMap = {
                {-1, 10, 5, 6, 4},
                {3, 8, 7, 4, -1},
                {4, 7, 6, 3, 4},
                {2, 9, 5, 2, 5},
                {-1, 5, 1, 2, 3}
        };

        DroneMapAnalysis droneMapAnalysis = new DroneMapAnalysis("map.txt");
        droneMapAnalysis.printAllData();

        DroneMapAnalysis droneMapAnalysis2 = new DroneMapAnalysis(mountainMap);
        System.out.println(droneMapAnalysis2.printSummary());

    }
}

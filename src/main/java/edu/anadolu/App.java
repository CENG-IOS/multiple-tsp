package edu.anadolu;

import javax.swing.plaf.IconUIResource;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    static final int DEPOT_NUMBERS = 2;
    static final int ROUTE_NUMBERS = 5;

    public static void main(String[] args) {

        Map<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions = new HashMap<>();//?????????

        /*Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }


        mTSP best = null;
        int minCost = Integer.MAX_VALUE;


        for (int i = 0; i < 100_000; i++) {

            mTSP mTSP = new mTSP(params.getNumDepots(), params.getNumSalesmen());

            mTSP.randomSolution();
            mTSP.validate();
            // mTSP.print(false);

            final int cost = mTSP.cost();

            // System.out.println("Total cost is " + cost);

            if (cost < minCost) {
                best = mTSP;
                minCost = cost;
            }
        }


        if (best != null) {
            best.print(params.getVerbose());
            System.out.println("**Total cost is " + best.cost());
        }*/

        int tryCounter = 0;
        while (tryCounter != 100000) {
            int multiply = ROUTE_NUMBERS * DEPOT_NUMBERS;
            int sum = 0;
            LinkedHashMap<Integer, ArrayList<Integer>[]> map = new LinkedHashMap<>();

            LinkedList<Integer> cities = new LinkedList<>();
            for (int i = 0; i < 81; i++) {
                cities.add(i);
            }
            Collections.shuffle(cities);

            /** Depo şehirleri seçimi */
            for (int i = 0; i < DEPOT_NUMBERS; i++) {
                ArrayList<Integer>[] arrayLists = new ArrayList[ROUTE_NUMBERS];
                int rnd_depot = cities.remove(0);
                map.put(rnd_depot, arrayLists);
                sum += 1;
            }

            /** Route şehirleri seçimi */
            int counter = 0;
            int routCounter = 0;
            for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
                for (int j = 0; j < ROUTE_NUMBERS; j++) {
                    ArrayList<Integer> route_cities = new ArrayList<>();
                    int route_border;
                    if (counter == DEPOT_NUMBERS * ROUTE_NUMBERS - 1) {
                        route_border = 81 - sum;
                    } else {
                        route_border = (int) (Math.random() * (cities.size() - (multiply - 1)) + 1);//*********
                    }
                    counter++;
                    multiply--;//****
                    sum += route_border;

                    for (int i = 0; i < route_border; i++) {
                        int rnd_route = cities.remove(0);
                        route_cities.add(rnd_route);
                    }
                    entry.getValue()[j] = route_cities;
                }
            }
            solutions.put(map, calculateCost(map, ROUTE_NUMBERS));
            tryCounter++;
        }
        Map<Integer, ArrayList<Integer>[]> map = bestSolution(solutions);
        Map<Integer, ArrayList<Integer>[]> copy = copy(map);

        int tryCounter2 = 0;
        while (tryCounter2 != 5000000) {
            copy = swapNodesInRoute(map);
            if (calculateCost(copy, ROUTE_NUMBERS) < calculateCost(map, ROUTE_NUMBERS)) {
                map = copy(copy);//???????
            }else
                copy = copy(map);//???????
            tryCounter2++;
        }


        print(bestSolution(solutions));
        System.out.println(calculateCost((bestSolution(solutions)), ROUTE_NUMBERS));
    }

    public static Map<Integer, ArrayList<Integer>[]> copy(Map<Integer, ArrayList<Integer>[]> map) {
        Map<Integer, ArrayList<Integer>[]> new_map = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            new_map.put(entry.getKey(), entry.getValue());
        }
        return new_map;
    }

    public static void print(Map<Integer, ArrayList<Integer>[]> map) {
        int counter = 1;
        for (Map.Entry<Integer, ArrayList<Integer>[]> depot : map.entrySet()) {
            System.out.println("Depot" + counter + ": " + depot.getKey());
            int routeNumber = 1;
            for (int i = 0; i < depot.getValue().length; i++) {
                System.out.print("    Route" + routeNumber + ": ");
                for (int j = 0; j < depot.getValue()[i].size(); j++) {
                    System.out.print(depot.getValue()[i].get(j));
                    if (j != depot.getValue()[i].size() - 1) {
                        System.out.print(",");
                    }
                }
                System.out.println();
                routeNumber++;
            }

            counter++;
        }
    }

    public static int calculateCost(Map<Integer, ArrayList<Integer>[]> map, int ROUTE_NUMBERS) {
        int cost = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            int depot = entry.getKey();
            for (int i = 0; i < ROUTE_NUMBERS; i++) {
                for (int j = 0; j < entry.getValue()[i].size(); j++) {
                    int city = entry.getValue()[i].get(j);
                    cost += TurkishNetwork.distance[depot][city];
                }
            }
        }
        return cost;
    }

    public static Map<Integer, ArrayList<Integer>[]> bestSolution(Map<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions) {
        int minCost = Collections.min(solutions.values());
        Map<Integer, ArrayList<Integer>[]> bestSolution = new LinkedHashMap<>();
        for (Map.Entry<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> entry : solutions.entrySet()) {
            if (entry.getValue() == minCost) {
                bestSolution = entry.getKey();
            }
        }
        return bestSolution;
    }

    public static int[] generateRandomNumber(int lowerBound, int upperBound) {
        int[] arr = new int[2];
        if (lowerBound == upperBound) {
            throw new RuntimeException("Alt ve üst sınır aynı olamaz!");
        }
        while (true) {
            int rnd = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
            int rnd1 = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
            if (rnd != rnd1) {
                arr[0] = rnd;
                arr[1] = rnd1;
                break;
            }
        }
        return arr;
    }

    public static Map<Integer, ArrayList<Integer>[]> swapNodesInRoute(Map<Integer, ArrayList<Integer>[]> map) {
        //int[] rndDepot = generateRandomNumber(0, DEPOT_NUMBERS);
        //int[] rnd = generateRandomNumber(0, map);
        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            if (counter == depotIndex) {
                ArrayList<Integer> temp = entry.getValue()[routeIndex];
                int[] numbers = generateRandomNumber(0, temp.size());
                Collections.swap(temp, numbers[0], numbers[1]);
            }
            counter++;
        }
        //int[] numbers = generateRandomNumber(0, 0);
        return map;
    }
}

package edu.anadolu;

import java.util.*;

/**
 * Hello world!
 */
public class App {
    static final int DEPOT_NUMBERS = 2;
    static final int ROUTE_NUMBERS = 5;
    static int cost;

    public static void main(String[] args) {
        LinkedHashMap<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions = new LinkedHashMap<>();//?????????

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
            solutions.put(map, calculateCost(map));
            tryCounter++;
        }


        LinkedHashMap<Integer, ArrayList<Integer>[]> firstSolution = bestSolution(solutions);//100.000 de bir çözüm
        //LinkedHashMap<Integer, ArrayList<Integer>[]> copy = copy(firstSolution);//copy of firstSolution
        /*ArrayList<Integer> bir = new ArrayList<>();
        bir.add(6);
        ArrayList<Integer> iki = new ArrayList<>();
        iki.add(10);
        ArrayList<Integer>[] asd = new ArrayList[]{bir, iki};*/

        LinkedHashMap<Integer, ArrayList<Integer>[]> copy = copy(firstSolution);

        /** Test */
      /*  System.out.println("fr: " + firstSolution);
        System.out.println("cr: " + copy);
        System.out.println("-----------------------");
        System.out.println("Firs");
        print(firstSolution);
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copy.entrySet()) {
            entry.setValue(asd);
        }
        System.out.println("*********************");
        System.out.println("Copy");
        print(copy);

        */


        cost = calculateCost(firstSolution);
        System.out.println("First Solution: ");
        print(firstSolution);
        System.out.println(cost);
        System.out.println("-----------------------------");

        int tryCounter2 = 0;
        while (tryCounter2 != 5000000) {

            firstSolution = check(firstSolution, swapNodesBetweenRoutes(copy));
            tryCounter2++;
        }

        print(firstSolution);
        System.out.println(cost);
        //printConsole(solutions);

    }

    public static void printConsole(LinkedHashMap<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions) {
        for (Map.Entry<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> entry : solutions.entrySet()) {
            System.out.println(entry.getValue());
        }

    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> copy(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
        LinkedHashMap<Integer, ArrayList<Integer>[]> new_map = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            ArrayList<Integer>[] arrayLists = new ArrayList[ROUTE_NUMBERS];
            for (int i = 0; i < entry.getValue().length; i++) {
                ArrayList<Integer> copyRoutes = new ArrayList<>();
                for (int j = 0; j < entry.getValue()[i].size(); j++) {
                    copyRoutes.add(entry.getValue()[i].get(j));
                }
                arrayLists[i] = copyRoutes;
            }
            new_map.put(entry.getKey(), arrayLists);
        }
        return new_map;
    }

    public static void print(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
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

    public static int calculateCost(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
        int cost = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            int beginning;
            for (int i = 0; i < ROUTE_NUMBERS; i++) {
                for (int j = 0; j < entry.getValue()[i].size(); j++) {
                    if (j == 0) {
                        beginning = entry.getKey();

                    } else {
                        beginning = entry.getValue()[i].get(j - 1);
                    }
                    int city = entry.getValue()[i].get(j);
                    cost += TurkishNetwork.distance[beginning][city];
                }
            }
        }
        return cost;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution(LinkedHashMap<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions) {
        int minCost = Collections.min(solutions.values());
        LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution = new LinkedHashMap<>();
        for (Map.Entry<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> entry : solutions.entrySet()) {
            if (entry.getValue() == minCost) {
                bestSolution = entry.getKey();
            }
        }
        return bestSolution;
    }

    public static int[] generateRandomNumber(int lowerBound, int upperBound, boolean canSame) {
        int[] arr = new int[2];
        if (lowerBound == upperBound) {
            throw new RuntimeException("Alt ve üst sınır aynı olamaz!");
        }
        if (!canSame) {
            while (true) {
                int rnd = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
                int rnd1 = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
                if (rnd != rnd1) {
                    arr[0] = rnd;
                    arr[1] = rnd1;
                    break;
                }
            }
        } else {
            arr[0] = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
            arr[1] = (int) (Math.random() * (upperBound - lowerBound) + lowerBound);
        }
        return arr;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> check(LinkedHashMap<Integer, ArrayList<Integer>[]> solution, LinkedHashMap<Integer, ArrayList<Integer>[]> newSolution) {
        int newCost = calculateCost(newSolution);
        if (newCost < cost) {
            solution = copy(newSolution);
            cost = newCost;
        }
        return solution;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> swapNodesBetweenRoutes(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {
        int[] depotIndexes = generateRandomNumber(0, DEPOT_NUMBERS, true);
        int[] routeIndexes;
        if (depotIndexes[0] == depotIndexes[1]) {
            routeIndexes = generateRandomNumber(0, ROUTE_NUMBERS, false);
        } else
            routeIndexes = generateRandomNumber(0, ROUTE_NUMBERS, true);
        int[] nodeIndexes = new int[2];
        int n1 = 0;
        int n2 = 0;
        int depot1 = 0;
        int depot2 = 0;
        //Find 2 nodes in random node and keep as n1,n2

        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (depotIndexes[0] == depot1) {
                int rnd = (int) (Math.random() * entry.getValue()[routeIndexes[0]].size());
                nodeIndexes[0] = rnd;
                n1 = entry.getValue()[routeIndexes[0]].get(rnd);
            }
            if (depotIndexes[1] == depot2) {
                int rnd = (int) (Math.random() * entry.getValue()[routeIndexes[1]].size());
                nodeIndexes[1] = rnd;
                n2 = entry.getValue()[routeIndexes[1]].get(nodeIndexes[1]);
            }
            depot1++;
            depot2++;
        }
        depot1 = 0;
        depot2 = 0;
        //Swap n1 and n2
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (depotIndexes[0] == depot1) {
                entry.getValue()[routeIndexes[0]].set(nodeIndexes[0], n2);
            }
            if (depotIndexes[1] == depot2) {
                entry.getValue()[routeIndexes[1]].set(nodeIndexes[1], n1);
            }
            depot1++;
            depot2++;
        }
        return copyMap;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> swapNodesInRoute(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {

        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (counter == depotIndex) {
                if (entry.getValue()[routeIndex].size() != 1) {
                    int[] numbers = generateRandomNumber(0, entry.getValue()[routeIndex].size(), false);
                    Collections.swap(entry.getValue()[routeIndex], numbers[0], numbers[1]);
                } else {
                    break;
                }
            }
            counter++;
        }
        return copyMap;
    }
}

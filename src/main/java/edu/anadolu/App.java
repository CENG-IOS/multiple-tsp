package edu.anadolu;

import java.util.*;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        final int DEPOT_NUMBERS = 2;
        final int ROUTE_NUMBERS = 5;
        Map<LinkedHashMap<Integer, ArrayList<Integer>[]>, Integer> solutions = new HashMap<>();


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
                        route_border = (int) (Math.random() * (cities.size() - (multiply- 1)) + 1);//*********
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

}

package edu.anadolu;

import java.util.*;

/**
 * Hello world!
 */
@SuppressWarnings("DanglingJavadoc")
public class App {
    static final int DEPOT_NUMBERS = 5;
    static final int ROUTE_NUMBERS = 2;
    static int cost = 0;

    public static void main(String[] args) {


        LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<Integer>[]> solution;
        LinkedHashMap<Integer, ArrayList<Integer>[]> copySolution;

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
            solution = map;

            if (tryCounter == 0)
                bestSolution = copy(solution);

            else {
                if (check(bestSolution, solution)) {
                    bestSolution = copy(solution);
                }
            }

            tryCounter++;
        }
        cost = calculateCost(bestSolution);


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

        System.out.println("First Solution :");
        print(bestSolution);
        System.out.println("**" + cost);


        int tryCounter2 = 0;

        copySolution = copy(bestSolution);
        while (tryCounter2 != 5000000) {
            int rnd = (int) (Math.random() * 5);
            LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap;
            //LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap = insertNodeInRoute(copySolution); //çalışmıyor.
            //LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap = swapNodesInRoute(copySolution);
            //LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap = insertNodeBetweenRoutes(copySolution); //şehirleri siliyor
            //LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap = swapHubWithNodeInRoute(copySolution);
            // LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap = swapNodesBetweenRoutes(copySolution);
           /* if (check(bestSolution, swappedMap)) {
                bestSolution = copy(swappedMap);
            }*/

            /** Switch yapısı var */
            switch (rnd) {
                case 0:
                    swappedMap = swapNodesInRoute(copySolution);
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                    }
                    break;
                case 1:
                    swappedMap = swapHubWithNodeInRoute(copySolution);
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                    }
                    break;
                case 2:
                    swappedMap = swapNodesBetweenRoutes(copySolution);
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                    }
                    break;
              /*  case 3:
                    swappedMap = insertNodeInRoute(copySolution);
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                    }
                    break;*/
             /*   case 4:
                    if (DEPOT_NUMBERS >= 2) {
                        swappedMap = insertNodeBetweenRoutes(copySolution);
                        if (check(bestSolution, swappedMap)) {
                            bestSolution = copy(swappedMap);
                        }
                    }*/
                default:
                    continue;
            }


            tryCounter2++;
        }
        cost = calculateCost(bestSolution);
        System.out.println("New Solution :");
        print(bestSolution);
        System.out.println("**" + cost);

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

    public static boolean check(LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution, LinkedHashMap<Integer, ArrayList<Integer>[]> newSolution) {
        int newCost = calculateCost(newSolution);
        int originalCost = calculateCost(bestSolution);
        return newCost < originalCost;
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

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> swapHubWithNodeInRoute(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {

        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int depotIndex1 = (int) (Math.random() * DEPOT_NUMBERS);
        int routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
        int counter = 0;
        int counterTemp = 0;

        int temp = -1;
        int temp1 = -1;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {

            if (counter == depotIndex) {
                temp1 = entry.getKey();

            }
            if (depotIndex1 == counter) {
                counterTemp = (int) (Math.random() * entry.getValue()[routeIndex].size());
                temp = entry.getValue()[routeIndex].get(counterTemp);
            }
            if (temp != -1 && temp1 != -1) {

                break;
            }

            counter++;

        }

        int depotCounter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (depotCounter == depotIndex1) {
                entry.getValue()[routeIndex].set(counterTemp, temp1);
            }
            depotCounter++;
        }


        ArrayList<Integer>[] array = new ArrayList[copyMap.get(temp1).length];
        for (int i = 0; i < array.length; i++) {
            array[i] = copyMap.get(temp1)[i];
        }

        copyMap.remove(temp1);
        copyMap.put(temp, array);


        return copyMap;
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

    //yanlışşşşşşşşşşşşşşş //zorlamasın
    public static LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeInRoute(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {
        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (counter == depotIndex) {
                int[] routeIndexes;
                do {
                    routeIndexes = generateRandomNumber(0, entry.getValue().length, false);
                } while (!(!(entry.getValue()[routeIndexes[0]].size() == 1) ||
                        !(entry.getValue()[routeIndexes[1]].size() == 1)));
                if (entry.getValue()[routeIndexes[0]].size() == 1 || entry.getValue()[routeIndexes[1]].size() == 1) {
                    if (entry.getValue()[routeIndexes[0]].size() == 1) {
                        int rnd = (int) (Math.random() * entry.getValue()[routeIndexes[1]].size());
                        int deleted = entry.getValue()[routeIndexes[1]].remove(rnd);
                        entry.getValue()[routeIndexes[0]].add(deleted);
                    } else {
                        int rnd = (int) (Math.random() * entry.getValue()[routeIndexes[0]].size());
                        int deleted = entry.getValue()[routeIndexes[0]].remove(rnd);
                        entry.getValue()[routeIndexes[1]].add(deleted);
                    }
                } else {
                    ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
                    temp.add(entry.getValue()[routeIndexes[0]]);
                    temp.add(entry.getValue()[routeIndexes[1]]);
                    Collections.shuffle(temp);
                    int rnd = (int) (Math.random() * temp.size());
                    //yanlış olan yer bura knk bir tane daha random sayı seçip o indexe atman gerekir
                    ArrayList<Integer> a = temp.get(0);
                    int b = a.remove(rnd);
                    temp.get(1).add(b);
                }
            }
        }
        return copyMap;
    }

    //en az 2 depot olması lazım bu method çalışması için. //site gg
    public static LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeBetweenRoutes(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {
        int[] depotIndexes = generateRandomNumber(0, DEPOT_NUMBERS, false);
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(depotIndexes[0]);
        temp.add(depotIndexes[1]);
        Collections.shuffle(temp);

        int depot1 = 0;
        int depot2 = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            int deleted = -1;
            if (temp.get(0) == depot1) {
                int route1Index = (int) (Math.random() * entry.getValue().length);
                if (entry.getValue()[route1Index].size() == 1) {
                    break;//zorlamıyor
                } else {
                    int node = (int) (Math.random() * entry.getValue()[route1Index].size());
                    deleted = entry.getValue()[route1Index].remove(node);
                }
            }
            if (temp.get(1) == depot2) {
                if (deleted == -1) {
                    continue;
                } else {
                    int route2Index = (int) (Math.random() * entry.getValue().length);
                    int node = (int) (Math.random() * entry.getValue()[route2Index].size());
                    entry.getValue()[route2Index].add(node + 1, deleted);
                }
            }
            depot1++;
            depot2++;
        }


        return copyMap;
    }
}

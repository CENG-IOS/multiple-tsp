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

    static int swapNodesInRoute = 0;
    static int swapHubWithNodeInRoute = 0;
    static int swapNodesBetweenRoutes = 0;
    static int insertNodeInRoute = 0;
    static int insertNodeBetweenRoutes = 0;

    public static void main(String[] args) {

        LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<Integer>[]> solution;
        LinkedHashMap<Integer, ArrayList<Integer>[]> copySolution;

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
        System.out.println("** " + cost);
        int tryCounter2 = 0;

        copySolution = copy(bestSolution);
        while (tryCounter2 != 5000000) {
            int rnd = (int) (Math.random() * 4);
            LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap;

            /** Switch yapısı var */
            switch (rnd) {
                case 0:
                    swappedMap = (swapNodesInRoute(copySolution));
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapNodesInRoute++;
                    }
                    break;
                case 1:
                    swappedMap = (swapHubWithNodeInRoute(copySolution));
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapHubWithNodeInRoute++;
                    }
                    break;
                case 2:
                    swappedMap = (swapNodesBetweenRoutes(copySolution));
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapNodesBetweenRoutes++;
                    }
                    break;
                case 3:
                    swappedMap = (insertNodeInRoute(copySolution));
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        insertNodeInRoute++;
                    }
                    break;
                case 4:
                    if (DEPOT_NUMBERS >= 2) {
                        swappedMap = insertNodeBetweenRoutes(copySolution);
                        if (check(bestSolution, swappedMap)) {
                            bestSolution = copy(swappedMap);
                            insertNodeBetweenRoutes++;
                        }
                    }
            }
            tryCounter2++;
        }
        cost = calculateCost(bestSolution);
        System.out.println("New Solution :");
        print(bestSolution);
        System.out.println("** " + cost);
        printCounters();
    }

    public static void printCounters() {// bozuk counterlar
        System.out.println("swapNodesInRoute: " + swapNodesInRoute);
        System.out.println("swapHubWithNodeInRoute: " + swapHubWithNodeInRoute);
        System.out.println("swapNodesBetweenRoutes: " + swapNodesBetweenRoutes);
        System.out.println("insertNodeInRoute: " + insertNodeInRoute);
        System.out.println("insertNodeBetweenRoutes: " + insertNodeBetweenRoutes);
        /*int temp = 5000000 - (swapNodesInRoute + swapHubWithNodeInRoute + swapNodesBetweenRoutes + insertNodeInRoute + insertNodeBetweenRoutes);
        System.out.println("Atlanan adım sayısı: " + temp);*/
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
        int depot = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : map.entrySet()) {
            int beginning;
            int city = 0;
            for (int i = 0; i < ROUTE_NUMBERS; i++) {
                for (int j = 0; j < entry.getValue()[i].size(); j++) {
                    if (j == 0) {
                        beginning = entry.getKey();
                        depot = entry.getKey();
                    } else {
                        beginning = entry.getValue()[i].get(j - 1);
                    }
                    city = entry.getValue()[i].get(j);
                    cost += TurkishNetwork.distance[beginning][city];
                }
                cost += TurkishNetwork.distance[city][depot];
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
                    //swapNodesInRoute++;
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
        //swapHubWithNodeInRoute++;
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
        //swapNodesBetweenRoutes++;
        return copyMap;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeInRoute(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {
        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {
            if (counter == depotIndex) {
                /*int[] routeIndexes = generateRandomNumber(0, entry.getValue().length, false);
                if (entry.getValue()[routeIndexes[0]].size() != 1 || entry.getValue()[routeIndexes[1]].size() != 1) {
                    System.out.println("zorlamadı knk");
                }else{
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
                }*/
                //int[] routeIndexes = generateRandomNumber(0, entry.getValue().length, false);
                int routeIndex = (int) (Math.random() * entry.getValue().length);
                if (entry.getValue()[routeIndex].size() != 1) {
                    //System.out.println("uzunluğu 1, değişim yapılmadı");
                    int[] nodesIndexes = generateRandomNumber(0, entry.getValue()[routeIndex].size(), false);
                    int rndIndex1 = nodesIndexes[0];//silinecek olan node'un indexi
                    int rndIndex2 = nodesIndexes[1];//yanına alınacak olan node'un indexi
                    int number1 = entry.getValue()[routeIndex].get(rndIndex1);//silinecek olan node
                    int number2 = entry.getValue()[routeIndex].get(rndIndex2);//yanına alınacak olan node
                    entry.getValue()[routeIndex].set(rndIndex1, -1);//silmek yerine -1 koydum
                    entry.getValue()[routeIndex].add(rndIndex2 + 1, number1);
                    entry.getValue()[routeIndex].remove((Integer) (-1));//???????????
                    /*for (int i = 0; i < entry.getValue()[routeIndex].size(); i++) {
                        if (entry.getValue()[routeIndex].get(i) == -1) {
                            Integer a = entry.getValue()[routeIndex].remove(i);//?????????
                        }
                    }*/
                    //insertNodeInRoute++;
                    break;
                }
                /*int rnd = (int) (Math.random() * 2);
                    int selectedIndex = nodesIndexes[rnd];
                    int selected = entry.getValue()[routeIndex].get(selectedIndex);
                    int otherIndex;
                    if (rnd == 1) {
                        otherIndex = 0;
                    } else
                        otherIndex = 1;
                    entry.getValue()[routeIndex].set(selectedIndex, -1);////////////////////
                    int temp = nodesIndexes[otherIndex] + 1;
                    entry.getValue()[routeIndex].set(temp, selected);
                    for (int i = 0; i < entry.getValue()[routeIndex].size(); i++) {
                        if (entry.getValue()[routeIndex].get(i) == -1) {
                            Integer a = entry.getValue()[routeIndex].remove(i);//?????????
                        }
                    }*/
            }
        }
        return copyMap;
    }

    public static LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeBetweenRoutes(LinkedHashMap<Integer, ArrayList<Integer>[]> copyMap) {
        int[] depotIndexes = generateRandomNumber(0, DEPOT_NUMBERS, false);
        int depot1 = 0;
        int depot2 = 0;
        int deleted = -1;
        int route1Index = 0;
        int route2Index;
        ArrayList<Integer> route1;
        ArrayList<Integer> route2;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copyMap.entrySet()) {

            if (depotIndexes[0] == depot1) {

                if (entry.getValue()[route1Index].size() == 1) {
                    break;
                } else {
                    route1 = entry.getValue()[route1Index];
                    int node = (int) (Math.random() * route1.size());
                    deleted = entry.getValue()[route1Index].remove(node);
                }
            }
            if (depotIndexes[1] == depot2) {
                if (deleted == -1) {
                    depot1++;
                    continue;
                } else {
                    route2Index = (int) (Math.random() * entry.getValue().length);
                    route2 = entry.getValue()[route2Index];


                    int node = (int) (Math.random() * route2.size());
                    entry.getValue()[route2Index].add(node + 1, deleted);

                    break;

                }
            }
            depot1++;
            depot2++;
        }
        return copyMap;
    }
}

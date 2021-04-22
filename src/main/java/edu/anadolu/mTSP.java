package edu.anadolu;


import java.util.*;

@SuppressWarnings("ALL")
public class mTSP {
    private final int DEPOT_NUMBERS;
    private final int ROUTE_NUMBERS;
    private int cost = 0;

    private int swapNodesInRoute = 0;
    private int swapHubWithNodeInRoute = 0;
    private int swapNodesBetweenRoutes = 0;
    private int insertNodeInRoute = 0;
    private int insertNodeBetweenRoutes = 0;

    private LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution = new LinkedHashMap<>();
    private LinkedHashMap<Integer, ArrayList<Integer>[]> solution;
    private LinkedHashMap<Integer, ArrayList<Integer>[]> copySolution;

    public mTSP(int depot_numbers, int route_numbers) {
        DEPOT_NUMBERS = depot_numbers;
        ROUTE_NUMBERS = route_numbers;
    }

    private void printCounters() {
        System.out.println("swapNodesInRoute: " + swapNodesInRoute);
        System.out.println("swapHubWithNodeInRoute: " + swapHubWithNodeInRoute);
        System.out.println("swapNodesBetweenRoutes: " + swapNodesBetweenRoutes);
        System.out.println("insertNodeInRoute: " + insertNodeInRoute);
        System.out.println("insertNodeBetweenRoutes: " + insertNodeBetweenRoutes);
    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> copy(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
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

    private void print(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
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

    private int calculateCost(LinkedHashMap<Integer, ArrayList<Integer>[]> map) {
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

    private int[] generateRandomNumber(int lowerBound, int upperBound, boolean canSame) {
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

    private boolean check(LinkedHashMap<Integer, ArrayList<Integer>[]> bestSolution, LinkedHashMap<Integer, ArrayList<Integer>[]> newSolution) {
        int newCost = calculateCost(newSolution);
        int originalCost = calculateCost(bestSolution);
        if (newCost < originalCost) {
            cost = newCost;
            return true;
        }
        return false;

    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> swapNodesInRoute() {
        Collection<ArrayList<Integer>[]> routes = new ArrayList<>();


        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
        int counter = 0;

        routes = copySolution.values();


        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {

            if (counter == depotIndex) {
                if (entry.getValue()[routeIndex].size() != 1) {
                    int[] numbers = generateRandomNumber(0, entry.getValue()[routeIndex].size(), false);
                    Collections.swap(entry.getValue()[routeIndex], numbers[0], numbers[1]);
                } else {
                    depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
                    routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
                    counter = -1;
                }
            }
            counter++;
        }

        return copySolution;
    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> swapHubWithNodeInRoute() {
        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int depotIndex1 = (int) (Math.random() * DEPOT_NUMBERS);
        int routeIndex = (int) (Math.random() * ROUTE_NUMBERS);
        int counter = 0;
        int counterTemp = 0;
        int temp = -1;
        int temp1 = -1;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {
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
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {
            if (depotCounter == depotIndex1) {
                entry.getValue()[routeIndex].set(counterTemp, temp1);
            }
            depotCounter++;
        }
        ArrayList<Integer>[] array = new ArrayList[copySolution.get(temp1).length];
        for (int i = 0; i < array.length; i++) {
            array[i] = copySolution.get(temp1)[i];
        }
        copySolution.remove(temp1);
        copySolution.put(temp, array);
        return copySolution;
    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> swapNodesBetweenRoutes() {
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
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {
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
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {
            if (depotIndexes[0] == depot1) {
                entry.getValue()[routeIndexes[0]].set(nodeIndexes[0], n2);
            }
            if (depotIndexes[1] == depot2) {
                entry.getValue()[routeIndexes[1]].set(nodeIndexes[1], n1);
            }
            depot1++;
            depot2++;
        }
        return copySolution;
    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeInRoute() {
        int depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {
            if (counter == depotIndex) {

                int routeIndex = (int) (Math.random() * entry.getValue().length);
                if (entry.getValue()[routeIndex].size() != 1) {
                    int[] nodesIndexes = generateRandomNumber(0, entry.getValue()[routeIndex].size(), false);
                    int rndIndex1 = nodesIndexes[0];//silinecek olan node'un indexi
                    int rndIndex2 = nodesIndexes[1];//yanına alınacak olan node'un indexi
                    int number1 = entry.getValue()[routeIndex].get(rndIndex1);//silinecek olan node
                    entry.getValue()[routeIndex].set(rndIndex1, -1);//silmek yerine -1 koydum
                    entry.getValue()[routeIndex].add(rndIndex2 + 1, number1);
                    entry.getValue()[routeIndex].remove((Integer) (-1));//???????????
                    break;
                } else {
                    depotIndex = (int) (Math.random() * DEPOT_NUMBERS);
                    counter = 0;

                }
            }
            counter++;
        }
        return copySolution;
    }

    private LinkedHashMap<Integer, ArrayList<Integer>[]> insertNodeBetweenRoutes() {
        int[] depotIndexes = generateRandomNumber(0, DEPOT_NUMBERS, false);
        int depot1 = 0;
        int depot2 = 0;
        int deleted = -1;
        int route1Index = 0;
        int route2Index;
        ArrayList<Integer> route1;
        ArrayList<Integer> route2;
        for (Map.Entry<Integer, ArrayList<Integer>[]> entry : copySolution.entrySet()) {

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
        return copySolution;
    }

    public void firstPart (){
        int tryCounter = 0;
        while (tryCounter != 100000) {
            int route_border = (81 - DEPOT_NUMBERS) / (DEPOT_NUMBERS * ROUTE_NUMBERS);
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
                int rndRouteBorder = 0;
                for (int j = 0; j < ROUTE_NUMBERS; j++) {
                    ArrayList<Integer> route_cities = new ArrayList<>();
                    if (counter == DEPOT_NUMBERS * ROUTE_NUMBERS - 1) {
                        rndRouteBorder = 81 - sum;
                    } else {
                        rndRouteBorder = (int) (Math.random() * 2 + route_border);
                    }
                    counter++;

                    sum += rndRouteBorder;

                    for (int i = 0; i < rndRouteBorder; i++) {
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
        copySolution = copy(bestSolution);
        System.out.println("First Solution :");
        print(bestSolution);
        System.out.println("** " + calculateCost(bestSolution));
    }

    public void secondPart(){
        int tryCounter2 = 0;
        while (tryCounter2 != 5000000) {

            int rnd = (int) (Math.random() * 5);
            LinkedHashMap<Integer, ArrayList<Integer>[]> swappedMap;


            switch (rnd) {
                case 0:
                    swappedMap = (swapNodesInRoute());
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapNodesInRoute++;
                    } else {
                        copySolution = copy(bestSolution);
                    }
                    break;
                case 1:
                    swappedMap = (swapHubWithNodeInRoute());
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapHubWithNodeInRoute++;
                    }
                    else {
                        copySolution = copy(bestSolution);
                    }
                    break;
                case 2:
                    swappedMap = (swapNodesBetweenRoutes());
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        swapNodesBetweenRoutes++;
                    }
                    else {
                        copySolution = copy(bestSolution);
                    }
                    break;
                case 3:
                    swappedMap = (insertNodeInRoute());
                    if (check(bestSolution, swappedMap)) {
                        bestSolution = copy(swappedMap);
                        insertNodeInRoute++;
                    }
                    else {
                        copySolution = copy(bestSolution);
                    }
                    break;
                case 4:
                    if (DEPOT_NUMBERS >= 2) {
                        swappedMap = (insertNodeBetweenRoutes());
                        if (check(bestSolution, swappedMap)) {
                            bestSolution = copy(swappedMap);
                            insertNodeBetweenRoutes++;
                        }
                        else {
                            copySolution = copy(bestSolution);
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

}

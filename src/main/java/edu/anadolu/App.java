package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Hello world!
 */
@SuppressWarnings("ALL")
public class App {
    public static void main(String[] args) {
        int numTasks = 20;
        ForkJoinPool pool = new ForkJoinPool(numTasks);

        Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }

        mTSP best = null;
        //int minCost = Integer.MAX_VALUE;

        ConcurrentLinkedQueue<mTSP> solutions = new ConcurrentLinkedQueue<>();

        long startTime = System.currentTimeMillis();
        /** First Part */
        IntStream.range(1, 100_000)
                .boxed()
                .parallel()
                .forEach(integer -> {
                            mTSP mTSP = new mTSP(params.getNumDepots(), params.getNumSalesmen());
                            mTSP.randomSolution();
                            mTSP.validate();
                            solutions.add(mTSP);
                        }
                );
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);

        /** Finding best */
        best = solutions.stream().min(Comparator.comparingInt(mTSP::cost)).get();

        if (best != null) {
            best.print(params.getVerbose());
            System.out.println("**Total cost is " + best.cost());
        }

        System.out.println("**************************************************************************");

        /** Second Part */
        for (int i = 0; i < 5_000_000; i++) {
            best.improveSolution();
        }

        best.print(params.getVerbose());
        System.out.println("**Total cost is " + best.cost());
        best.printCounters();
        best.writeJSONFILE();

    }
}

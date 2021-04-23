package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

/**
 * Hello world!
 */
@SuppressWarnings("ALL")
public class App {
    public static void main(String[] args) {
        Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }

        mTSP best = null;
        int minCost = Integer.MAX_VALUE;

        /** First Part */
        for (int i = 0; i < 100_000; i++) {

            mTSP mTSP = new mTSP(params.getNumDepots(), params.getNumSalesmen());

            mTSP.randomSolution();
            mTSP.validate();

            final int cost = mTSP.cost();

            if (cost < minCost) {
                best = mTSP;
                minCost = cost;
            }
        }

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

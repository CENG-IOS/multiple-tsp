package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

public class Main {
    public static void main(String[] args) {
        Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }

        mTSP mTSP = new mTSP(params.getNumDepots(), params.getNumSalesmen(), params.getVerbose());
        mTSP.firstPart();
        mTSP.secondPart();
    }
}

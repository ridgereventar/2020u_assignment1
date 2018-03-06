package sample;

import java.io.*;
import java.util.*;

public class ProbabilityMap {

    public Map<String,Double> pMap;

    public ProbabilityMap() {
        pMap = new TreeMap<>();
    }

    public void proccessProbMap(FrequencyMap ham, FrequencyMap spam) {

        double numOfHamFiles = 2500.0;
        double numOfSpamFiles = 500.0;

        // iterate through trainSpamFreq map
        for (Map.Entry<String,Integer> entrySpam : spam.fmap.entrySet()) {

            String key = entrySpam.getKey();

            if(ham.fmap.containsKey(key)){
                // calculate probability values.
                double prW_S = (double)entrySpam.getValue() / numOfSpamFiles;
                double prW_H = (double)ham.fmap.get(key) / numOfHamFiles;
                double prS_W = prW_S / (prW_S + prW_H);
                pMap.put(key, prS_W);
            } else {
                // prS_W simplifies to 1.0 if word is not contained in hamFreqMap.
                pMap.put(key, 1.0);
            }
        }
        // iterate through trainHamFreq map
        for (Map.Entry<String,Integer> entryHam : ham.fmap.entrySet()) {

            String key = entryHam.getKey();

            if(spam.fmap.containsKey(key)){
                // calculate probability values.
                double prW_S = (double)spam.fmap.get(key)/ numOfSpamFiles;
                double prW_H = (double)entryHam.getValue() / numOfHamFiles;
                double prS_W = prW_S / (prW_S + prW_H);
                pMap.put(key, prS_W);
            } else {
                // prS_W simplifies to 0.0 if word is not contained in spamFreqMap.
                pMap.put(key, 0.0);
            }
        }


    }

}
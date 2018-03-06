package sample;

import java.io.*;
import java.util.*;

public class FrequencyMap {

    public Map<String,Integer> fmap;

    public FrequencyMap() {
        fmap = new TreeMap<>();
    }

    private boolean isWord(String word) {
        String pattern = "^[a-zA-Z]+$";
        if (word.matches(pattern)) {
            return true;
        } else { return false; }
    }

    public void processMap(File file) throws IOException {

        if(file.isDirectory()) {

            //process all the files in that directory.
            File[] contents = file.listFiles();

            for(int i=0; i < contents.length; i++) {

                Scanner scanner = new Scanner(contents[i]);
                scanner.useDelimiter("\\s");

                // A map that checks if the word was already found in each particular file.
                Map<String,Boolean> wordsFoundInFile = new TreeMap<>();

                while(scanner.hasNext()) {

                    String word = scanner.next();

                    if(isWord(word)) {

                        if(!fmap.containsKey(word) && !wordsFoundInFile.containsKey(word)) {
                            // if the fmap AND wordFoundInFile both don't contain the word,
                            // then add it into both maps.
                            fmap.put(word, 1);
                            wordsFoundInFile.put(word, true);

                        } else {
                            // if the fmap contains the word but wordFoundInFile does not,
                            // this means we are in a new file, since wordFoundInFile was re-initialized.
                            // We can then increment the key value by one and add it into wordFoundInFile.
                            if(!wordsFoundInFile.containsKey(word)) {
                                fmap.put(word, fmap.get(word) + 1);
                                wordsFoundInFile.put(word, true);
                            }
                        }
                    }
                }
            }
        }
    }

}
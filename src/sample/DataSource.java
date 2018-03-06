package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataSource {

    // This method takes a directory and breaks it down into an ArrayList of its subFolders.
    // Allows for indexing which folder to search through.
    public static void generateSubDirectorys(File dir, ArrayList<File> folderlist) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                folderlist.add(file);
                generateSubDirectorys(file, folderlist);
            }
        }
    }

    // Generates the test files, and stores them into an ObservableList<>.
    public static void generateTestFiles(File dir, ObservableList<TestFile> testList, ProbabilityMap probMap ) throws IOException {

        File[] files = dir.listFiles();

        for(File file: files) {

            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\s");

            ArrayList<Double> n_list = new ArrayList<>();

            while(scanner.hasNext()) {
                String word = scanner.next();
                if(probMap.pMap.containsKey(word)) {
                    double wordProb = probMap.pMap.get(word);
                    double n_element = (Math.log(1.0 - wordProb)) - Math.log(wordProb);
                    n_list.add(n_element);
                }
            }

            // Sum the elements in n_list to obtain the n value.
            double n = 0;
            for(int i=0; i < n_list.size(); i++) {
                n += n_list.get(i);
            }

            // use that n value to obtain prS|F value
            double prS_F = 1 / ( 1 + (Math.pow(Math.E, n)) );

            testList.add(new TestFile(file.getName(), prS_F, dir.getName()));
        }
    }

    // Generates the test files, and stores them into an ArrayList<>.
    public static void generateArrayList(File dir, ArrayList<TestFile> testList, ProbabilityMap probMap ) throws IOException {

        File[] files = dir.listFiles();

        for(File file: files) {

            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\s");

            ArrayList<Double> n_list = new ArrayList<>();

            while(scanner.hasNext()) {
                String word = scanner.next();
                if(probMap.pMap.containsKey(word)) {
                    double wordProb = probMap.pMap.get(word);
                    double n_element = (Math.log(1.0 - wordProb)) - Math.log(wordProb);
                    n_list.add(n_element);
                }
            }

            // Sum the elements in n_list to obtain the n value.
            double n = 0;
            for(int i=0; i < n_list.size(); i++) {
                n += n_list.get(i);
            }

            // use that n value to obtain prS|F value
            double prS_F = 1 / ( 1 + (Math.pow(Math.E, n)) );

            testList.add(new TestFile(file.getName(), prS_F, dir.getName()));
        }
    }

    // This functions initiates both TRAINING and TESTING. It then returns the ObservableList<>.
    public static ObservableList<TestFile> getAllTestFiles(File dir) {

        // TRAINING:

        // initializing maps
        FrequencyMap trainHamFreq = new FrequencyMap();
        FrequencyMap trainSpamFreq = new FrequencyMap();
        ProbabilityMap probMap = new ProbabilityMap();

        // ENHANCEMENT: generateSubDirectorys() method.
        //
        //              If user wanted to use a different directory other than 'data',
        //              the user can choose any directory when prompted to do so,
        //              and by printing this ArrayList called 'folderList', they will
        //              be able to see a list of all the paths they can search.
        // Example...
        //
        // Result:
        // index 0 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/test
        // index 1 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/test/ham   <-- will be used for TESTING
        // index 2 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/test/spam  <-- will be used for TESTING
        // index 3 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/train
        // index 4 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/train/ham  <-- will be used for TRAINING
        // index 5 = /Users/RidgeReventar/IdeaProjects/Assignment1/data/train/spam <-- will be used for TRAINING

        ArrayList<File> folderList = new ArrayList<>();
        generateSubDirectorys(dir, folderList);

        ObservableList<TestFile> files = FXCollections.observableArrayList();

        try {
            // process the maps
            trainHamFreq.processMap(folderList.get(4));
            trainSpamFreq.processMap(folderList.get(5));
            probMap.proccessProbMap(trainHamFreq, trainSpamFreq);

            // TESTING:

            // generate TestFile List :
            generateTestFiles(folderList.get(1), files, probMap);
            generateTestFiles(folderList.get(2), files, probMap);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;

    }

    // Same functionality as getAllTestFiles() method, except it returns an ArrayList<> instead of ObservableList<>
    public static ArrayList<TestFile> getArrayList(File dir) {

        FrequencyMap trainHamFreq = new FrequencyMap();
        FrequencyMap trainSpamFreq = new FrequencyMap();
        ProbabilityMap probMap = new ProbabilityMap();

        ArrayList<File> folderList = new ArrayList<>();
        generateSubDirectorys(dir, folderList);


        ArrayList<TestFile> files = new ArrayList<>();


        try {
            // process the maps
            trainHamFreq.processMap(folderList.get(4));
            trainSpamFreq.processMap(folderList.get(5));
            probMap.proccessProbMap(trainHamFreq, trainSpamFreq);

            // generate TestFile List :
            generateArrayList(folderList.get(1), files, probMap);
            generateArrayList(folderList.get(2), files, probMap);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;

    }

    // Calculates accuracy and precision values and stores them into its own ArrayList<>
    public static ArrayList<String> getAccuracyAndPrecision(ArrayList<TestFile> list) {

        ArrayList<String> accuracyPrecisionList = new ArrayList<>();

        double hamCorrectCount = 0.0;
        double hamWrongCount = 0.0;
        double spamCorrectCount = 0.0;
        double spamWrongCount = 0.0;

        double total = 2800.0;

        for(TestFile file: list) {
            if(file.isHam() && file.getSpamProbRounded().equals("0.00000")) {
                hamCorrectCount++;
            } else if(file.isHam() && !file.getSpamProbRounded().equals("0.00000")) {
                hamWrongCount++;
            } else if(file.isSpam() && !file.getSpamProbRounded().equals("0.00000")) {
                spamCorrectCount++;
            } else if(file.isSpam() && file.getSpamProbRounded().equals("0.00000")) {
                spamWrongCount++;
            }
        }

        double accuracy = (hamCorrectCount + spamCorrectCount) / total;
        double precision = spamCorrectCount / (spamCorrectCount + hamWrongCount);

        accuracyPrecisionList.add(Double.toString(accuracy));
        accuracyPrecisionList.add(Double.toString(precision));

        return accuracyPrecisionList;
    }


}

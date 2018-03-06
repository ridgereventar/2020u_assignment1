package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

public class Main extends Application {
    private TableView<TestFile> files;
    private TextField Accuracy, Precision;

    public static void main(String[] args) { Application.launch(args); }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Assignment 1");

        // ask user to choose the directory containing the ham and spam files.
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        // main table
        TableColumn<TestFile, String> fileNameCol = new TableColumn<>("File");
        fileNameCol.setPrefWidth(300);
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("Filename"));

        TableColumn<TestFile, String> classCol = new TableColumn<>("Actual Class");
        classCol.setPrefWidth(100);
        classCol.setCellValueFactory(new PropertyValueFactory<>("ActualClass"));

        TableColumn<TestFile, Double> probCol = new TableColumn<>("Spam Probability");
        probCol.setPrefWidth(100);
        probCol.setCellValueFactory(new PropertyValueFactory<>("SpamProbRounded"));

        this.files = new TableView<>();
        // returns the testFileList as an ObservableList<>
        this.files.setItems(DataSource.getAllTestFiles(mainDirectory));

        this.files.getColumns().add(fileNameCol);
        this.files.getColumns().add(classCol);
        this.files.getColumns().add(probCol);

        // text fields
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setVgap(10);
        gp.setHgap(10);

        // returns the testFileList as an ArrayList<>
        ArrayList<TestFile> filelist = DataSource.getArrayList(mainDirectory);
        // acquires an ArrayList<> containing the accuracy and precision values.
        ArrayList<String> accuracyAndPrecision = DataSource.getAccuracyAndPrecision(filelist);

        // assigning the ArrayList elements to String variables.
        String accuracy = accuracyAndPrecision.get(0);
        String precision = accuracyAndPrecision.get(1);

        Label accuracyLabel = new Label("Accuracy");
        Accuracy = new TextField(accuracy);
        gp.add(accuracyLabel, 0, 0);
        gp.add(Accuracy, 1, 0);

        Label precisionLabel = new Label("Precision");
        Precision = new TextField(precision);
        gp.add(precisionLabel, 0, 1);
        gp.add(Precision, 1, 1);

        // Initializing borderPane
        BorderPane layout = new BorderPane();
        layout.setCenter(files);
        layout.setBottom(gp);

        Scene scene = new Scene(layout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}

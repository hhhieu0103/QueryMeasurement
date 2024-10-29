package org.hieuho.querymeasurement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class QueryMeasurementController {
    @FXML
    private TreeView<String> chartTree;

    @FXML
    private ListView<String> fileListView;

    @FXML
    private BorderPane chartContainer;

    private FileReader reader;
    private List<BarChartFX> barChartFXList;
    private ChartFactory factory;

    public void initialize() {
        reader = new FileReader();
//        reader.importCSVFilesToDB();
        float[][] executionTimes = executeQueries();
        buildChartFactory(executionTimes);
        barChartFXList = createBarCharts();
        addChartsToTree();
        createFileListView();
    }

    private float[][] executeQueries() {
        List<String> csvFileNames = reader.getDataFileNamesWithoutExtension();
        List<String> queries = reader.getQueries();
        QueryExecutor executor = new QueryExecutor(csvFileNames, queries);
        try {
            executor.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return executor.getExecutionTimes();
    }

    private void buildChartFactory(float[][] executionTimes) {
        List<String> csvFileNames = reader.getDataFileNamesWithoutExtension();
        List<String> queries = reader.getQueries();
        List<String> queryLabels = IntStream.range(0, queries.size())
                .mapToObj(value -> "Query " + (value + 1))
                .toList();
        factory = new ChartFactory(csvFileNames, queryLabels, executionTimes);
    }

    private List<BarChartFX> createBarCharts() {
        List<BarChartFX> barChartFXList = new ArrayList<>();
        barChartFXList.add(factory.createGeneralBarChart());
        barChartFXList.addAll(factory.createBarChartsOnTableSize());
        barChartFXList.addAll(factory.createBarChartsOnQuery());
        return barChartFXList;
    }

    private void addChartsToTree() {
        TreeItem<String> rootItem = new TreeItem<>("Bar Charts");
        rootItem.setExpanded(true);
        barChartFXList.forEach(barChartFX -> {
            TreeItem<String> item = new TreeItem<>(barChartFX.getTitle());
            rootItem.getChildren().add(item);
        });
        chartTree.setRoot(rootItem);

        chartTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            Optional<BarChartFX> selected = barChartFXList.stream()
                    .filter(barChartFX -> barChartFX.getTitle().equals(newValue.getValue()))
                    .findFirst();

            if (selected.isPresent()) {
                BarChartFX barChartFX = selected.get();
                chartContainer.setCenter(barChartFX.getBarChart());
            }
        });

        chartTree.getSelectionModel().select(1);
    }

    private void createFileListView() {
        List<String> dataFiles = reader.getDataFileNamesWithoutExtension();
        dataFiles = dataFiles.stream().map(name -> "[DATA] " + name).toList();
        List<String> queryFiles = reader.getQueryFileNamesWithoutExtension();
        queryFiles = queryFiles.stream().map(name -> "[QUERY] " + name).toList();
        ObservableList<String> observableList = FXCollections.observableArrayList(dataFiles);
        observableList.addAll(queryFiles);
        fileListView.setItems(observableList);
    }

    public void onDataFilesSelected(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            List<Path> filePaths = files.stream().map(File::toPath).toList();
            reader.setDataFilePaths(filePaths);
            reader.importCSVFilesToDB();
            float[][] executionTimes = executeQueries();
            factory.setData(executionTimes);
            barChartFXList = createBarCharts();
            addChartsToTree();
            createFileListView();
        }
    }

    public void onQueriesFileSelected(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file", "*.txt"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            List<Path> filePaths = files.stream().map(File::toPath).toList();
            reader.setQueryFilePaths(filePaths);
            float[][] executionTimes = executeQueries();
            factory.setData(executionTimes);
            barChartFXList = createBarCharts();
            addChartsToTree();
            createFileListView();
        }
    }

    public void onChartsSaved(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            String filePath = file.getPath();
            List<BarChartFX> barChartFXList = createBarCharts();
            for (BarChartFX barChartFX : barChartFXList) {
                Scene scene = new Scene(new Group());
                BarChart<String, Number> barChart = barChartFX.getBarChart();
                barChart.setPrefHeight(640);
                barChart.setPrefWidth(800);
                ((Group) scene.getRoot()).getChildren().add(barChart);
                WritableImage image = scene.snapshot(null);
                File chartImage = new File(filePath + "\\" + barChart.getTitle() + ".png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", chartImage);
                } catch (IOException e) {
                    System.out.println("Unable to save chart " + barChart.getTitle());
                    System.out.println(e.getMessage());
                }
            }

            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    System.out.println("Unable to open charts folder: " + filePath);
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void onDefaultDataUsed(ActionEvent actionEvent) {
        reader.setDefaultDataPath();
        reader.importCSVFilesToDB();

        float[][] executionTimes = executeQueries();
        factory.setData(executionTimes);
        barChartFXList = createBarCharts();
        addChartsToTree();
        createFileListView();
    }

    public void onDefaultQueriesUsed(ActionEvent actionEvent) {
        reader.setDefaultQueryPath();

        float[][] executionTimes = executeQueries();
        factory.setData(executionTimes);
        barChartFXList = createBarCharts();
        addChartsToTree();
        createFileListView();
    }
}
package org.hieuho.querymeasurement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class QueryMeasurementController {
    @FXML
    private TreeView<String> chartTree;

    @FXML
    private BorderPane chartContainer;

    public void initialize() {
        FileReader reader = new FileReader();
//        reader.importCSVFilesToDB();
        float[][] executionTimes = executeQueries(reader);
        List<BarChartFX> barChartFXList = createBarCharts(reader, executionTimes);
        addChartsToTree(barChartFXList);
    }

    private float[][] executeQueries(FileReader reader) {
        List<String> csvFileNames = reader.getCsvFileNamesWithoutExtension();
        List<String> queries = reader.getQueries();
        QueryExecutor executor = new QueryExecutor(csvFileNames, queries);
        try {
            executor.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return executor.getExecutionTimes();
    }

    private List<BarChartFX> createBarCharts(FileReader reader, float[][] executionTimes) {
        List<String> csvFileNames = reader.getCsvFileNamesWithoutExtension();
        List<String> queries = reader.getQueries();
        List<String> queryLabels = IntStream.range(0, queries.size())
                .mapToObj(value -> "Query " + (value + 1))
                .toList();
        ChartFactory factory = new ChartFactory(csvFileNames, queryLabels, executionTimes);
        List<BarChartFX> barChartFXList = new ArrayList<>();
        barChartFXList.add(factory.createGeneralBarChart());
        barChartFXList.addAll(factory.createBarChartsOnTableSize());
        barChartFXList.addAll(factory.createBarChartsOnQuery());
        return barChartFXList;
    }

    private void addChartsToTree(List<BarChartFX> barChartFXList) {
        TreeItem<String> rootItem = new TreeItem<>("Bar Charts");
        rootItem.setExpanded(true);
        barChartFXList.forEach(barChartFX -> {
            TreeItem<String> item = new TreeItem<>(barChartFX.getTitle());
            rootItem.getChildren().add(item);
        });
        chartTree.setRoot(rootItem);

        chartTree.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {
            Optional<BarChartFX> selected = barChartFXList.stream()
                    .filter(barChartFX -> barChartFX.getTitle().equals(t1.getValue()))
                    .findFirst();

            if (selected.isPresent()) {
                BarChartFX barChartFX = selected.get();
                chartContainer.setCenter(barChartFX.getBarChart());
            }
        });

        chartTree.getSelectionModel().select(1);
    }
}
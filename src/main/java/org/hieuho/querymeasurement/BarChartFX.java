package org.hieuho.querymeasurement;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BarChartFX {

    private final String title;
    private final BarChart<String, Number> barChart;

    public BarChartFX(String title, String xAxisLabel, String yAxisLabel) {
        this.title = title;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xAxisLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);

        this.barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
    }

    public BarChartFX(String title, String xAxisLabel, String yAxisLabel, List<XYChart.Series<String, Number>> seriesList) {
        this(title, xAxisLabel, yAxisLabel);
        barChart.getData().addAll(seriesList);
    }

    public BarChartFX(String title, String xAxisLabel, String yAxisLabel, XYChart.Series<String, Number> series) {
        this(title, xAxisLabel, yAxisLabel);
        barChart.getData().add(series);
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    public String getTitle() {
        return title;
    }

    public void applyStyle() {
        barChart.getStylesheets().add(ClassLoader.getSystemResource("style.css").toExternalForm());
        barChart.setLegendVisible(false);
    }
}

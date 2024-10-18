package org.hieuho.querymeasurement;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class ChartFactory {
    List<String> sizeLabels;
    List<String> queryLabels;
    float[][] data;

    public ChartFactory(List<String> sizeLabels, List<String> queryLabels, float[][] data) {
        this.sizeLabels = sizeLabels;
        this.queryLabels = queryLabels;
        this.data = data;
    }

    public BarChartFX createGeneralBarChart() {
        int rows = data.length;
        int cols = data[0].length;

        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName(sizeLabels.get(i));
            for (int j = 0; j < cols; j++) {
                series.getData().add(new XYChart.Data<>(queryLabels.get(j), data[i][j]));
            }
            seriesList.add(series);
        }

        return new BarChartFX("Queries performance on different table sizes", "Query", "Execution time (ms)", seriesList);
    }

    public List<BarChartFX> createBarChartsOnTableSize() {
        List<BarChartFX> barChartFXList = new ArrayList<>();
        int rows = data.length;
        int cols = data[0].length;
        for (int i = 0; i < rows; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName(sizeLabels.get(i));
            for (int j = 0; j < cols; j++) {
                series.getData().add(new XYChart.Data<>(queryLabels.get(j), data[i][j]));
            }
            String title = String.format("Queries performance on %s table", sizeLabels.get(i));
            BarChartFX barChartFX = new BarChartFX(title, "Query", "Execution time (ms)", series);
            barChartFX.applyStyle();
            barChartFXList.add(barChartFX);
        }
        return barChartFXList;
    }

    public List<BarChartFX> createBarChartsOnQuery() {
        List<BarChartFX> barChartFXList = new ArrayList<>();
        int rows = data.length;
        int cols = data[0].length;
        for (int i = 0; i < cols; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName(queryLabels.get(i));
            for (int j = 0; j < rows; j++) {
                series.getData().add(new XYChart.Data<>(sizeLabels.get(j), data[j][i]));
            }
            String title = String.format("%s performance on different table sizes", queryLabels.get(i));
            BarChartFX barChartFX = new BarChartFX(title, "Table size", "Execution time (ms)", series);
            barChartFX.applyStyle();
            barChartFXList.add(barChartFX);
        }
        return barChartFXList;
    }
}

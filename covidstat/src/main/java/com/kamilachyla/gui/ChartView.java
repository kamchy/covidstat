package com.kamilachyla.gui;

import com.kamilachyla.model.Case;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum CASETYPE {
    ACTIVE, DEATHS, CONFIRMED, RECOVERED;

    public Integer extract(Case c) {
        return switch (this) {
            case ACTIVE -> c.active();
            case DEATHS -> c.deaths();
            case CONFIRMED -> c.confirmed();
            case RECOVERED -> c.recovered();
        };
    }
}

public class ChartView extends Parent {
    private final CategoryAxis xa = new CategoryAxis();
    private final NumberAxis ya = new NumberAxis();
    private final LineChart<String, Integer> lineChart = new LineChart(xa, ya);


    public ChartView(SimpleListProperty<Case> cases) {
        super();
        xa.setLabel("Dates");
        ya.setLabel("Cases");
        lineChart.setTitle("Cases");
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        cases.addListener((o, ov, nv) -> {
            setSeries(nv);
        });

        this.getChildren().add(lineChart);
    }

    public void setSeries(List<Case> cases) {
        updateSeriesWith(cases);
    }

    private void updateSeriesWith(List<Case> cases) {
        ObservableList<XYChart.Series<String, Integer>> seriesCollection = FXCollections.observableArrayList();
        Arrays.asList(CASETYPE.values()).forEach(ct ->{
            var dataForCasetype = cases.stream()
                    .map(c -> new XYChart.Data<>(c.date().toString(), ct.extract(c)))
                    .collect(Collectors.toList());
            XYChart.Series<String, Integer> singleSeries = new XYChart.Series<>(FXCollections.observableArrayList(dataForCasetype));
            singleSeries.setName(ct.name());
            seriesCollection.add(singleSeries);
        });
        lineChart.setData(seriesCollection);
    }
}

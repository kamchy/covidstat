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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<CASETYPE, ObservableList<XYChart.Data<String, Integer>>> oLists = new HashMap<>();
    private final LineChart lineChart = new LineChart(xa, ya);


    public ChartView(SimpleListProperty<Case> cases) {
        super();
        xa.setLabel("Dates");
        ya.setLabel("Cases");
        lineChart.setTitle("Cases");
        ObservableList<XYChart.Series<String, Integer>> observableSeries = FXCollections.observableArrayList();
        for (CASETYPE name : CASETYPE.values()){
            ObservableList<XYChart.Data<String, Integer>> seriesList = FXCollections.observableArrayList();
            oLists.put(name, seriesList);
            XYChart.Series<String, Integer> series = new XYChart.Series<>(seriesList);
            series.setName(name.toString());
            observableSeries.add(series);
        }
        lineChart.setData(observableSeries);
        lineChart.setAnimated(false);

        cases.addListener((o, ov, nv) -> {
            setSeries(nv);
        });

        this.getChildren().add(lineChart);
    }

    public void setSeries(List<Case> cases) {
        updateSeriesWith(cases);
    }

    private void updateSeriesWith(List<Case> cases) {
        Arrays.asList(CASETYPE.values()).forEach(ct ->{
            oLists.get(ct).setAll(cases.stream()
                    .map(c -> new XYChart.Data<>(c.date().toString(), ct.extract(c)))
                    .collect(Collectors.toList()));
        });
    }


}

package com.kamilachyla.gui;

import com.kamilachyla.model.Country;
import com.kamilachyla.viewmodel.CovidViewModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;

public class CovidView extends VBox {
    private final GridPane gp = new GridPane();
    private final CovidViewModel viewModel;

    private final ListView<Country> listView = new ListView<>();
    private final Label laConfirmed = new Label();
    private final Label laDeaths = new Label();
    private final Label laActive = new Label();
    private final Label laRecovered = new Label();
    private final Label laDate = new Label();
    private ChartView chartView;

    public CovidView(CovidViewModel viewModel) {
        this.viewModel = viewModel;
        createView();
        bindViewModel();
        Platform.runLater(() -> {
            listView.getSelectionModel().selectFirst();
        });

    }

    private void bindViewModel() {

        listView.setEditable(false);
        listView.itemsProperty().bind(viewModel.countriesProperty());
        listView.setCellFactory(new CountryCellFactory());


        listView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv)-> {
            viewModel.update(nv);
        });

        laConfirmed.textProperty().bindBidirectional(viewModel.confirmedProperty(), new NumberStringConverter());
        laDeaths.textProperty().bindBidirectional(viewModel.deathsProperty(), new NumberStringConverter());
        laActive.textProperty().bindBidirectional(viewModel.activeProperty(), new NumberStringConverter());
        laRecovered.textProperty().bindBidirectional(viewModel.recoveredProperty(), new NumberStringConverter());
        laDate.textProperty().bindBidirectional(viewModel.getDateProperty(), new LocalDateConverter());



    }

    private void createView() {
        var gpwrap = new VBox();
        chartView = new ChartView(viewModel.selectedCountryCasesProperty());
        gpwrap.setAlignment(Pos.CENTER);

        gp.setPadding(new Insets(20));
        gp.setVgap(4);
        gp.setHgap(10);

        gp.add(listView, 0, 0, 1, 6);

        addRow(1,0, "Confirmed", laConfirmed);
        addRow(1, 1, "Deaths", laDeaths);
        addRow(1, 2, "Active", laActive);
        addRow(1,3, "Recovered", laRecovered);
        addRow(1,4, "Date", laDate);
        gp.add(chartView, 1, 5, 3, 1);


        final ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(30);
        final ColumnConstraints chartCol = new ColumnConstraints();
        col.setHgrow(Priority.NEVER);
        chartCol.setHgrow(Priority.ALWAYS);

        gp.getColumnConstraints().addAll( col, col, col, chartCol);
        gpwrap.getChildren().add( gp );
        VBox.setVgrow(gpwrap, Priority.ALWAYS );

        this.getChildren().add(gpwrap);
    }

    private void addRow(int col, int row, String label, Label display) {
        gp.add(new Label(label), 0 + col, row);
        gp.add(display, 1 + col, row);
    }

    private static class CountryCellFactory implements Callback<ListView<Country>, ListCell<Country>>{
        @Override
        public ListCell<Country> call(ListView<Country> list) {
            return new ListCell<>() {
                @Override
                protected void updateItem(Country country, boolean b) {
                    super.updateItem(country, b);
                    if (country != null) {
                        setText(String.format("%s [%s]", country.name(), country.iso2()));
                    }
                }
            };
        }
    }

    private static class LocalDateConverter extends StringConverter<LocalDate> {
        @Override
        public String toString(LocalDate localDate) {
            return localDate.toString();
        }

        @Override
        public LocalDate fromString(String s) {
            return LocalDate.parse(s);
        }
}
}

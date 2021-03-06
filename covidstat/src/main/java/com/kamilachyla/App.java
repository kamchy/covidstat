package com.kamilachyla;

import com.kamilachyla.gui.CovidView;
import com.kamilachyla.service.NetworkService;
import com.kamilachyla.viewmodel.CovidViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var viewModel = new CovidViewModel(new NetworkService());
        var scene = new Scene(new CovidView(viewModel), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Covid statistics per country");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
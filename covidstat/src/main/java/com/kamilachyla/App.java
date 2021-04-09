package com.kamilachyla;

import com.kamilachyla.gui.CovidView;
import com.kamilachyla.service.HandmadeService;
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
        var viewModel = new CovidViewModel(new HandmadeService());
        var scene = new Scene(new CovidView(viewModel), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Covid statistics per country");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
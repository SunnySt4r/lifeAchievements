package com.github.sunnyst4r.lifeachievements;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LifeAchievementsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LifeAchievementsApplication.class.getResource("mainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Life Achievements");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
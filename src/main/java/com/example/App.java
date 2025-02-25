package com.example;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App extends Application {

  private static App instance;
  private static final Logger logger = LogManager.getLogger(App.class);
  private Stage stage;
  public static void main(String[] args){
    logger.info("Launching app");
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    instance = this;
    this.stage = stage;
    StackPane root = new StackPane();
    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.show();

  }

  public static App getInstance() {
    return instance;
  }

}

package com.example;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App extends Application {
  private static App instance;
  static final Logger logger = LogManager.getLogger(App.class);
  private Stage stage;
  private User loggedInUser;
  /**
   * Main method to enter the app.
   * @param args
   */

  public static void main(String[] args){
    logger.info("Launching app");
    launch();
  }

  /**
   * Start method called when JavaFX is launched.
   */

  @Override
  public void start(Stage stage) throws Exception {
    instance = this;
    this.stage = stage;
    stage.setTitle("Login");
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setWidth(screenBounds.getWidth());
    stage.setHeight(screenBounds.getHeight());
    stage.setFullScreen(true);
    stage.show();
    logger.info("App started");
    Login login = new Login(stage);
    login.show();
    logger.info("Login completed");
  }

  /**
   * Display input files page after login.
   */

  public void showInputFilesPage(){
    InputFilesPage inputFilesPage = new InputFilesPage(stage);
    inputFilesPage.show();
    logger.info("Input files page");
  }

  /**
   * A getter method to globally access the app instance.
   */
  public static App getInstance() {
    return instance;
  }

  public void setLoggedInUser(User user) {
    this.loggedInUser = user;
  }

  public User getLoggedInUser() {
    return loggedInUser;
  }

}

package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SettingsPage {

    private Stage stage;
    private String previousPage;
    private PageInfo pageInfo;
    private boolean darkMode;

    private final String BACKGROUND_COLOR = "#f5f5f7";
    private final String PRIMARY_COLOR = "#4285F4";
    private final String PRIMARY_DARK_COLOR = "#3367d6";
    private final String SECTION_BACKGROUND = "white";
    private final String HEADER_COLOR = "#333333";
    private final String TEXT_COLOR = "#555555";
    private final String LOGOUT_COLOUR = "#ff0000";
    private final String LOGOUT_HOVER_COLOUR = "#8b0000";
    private final String SETTINGS = "#888888";
    private final String SETTINGS_HOVER = "#555555";
    private final String DARKMODE_SECTION = "#2b2b2b";
    private final String DARKMODE_BACKGROUND = "#1f1f1f";
    private final String DARKMODE_TEXT = "#fafafa";

    public SettingsPage(Stage stage,String previousPage, PageInfo pageInfo,boolean darkMode){
        this.stage = stage;
        this.previousPage = previousPage;
        this.pageInfo = pageInfo;
        this.darkMode = darkMode;
    }

    public void show() {
        BorderPane root = new BorderPane();
        if (!darkMode) {
            root.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            root.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        HBox navBar = createNavigationBar();
        HBox settingsBar = createInputBar();
        HBox logoutBar = createLogoutBar();

        root.setTop(navBar);
        root.setCenter(settingsBar);
        root.setBottom(logoutBar);

        BorderPane.setMargin(settingsBar, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(logoutBar, new Insets(0, 0, 30, 0));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("Settings");
        stage.show();
    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            navBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            navBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label title = new Label("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);

        Button backButton = createBackButton();

        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer,backButton);

        return navBar;
    }

    private HBox createInputBar() {
        //Main container
        HBox inputBar = new HBox(15);
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setPadding(new Insets(15, 20, 15, 20));


        // Dark mode container
        VBox darkModeBar = new VBox(15);
        darkModeBar.setAlignment(Pos.CENTER);
        darkModeBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            darkModeBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            darkModeBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label selectTheme = new Label("Select theme");
        selectTheme.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        selectTheme.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : "#333333"));

        ToggleGroup themeToggle = new ToggleGroup();
        RadioButton lightButton = createStyledRadioButton("Light Mode",themeToggle,darkMode);
        lightButton.setOnAction(e -> {
            darkMode = false;
        });
        RadioButton darkButton = createStyledRadioButton("Dark Mode",themeToggle,darkMode);
        darkButton.setOnAction(e -> {
            darkMode = true;
        });

        if (darkMode) {
            darkButton.setSelected(true);
        } else {
            lightButton.setSelected(true);
        }

        darkModeBar.getChildren().addAll(selectTheme,lightButton,darkButton);

        // Help container
        VBox helpBar = new VBox(15);
        helpBar.setAlignment(Pos.CENTER);
        helpBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            helpBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            helpBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        Label instruction = new Label("How to use system");
        instruction.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        instruction.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : "#333333"));
        Button helpButton = createStyledButton("Help",PRIMARY_COLOR,PRIMARY_DARK_COLOR);

        helpBar.getChildren().addAll(instruction,helpButton);

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer,Priority.ALWAYS);
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer,Priority.ALWAYS);
        Region endSpacer = new Region();
        HBox.setHgrow(endSpacer,Priority.ALWAYS);

        inputBar.getChildren().addAll(leftSpacer,darkModeBar,rightSpacer,helpBar,endSpacer);


        return inputBar;
    }

    private HBox createLogoutBar() {
        HBox logoutBar = new HBox(15);
        logoutBar.setAlignment(Pos.CENTER_LEFT);
        logoutBar.setPadding(new Insets(15, 20, 15, 20));
        if (!darkMode) {
            logoutBar.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            logoutBar.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }


        //Button to logout
        Button logoutButton = createStyledButton("Logout",LOGOUT_COLOUR,LOGOUT_HOVER_COLOUR);
        logoutButton.setOnAction(e -> {
            Login loginPage = new Login(stage,darkMode);
            loginPage.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Add to navigation bar
        logoutBar.getChildren().addAll(spacer,logoutButton);

        return logoutBar;
    }

    public Button createStyledButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefSize(140, 35);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        String style = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: normal; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1); " +
                        "-fx-cursor: hand;", bgColor);

        String hoverStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: normal; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2); " +
                        "-fx-cursor: hand;", hoverColor);

        button.setStyle(style);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));

        return button;
    }

    private RadioButton createStyledRadioButton(String text, ToggleGroup group,boolean darkMode) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 13));
        radioButton.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : TEXT_COLOR));
        return radioButton;
    }

    private Button createBackButton(){
        Button backButton = createStyledButton("Back",SETTINGS,SETTINGS_HOVER);
        backButton.setOnAction(e -> {
            switch (previousPage) {
                case "Login":
                    Login login = new Login(stage,darkMode);
                    login.show();
                    break;

                case "AdminLogin":
                    AdminLogin adminLogin = new AdminLogin(stage, pageInfo.onSuccess,darkMode);
                    adminLogin.show();
                    break;

                case "Chart":
                    ChartPage chartPage = new ChartPage(stage, pageInfo.logManager,pageInfo.chartCreator,darkMode);
                    chartPage.show();
                    break;

                case "Input":
                    InputFilesPage inputFilesPage = new InputFilesPage(stage,darkMode);
                    inputFilesPage.show();
                    break;

                case "MultiChart":
                    MultiChartPage multiChartPage = new MultiChartPage(stage, pageInfo.logManager, pageInfo.currentCharts,
                            pageInfo.timeFlags,pageInfo.genders,pageInfo.incomes,pageInfo.contexts,pageInfo.ages,
                            pageInfo.chartCreator,darkMode);
                    multiChartPage.show();
                    break;

                case "Edit":
                    EditPage editPage = new EditPage(stage, pageInfo.logManager, pageInfo.currentCharts,
                            pageInfo.timeFlags,pageInfo.genders,pageInfo.incomes,pageInfo.contexts,pageInfo.ages,
                            pageInfo.chartNumber,pageInfo.chartCreator, darkMode);
                    editPage.show();
                    break;

                case "Overall":
                    OverallMetricsPage overallMetricsPage = new OverallMetricsPage(stage, pageInfo.logManager,pageInfo.chartCreator,darkMode);
                    overallMetricsPage.show();
                    break;

                case "Bounce":
                    BouncePage bouncePage = new BouncePage(stage, pageInfo.logManager,pageInfo.chartCreator,darkMode);
                    bouncePage.show();
                    break;

                case "Manage":
                    UserManagementPage userManagementPage = new UserManagementPage(stage,pageInfo.loggedInUser,darkMode);
                    userManagementPage.show();
                    break;

                case "Histogram":
                    HistogramChart histogramChart = new HistogramChart(stage, pageInfo.logManager, pageInfo.chartCreator, darkMode);
                    histogramChart.show();
                    break;
            }
        });

        return backButton;
    }
}

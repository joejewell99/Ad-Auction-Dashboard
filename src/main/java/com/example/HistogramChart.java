package com.example;

import com.itextpdf.text.DocumentException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;


import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistogramChart {
    private Stage Stage;

    private boolean darkMode;
    private LogManager logManager;
    private ChartCreator chartCreator;
    private ChartViewer chartViewer;

    private String gender;
    private ArrayList<String> age;
    private String income;
    private ArrayList<String> context;





    // Define style constants
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

    public HistogramChart(Stage stage,LogManager logManager, ChartCreator chartCreator, boolean darkMode) {
        this.Stage = stage;
        this.logManager = logManager;
        this.chartCreator = chartCreator;
        this.darkMode = darkMode;

        this.gender = "";
        this.age = new ArrayList<>();
        this.income = "";
        this.context = new ArrayList<>();
    }

    public void show() {
        BorderPane root = new BorderPane();
        if (!darkMode) {
            root.setStyle("-fx-background-color: #f5f5f7;");
        } else {
            root.setStyle("-fx-background-color: " + DARKMODE_BACKGROUND);
        }

        // Create top navigation bar
        HBox navBar = createNavigationBar();

        // Create chart area
        HBox chartSection = createChartSection();

        //Scroll pane
        ScrollPane filters = createFilterPanel(chartViewer);

        //Logout box
        HBox logoutBox = createLogoutBar();

        // Set layout
        root.setTop(navBar);
        root.setCenter(chartSection);
        root.setRight(filters);
        root.setBottom(logoutBox);

        // Set margins
        BorderPane.setMargin(chartSection, new Insets(20, 20, 20, 20));
        BorderPane.setMargin(logoutBox,new Insets(20,20,20,20));
        BorderPane.setMargin(filters, new Insets(10, 10, 10, 5));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        Stage.setScene(scene);
        Stage.setFullScreen(true);
        Stage.setTitle("Ad Auction Dashboard - Click-Histogram View");
        Stage.show();

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

        Label title = new Label("Click Cost Distribution");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : HEADER_COLOR));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button backButton = createStyledButton("Back to Charts", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        Button saveToPdfButton = createStyledButton("Save To Pdf", PRIMARY_COLOR, PRIMARY_DARK_COLOR);
        //Button logOutButton = createStyledButton("Logout", "#757575", "#616161");

        Button settingsButton = createStyledButton("Settings", SETTINGS,SETTINGS_HOVER);
        settingsButton.setOnAction(e -> {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setDarkMode(darkMode);
            pageInfo.setChartCreator(chartCreator);
            pageInfo.setLogManager(logManager);
            SettingsPage settingsPage = new SettingsPage(Stage,"Histogram",pageInfo,darkMode);
            settingsPage.show();
        });

        // Set button events
        backButton.setOnAction(e -> {
            ChartPage chartPage = new ChartPage(Stage,logManager,chartCreator,darkMode);
            chartPage.show();
        });

        saveToPdfButton.setOnAction(e -> {
            try {
                // Convert JavaFX Chart to image
                WritableImage writableImage = chartViewer.snapshot(null, null);

                // Save image as PDF
                chartCreator.saveChartAsPdf(writableImage,Stage);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add to navigation bar
        navBar.getChildren().addAll(title,spacer,backButton,saveToPdfButton,settingsButton);

        return navBar;
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
            Login loginPage = new Login(Stage,darkMode);
            loginPage.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Add to navigation bar
        logoutBar.getChildren().addAll(spacer,logoutButton);

        return logoutBar;
    }

    private HBox createChartSection() {
        HBox chartSection = new HBox(20);
        chartSection.setAlignment(Pos.CENTER);

        JFreeChart histogram = chartCreator.genClickCostHistogram(gender, income, context, age);
        chartViewer = new ChartViewer(histogram);
        chartViewer.setPrefSize(800, 600);
        chartViewer.setMaxSize(800, 600);
        chartViewer.setMinSize(800, 600);

        chartSection.getChildren().addAll(chartViewer);

        return chartSection;
    }



    private ScrollPane createFilterPanel(ChartViewer chartViewer) {
        VBox filterPanel = new VBox(20);
        filterPanel.setPadding(new Insets(20));
        if (!darkMode) {
            filterPanel.setStyle("-fx-background-color: " + SECTION_BACKGROUND + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            filterPanel.setStyle("-fx-background-color: " + DARKMODE_SECTION + "; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        // Title for filter panel
        Label filterTitle = new Label("Chart Settings");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        filterTitle.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        // Add different filter sections
        VBox genderSection = createGenderOptions(chartViewer);
        VBox incomeSection = createIncomeOptions(chartViewer);
        VBox contextSection = createContextOptions(chartViewer);
        VBox ageSection = createAgeOptions(chartViewer);

        // Add sections to filter panel
        filterPanel.getChildren().addAll(
                filterTitle,
                new Separator(),
                genderSection,
                new Separator(),
                incomeSection,
                new Separator(),
                contextSection,
                new Separator(),
                ageSection
        );

        // Create a scroll pane to hold the filter panel
        ScrollPane scrollPane = new ScrollPane(filterPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(300);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("edge-to-edge");

        return scrollPane;
    }

    private VBox createGenderOptions(ChartViewer chartViewer) {
        VBox genderBox = new VBox(10);

        Label genderLabel = new Label("Gender");
        genderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        genderLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        ToggleGroup genderToggleGroup = new ToggleGroup();

        RadioButton allGenderButton = createStyledRadioButton("All", genderToggleGroup,darkMode);
        allGenderButton.setSelected(true);

        RadioButton maleButton = createStyledRadioButton("Male", genderToggleGroup,darkMode);
        RadioButton femaleButton = createStyledRadioButton("Female", genderToggleGroup,darkMode);

        // Set action handlers for gender buttons
        allGenderButton.setOnAction(e -> {
            gender = "";
            updateChart(chartViewer);
        });

        maleButton.setOnAction(e -> {
            gender = "Male";
            updateChart(chartViewer);
        });

        femaleButton.setOnAction(e -> {
            gender = "Female";
            updateChart(chartViewer);
        });

        genderBox.getChildren().addAll(genderLabel, allGenderButton, maleButton, femaleButton);

        return genderBox;
    }

    private VBox createIncomeOptions(ChartViewer chartViewer) {
        VBox incomeBox = new VBox(10);

        Label incomeLabel = new Label("Income");
        incomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        incomeLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        ToggleGroup incomeToggleGroup = new ToggleGroup();

        RadioButton allIncomeButton = createStyledRadioButton("All", incomeToggleGroup,darkMode);
        allIncomeButton.setSelected(true);

        RadioButton lowButton = createStyledRadioButton("Low", incomeToggleGroup,darkMode);
        RadioButton mediumButton = createStyledRadioButton("Medium", incomeToggleGroup,darkMode);
        RadioButton highButton = createStyledRadioButton("High", incomeToggleGroup,darkMode);

        // Set action handlers for income buttons
        allIncomeButton.setOnAction(e -> {
            income = "";
            updateChart(chartViewer);
        });

        lowButton.setOnAction(e -> {
            income = "Low";
            updateChart(chartViewer);
        });

        mediumButton.setOnAction(e -> {
            income = "Medium";
            updateChart(chartViewer);
        });

        highButton.setOnAction(e -> {
            income = "High";
            updateChart(chartViewer);
        });

        incomeBox.getChildren().addAll(incomeLabel, allIncomeButton, lowButton, mediumButton, highButton);

        return incomeBox;
    }


    private VBox createContextOptions(ChartViewer chartViewer) {
        VBox contextBox = new VBox(10);

        Label contextLabel = new Label("Context");
        contextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        contextLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        CheckBox newsCheck = createStyledCheckBox("News",darkMode);
        CheckBox shoppingCheck = createStyledCheckBox("Shopping",darkMode);
        CheckBox socialMediaCheck = createStyledCheckBox("Social Media",darkMode);
        CheckBox blogCheck = createStyledCheckBox("Blog",darkMode);
        CheckBox hobbyCheck = createStyledCheckBox("Hobby",darkMode);
        CheckBox travelCheck = createStyledCheckBox("Travel",darkMode);

        // Button to clear all context filters
        Button clearContextButton = new Button("Clear All");
        clearContextButton.setFont(Font.font("Arial", 12));
        clearContextButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333;");
        clearContextButton.setOnAction(e -> {
            newsCheck.setSelected(false);
            shoppingCheck.setSelected(false);
            socialMediaCheck.setSelected(false);
            blogCheck.setSelected(false);
            hobbyCheck.setSelected(false);
            travelCheck.setSelected(false);
            context.clear();
            updateChart(chartViewer);
        });

        // Set action handlers for context checkboxes
        newsCheck.setOnAction(e -> {
            if (newsCheck.isSelected()) {
                context.add("News");
            } else {
                context.remove("News");
            }
            updateChart(chartViewer);
        });

        shoppingCheck.setOnAction(e -> {
            if (shoppingCheck.isSelected()) {
                context.add("Shopping");
            } else {
                context.remove("Shopping");
            }
            updateChart(chartViewer);
        });

        socialMediaCheck.setOnAction(e -> {
            if (socialMediaCheck.isSelected()) {
                context.add("Social Media");
            } else {
                context.remove("Social Media");
            }
            updateChart(chartViewer);
        });

        blogCheck.setOnAction(e -> {
            if (blogCheck.isSelected()) {
                context.add("Blog");
            } else {
                context.remove("Blog");
            }
            updateChart(chartViewer);
        });

        hobbyCheck.setOnAction(e -> {
            if (hobbyCheck.isSelected()) {
                context.add("Hobby");
            } else {
                context.remove("Hobby");
            }
            updateChart(chartViewer);
        });

        travelCheck.setOnAction(e -> {
            if (travelCheck.isSelected()) {
                context.add("Travel");
            } else {
                context.remove("Travel");
            }
            updateChart(chartViewer);
        });

        HBox clearButtonContainer = new HBox();
        clearButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        clearButtonContainer.getChildren().add(clearContextButton);
        clearButtonContainer.setPadding(new Insets(5, 0, 0, 0));

        contextBox.getChildren().addAll(
                contextLabel,
                newsCheck,
                shoppingCheck,
                socialMediaCheck,
                blogCheck,
                hobbyCheck,
                travelCheck,
                clearButtonContainer
        );

        return contextBox;
    }

    private VBox createAgeOptions(ChartViewer chartViewer) {
        VBox ageBox = new VBox(10);

        Label ageLabel = new Label("Age");
        ageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ageLabel.setTextFill(Color.web(darkMode ? DARKMODE_TEXT :HEADER_COLOR));

        CheckBox under25Check = createStyledCheckBox("Under 25",darkMode);
        CheckBox age25to34Check = createStyledCheckBox("25-34",darkMode);
        CheckBox age35to44Check = createStyledCheckBox("35-44",darkMode);
        CheckBox age45to54Check = createStyledCheckBox("45-54",darkMode);
        CheckBox over54Check = createStyledCheckBox("Over 54",darkMode);

        // Button to clear all age filters
        Button clearAgeButton = new Button("Clear All");
        clearAgeButton.setFont(Font.font("Arial", 12));
        clearAgeButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333;");
        clearAgeButton.setOnAction(e -> {
            under25Check.setSelected(false);
            age25to34Check.setSelected(false);
            age35to44Check.setSelected(false);
            age45to54Check.setSelected(false);
            over54Check.setSelected(false);
            age.clear();
            updateChart(chartViewer);
        });

        // Set action handlers for age checkboxes
        under25Check.setOnAction(e -> {
            if (under25Check.isSelected()) {
                age.add("<25");
            } else {
                age.remove("<25");
            }
            updateChart(chartViewer);
        });

        age25to34Check.setOnAction(e -> {
            if (age25to34Check.isSelected()) {
                age.add("25-34");
            } else {
                age.remove("25-34");
            }
            updateChart(chartViewer);
        });

        age35to44Check.setOnAction(e -> {
            if (age35to44Check.isSelected()) {
                age.add("35-44");
            } else {
                age.remove("35-44");
            }
            updateChart(chartViewer);
        });

        age45to54Check.setOnAction(e -> {
            if (age45to54Check.isSelected()) {
                age.add("45-54");
            } else {
                age.remove("45-54");
            }
            updateChart(chartViewer);
        });

        over54Check.setOnAction(e -> {
            if (over54Check.isSelected()) {
                age.add(">54");
            } else {
                age.remove(">54");
            }
            updateChart(chartViewer);
        });

        HBox clearButtonContainer = new HBox();
        clearButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        clearButtonContainer.getChildren().add(clearAgeButton);
        clearButtonContainer.setPadding(new Insets(5, 0, 0, 0));

        ageBox.getChildren().addAll(
                ageLabel,
                under25Check,
                age25to34Check,
                age35to44Check,
                age45to54Check,
                over54Check,
                clearButtonContainer
        );

        return ageBox;
    }

    public void updateChart(ChartViewer chartViewer){
        JFreeChart histogram = chartCreator.genClickCostHistogram(gender,income,context,age);
        chartViewer.setChart(histogram);
    }


    private CheckBox createStyledCheckBox(String text,boolean darkMode) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setFont(Font.font("Arial", 13));
        checkBox.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : TEXT_COLOR));
        return checkBox;
    }

    private RadioButton createStyledRadioButton(String text, ToggleGroup group,boolean darkMode) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 13));
        radioButton.setTextFill(Color.web(darkMode ? DARKMODE_TEXT : TEXT_COLOR));
        return radioButton;
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

}

package dev.gabriel.schedulingapplication;

import dev.gabriel.schedulingapplication.dbhelper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageBundle/Nat", locale);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dev/gabriel/schedulingapplication/Views/log-in-view.fxml"));
            fxmlLoader.setResources(resourceBundle);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchScreen(String fxmlFile, Node node) throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}
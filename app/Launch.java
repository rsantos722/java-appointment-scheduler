package C195RafaelSantos;

import C195RafaelSantos.Data.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//TODO JavaDocs
//TODO Code Cleanup

/**
 * <p>
 * Launches the program, and calls the Database connnection and Login controller
 * @author Rafael Santos
 * </p>
 */
public class Launch extends Application {

    public static void main(String[] args) {
        //Start DB Connection before launch
        DatabaseConnection.startConnection();
        launch(args);

        //Close DB Connection on close
        DatabaseConnection.closeConnection();
    }


    @Override
    public void start(Stage stage) throws Exception {

        //Login Page Loader
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login-C195 Project by Rafael Santos");
        stage.show();



    }
}

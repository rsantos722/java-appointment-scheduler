package C195RafaelSantos.View;

import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.ReportModel.AmountByLocation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationReportController {

    @FXML // fx:id="locationReportTable"
    private TableView<AmountByLocation> locationReportTable; // Value injected by FXMLLoader

    @FXML // fx:id="locationColumn"
    private TableColumn<AmountByLocation, String> locationColumn; // Value injected by FXMLLoader

    @FXML // fx:id="amountColumn"
    private TableColumn<AmountByLocation, Integer> amountColumn; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void exitButtonAction() {
        //Close window
        Stage stage = (Stage) locationReportTable.getScene().getWindow();
        stage.close();

    }

    @FXML
    void locationReportTableAction() {

    }

    /**
     * <p>
     * Initializes the report, fetching data from the AmountByLocation
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws SQLException {
        assert locationReportTable != null : "fx:id=\"locationReportTable\" was not injected: check your FXML file 'LocationReport.fxml'.";
        assert locationColumn != null : "fx:id=\"locationColumn\" was not injected: check your FXML file 'LocationReport.fxml'.";
        assert amountColumn != null : "fx:id=\"amountColumn\" was not injected: check your FXML file 'LocationReport.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'LocationReport.fxml'.";

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        amountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAmount()).asObject());

        ObservableList<AmountByLocation> amountByLocationObservableList = FXCollections.observableArrayList();
        ResultSet locationResultSet = appointmentDAO.getAmountByLocation();
        while(locationResultSet.next()) {
            AmountByLocation a = new AmountByLocation();
            a.setLocation(locationResultSet.getString("Location"));
            a.setAmount(locationResultSet.getInt("Amount"));
            amountByLocationObservableList.add(a);
        }

        locationReportTable.setItems(amountByLocationObservableList);
    }
}

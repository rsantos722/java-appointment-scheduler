package C195RafaelSantos.View;

import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.ReportModel.AmountByMonth;
import C195RafaelSantos.ReportModel.AmountByType;
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

/**
 * <p>
 * Displays two tables, one with Appointments grouped by month, and another grouped by type.
 * @author Rafael Santos
 * </p>
 */
public class AppointmentReportController {

    @FXML // fx:id="byTypeTable"
    private TableView<AmountByType> byTypeTable; // Value injected by FXMLLoader
    @FXML // fx:id="byTypeTypeColumn"
    private TableColumn<AmountByType, String> byTypeTypeColumn; // Value injected by FXMLLoader
    @FXML // fx:id="byTypeAmountColumn"
    private TableColumn<AmountByType, Integer> byTypeAmountColumn; // Value injected by FXMLLoader
    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader
    @FXML // fx:id="byMonthTable"
    private TableView<AmountByMonth> byMonthTable; // Value injected by FXMLLoader
    @FXML // fx:id="byMonthMonthColumn"
    private TableColumn<AmountByMonth, String> byMonthMonthColumn; // Value injected by FXMLLoader
    @FXML // fx:id="byMonthAmountColumn"
    private TableColumn<AmountByMonth, Integer> byMonthAmountColumn; // Value injected by FXMLLoader

    @FXML
    void appointmentTableAction() {}
    @FXML
    void byTypeTableAction() {}

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void exitButtonAction() {
        //Close window
        Stage stage = (Stage) byMonthTable.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>
     * Initializes the report.
     * Both tables are populated with data stored in the AmountByMonth and AmountByType ReportModels
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws SQLException, NoSuchFieldException {
        assert byTypeTable != null : "fx:id=\"byTypeTable\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert byTypeTypeColumn != null : "fx:id=\"byTypeTypeColumn\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert byTypeAmountColumn != null : "fx:id=\"byTypeAmountColumn\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert byMonthTable != null : "fx:id=\"byMonthTable\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert byMonthMonthColumn != null : "fx:id=\"byMonthMonthColumn\" was not injected: check your FXML file 'AppointmentReport.fxml'.";
        assert byMonthAmountColumn != null : "fx:id=\"byMonthAmountColumn\" was not injected: check your FXML file 'AppointmentReport.fxml'.";

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        byMonthAmountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAmount()).asObject());
        byMonthMonthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMonth()));

        byTypeTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        byTypeAmountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAmount()).asObject());


        //Setup Type Table
        ObservableList<AmountByType> amountByTypeList = FXCollections.observableArrayList();
        ResultSet typeResultSet = appointmentDAO.getAppointmentsByType();
        while (typeResultSet.next()) {
            AmountByType t = new AmountByType();
            t.setAmount(typeResultSet.getInt("Amount"));
            t.setType(typeResultSet.getString("Type"));
            amountByTypeList.add(t);
        }
        byTypeTable.setItems(amountByTypeList);

        //Setup Month Table
        ObservableList<AmountByMonth> amountByMonthList = FXCollections.observableArrayList();
        ResultSet monthResultSet = appointmentDAO.getAppointmentsByMonth();
        while (monthResultSet.next()) {
            AmountByMonth m = new AmountByMonth();
            m.setAmount(monthResultSet.getInt("Amount"));
            m.setMonth(monthResultSet.getInt("Month"));
            amountByMonthList.add(m);
        }
        byMonthTable.setItems(amountByMonthList);

    }
}

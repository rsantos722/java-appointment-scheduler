package C195RafaelSantos.View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.Data.ContactDAO;
import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import com.sun.javafx.binding.SelectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * <p>
 * Displays a report with a specified contact's schedule
 * Contact can be selected from a combobox, with their appointments displayed in a tableview
 * </p>
 */
public class ScheduleReportController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="contactMenu"
    private ComboBox<String> contactMenu; // Value injected by FXMLLoader

    @FXML // fx:id="appointmentTable"
    private TableView<Appointment> appointmentTable; // Value injected by FXMLLoader

    @FXML // fx:id="appointmentIdColumn"
    private TableColumn<Appointment,Integer> appointmentIdColumn; // Value injected by FXMLLoader

    @FXML // fx:id="titleColumn"
    private TableColumn<Appointment,String> titleColumn; // Value injected by FXMLLoader

    @FXML // fx:id="typeColumn"
    private TableColumn<Appointment,String> typeColumn; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionColumn"
    private TableColumn<Appointment,String> descriptionColumn; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML
    private TableColumn<Appointment,String> dateColumn;

    @FXML
    private TableColumn<Appointment,String> startTimeColumn;

    @FXML
    private TableColumn<Appointment,String> endTimeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIDColumn;

    @FXML
    void appointmentTableAction(ActionEvent event) {


    }

    /**
     * <p>
     * Checks which contact was chosen, and updates the table with their appointments
     * </p>
     */
    @FXML
    void contactMenuAction(ActionEvent event) throws SQLException {


        String contactName = contactMenu.getValue();
        int contactId = SchedulingData.getContactByName(contactName);

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ObservableList<Appointment> appointmentsForContact = appointmentDAO.getAppointmentsByContact(contactId);
        appointmentTable.setItems(appointmentsForContact);



    }

    /**
     * <p>
     * Gets the current window and exits
     * </p>
     */
    @FXML
    void exitButtonAction(ActionEvent event) {
        //Close window
        Stage stage = (Stage) appointmentTable.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>
     * Initializes the window and gets all contacts
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws SQLException {
        assert contactMenu != null : "fx:id=\"contactMenu\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert appointmentTable != null : "fx:id=\"appointmentTable\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert appointmentIdColumn != null : "fx:id=\"appointmentIdColumn\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert titleColumn != null : "fx:id=\"titleColumn\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert typeColumn != null : "fx:id=\"typeColumn\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert descriptionColumn != null : "fx:id=\"descriptionColumn\" was not injected: check your FXML file 'ScheduleReport.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'ScheduleReport.fxml'.";

        ContactDAO contactDAO = new ContactDAO();

        //data -> new SimpleStringProperty(data.getValue().getLocation())

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateString()));
        startTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartTimeString()));
        endTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndTimeString()));
        customerIDColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCustomerId()));

        ObservableList<String> allContactList = contactDAO.readAllContactNames();
        contactMenu.setItems(allContactList);


    }
}

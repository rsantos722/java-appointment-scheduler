package C195RafaelSantos.View;

import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * <p>
 * Appointment window controller
 * Displays Upcoming appointments (within the current week or the next month) based on the selected radiobutton.
 * Appointments can be added or updated with their respective button, or deleted directly from this window using the delete button
 * </p>
 */
public class AppointmentsController {

    static int selectedAppointmentId;

    @FXML // fx:id="appointmentsTable"
    private TableView<Appointment> appointmentsTable; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentIdColumn"
    private TableColumn<Appointment, Integer> appointmentIdColumn; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentTitleColumn"
    private TableColumn<Appointment, String> appointmentTitleColumn; // Value injected by FXMLLoader
    @FXML // fx:id="descriptionColumn"
    private TableColumn<Appointment, String> descriptionColumn; // Value injected by FXMLLoader
    @FXML // fx:id="locationColumn"
    private TableColumn<Appointment, String> locationColumn; // Value injected by FXMLLoader
    @FXML // fx:id="contactColumn"
    private TableColumn<Appointment, String> contactColumn; // Value injected by FXMLLoader
    @FXML // fx:id="typeColumn"
    private TableColumn<Appointment, String> typeColumn; // Value injected by FXMLLoader
    @FXML // fx:id="startTimeColumn"
    private TableColumn<Appointment, String> startTimeColumn; // Value injected by FXMLLoader
    @FXML // fx:id="endTimeColumn"
    private TableColumn<Appointment, String> endTimeColumn; // Value injected by FXMLLoader
    @FXML // fx:id="customerIDColumn"
    private TableColumn<Appointment, Integer> customerIDColumn; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentsThisWeekRadioButton"
    private RadioButton appointmentsThisWeekRadioButton; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentTimeFrameToggleGroup"
    private ToggleGroup appointmentTimeFrameToggleGroup; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentsThisMonth"
    private RadioButton appointmentsThisMonth; // Value injected by FXMLLoader
    @FXML // fx:id="updateAppointmentButton"
    private Button updateAppointmentButton; // Value injected by FXMLLoader
    @FXML // fx:id="addNewAppointmentButton"
    private Button addNewAppointmentButton; // Value injected by FXMLLoader
    @FXML // fx:id="deleteAppointmentButton"
    private Button deleteAppointmentButton; // Value injected by FXMLLoader
    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader
    @FXML // fx:id="dateColumn"
    private TableColumn<Appointment, String> dateColumn; // Value injected by FXMLLoader


    /**
     * <p>
     * Opens the Add Appointment window
     * </p>
     */
    @FXML
    void addNewAppointmentButtonAction() {
        windowLoader("AddAppointment.fxml","Add Appointment");
        appointmentsTable.refresh();
    }

    @FXML
    void appointmentTitleColumnCancelAction() {}
    @FXML
    void appointmentTitleColumnCommitAction() {}
    @FXML
    void appointmentsTableSortAction() {}
    @FXML
    void appointmentsThisMonthRadioButton() {}
    @FXML
    void appointmentsThisWeekRadioButtonAction() {}
    @FXML
    void contactColumnCancelAction() {}
    @FXML
    void contactColumnCommitAction() { }

    /**
     * <p>
     * Checks for which appointment is selected. Then, confirms with the user if they would like to go ahead with the deletion.
     * If they decide to continue with deletion, the selected appointment is deleted from SchedulingData and the DB
     * </p>
     */
    @FXML
    void deleteAppointmentButtonAction() throws SQLException {
        Appointment selectedAppointment  = appointmentsTable.getSelectionModel().getSelectedItem();
        //Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you would like to delete this Appointment? \n The following appointment will be deleted: \n Appointment ID: " + selectedAppointment.getAppointmentId() + "\n Type: " +selectedAppointment.getType(),
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            SchedulingData.deleteAppointment(selectedAppointment);
        }
    }

    @FXML
    void descriptionColumnCancelAction() {}
    @FXML
    void descriptionColumnCommitAction() {}
    @FXML
    void endTimeColumnCancelAction() {}
    @FXML
    void endTimeColumnCommitAction() {}

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void exitButtonAction() {
        //Close window
        Stage stage = (Stage) appointmentsTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    void locationColumnCancelAction() {}
    @FXML
    void locationColumnCommitAction() {}
    @FXML
    void startTimeColumnCommitAction() {}
    @FXML
    void startTimeCommitCancelAction() {}
    @FXML
    void typeColumnCancelAction() {}
    @FXML
    void typeColumnCommitAction() {}

    /**
     * <p>
     * Makes sure an appointment is actually selected, and if so, opens the Update Appointment window
     * </p>
     */
    @FXML
    void updateAppointmentButtonAction() {
        Appointment selectedAppointment  = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert noInputGiven = new Alert(Alert.AlertType.ERROR, "No appointment was selected to update.\n Please choose an appointment to update and try again.");
            noInputGiven.showAndWait();
            return;
        }
        selectedAppointmentId =  appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentId();
        windowLoader("UpdateAppointment.fxml", "Update Appointment");
    }

    /**
     * <p>
     * Initializes the window
     * Entries are loaded from SchedulingData, and the radio buttons for filtering appointments are also set up.
     * Lambda expressions can be found here:
     *
     *
     * Lambda is utilized  to simplify the radio button filter's predicate.
     * The start time of each appointment is verified to be within a certian timeframe.
     * Without the lambda, a  couple of for(if()) loops were used to make sure each condition was met for every appointment. With the lambda operation, it can be simplified to a single line.
     * filteredAppointmentList.setPredicate(appointment -> (appointment.getStartTime().isAfter(ZonedDateTime.now().with(LocalTime.MIN)) && (appointment.getStartTime().isBefore(ZonedDateTime.now().plusWeeks(1)))));
     *
     * Lambda 2
     * This Lambda is used in multiple places to simplify the focus change listener.
     * When a window goes in or out of focus, it is refreshed so it keeps up to date with ObservableList entries
     * While not a complex function to begin with, the Lambda again shortens this to a simple one line statement.
     * appointmentsTable.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> { appointmentsTable.refresh();});
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert dateColumn != null : "fx:id=\"dateColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentsTable != null : "fx:id=\"appointmentsTable\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentIdColumn != null : "fx:id=\"appointmentIdColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentTitleColumn != null : "fx:id=\"appointmentTitleColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert descriptionColumn != null : "fx:id=\"descriptionColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert locationColumn != null : "fx:id=\"locationColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert contactColumn != null : "fx:id=\"contactColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert typeColumn != null : "fx:id=\"typeColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert startTimeColumn != null : "fx:id=\"startTimeColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert endTimeColumn != null : "fx:id=\"endTimeColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert customerIDColumn != null : "fx:id=\"customerIDColumn\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentsThisWeekRadioButton != null : "fx:id=\"appointmentsThisWeekRadioButton\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentTimeFrameToggleGroup != null : "fx:id=\"appointmentTimeFrameToggleGroup\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert appointmentsThisMonth != null : "fx:id=\"appointmentsThisMonth\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert updateAppointmentButton != null : "fx:id=\"updateAppointmentButton\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert addNewAppointmentButton != null : "fx:id=\"addNewAppointmentButton\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert deleteAppointmentButton != null : "fx:id=\"deleteAppointmentButton\" was not injected: check your FXML file 'Appointments.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'Appointments.fxml'.";

        //Setup Table
        appointmentsTable.setEditable(false);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeString"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));

        //FilteredList<Appointment> filteredAppointmentList = new FilteredList<>((appointmentsTable -> true));

        FilteredList<Appointment> filteredAppointmentList = new FilteredList<>(SchedulingData.getAllAppointments());
        SortedList<Appointment> sortedFilteredList = new SortedList<>(filteredAppointmentList);
        sortedFilteredList.comparatorProperty().bind(appointmentsTable.comparatorProperty());
        appointmentsTable.setItems(sortedFilteredList);

        /**
         * <p>
         * @Lambda
         * Lambda's are utilized here to simplify the radio button filter's predicate.
         * The start time of each appointment is verified to be within a certian timeframe.
         * Without the lambda, a  couple of for(if()) loops were used to make sure each condition was met for every appointment. With the lambda operation, it can be simplified to a single line.
         * </p>
         */
        if(appointmentTimeFrameToggleGroup.getSelectedToggle().equals(appointmentsThisWeekRadioButton)) {
            //Within this week
            filteredAppointmentList.setPredicate(
                    appointment -> (appointment.getStartTime().isAfter(ZonedDateTime.now().with(LocalTime.MIN)) && (appointment.getStartTime().isBefore(ZonedDateTime.now().plusWeeks(1)))));
        }
        else if (appointmentTimeFrameToggleGroup.getSelectedToggle().equals(appointmentsThisMonth)) {
            //Within this month
            filteredAppointmentList.setPredicate(appointment -> appointment.getStartTime().getMonthValue() == ZonedDateTime.now().getMonthValue());
        }

        //Then set sort actions
        appointmentTimeFrameToggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if(appointmentTimeFrameToggleGroup.getSelectedToggle().equals(appointmentsThisWeekRadioButton)) {
                //Within this week
                 filteredAppointmentList.setPredicate(
                         appointment -> (appointment.getStartTime().isAfter(ZonedDateTime.now().with(LocalTime.MIN)) && (appointment.getStartTime().isBefore(ZonedDateTime.now().plusWeeks(1)))));
            }
            else if (appointmentTimeFrameToggleGroup.getSelectedToggle().equals(appointmentsThisMonth)) {
                //Within this month
                filteredAppointmentList.setPredicate(appointment -> appointment.getStartTime().getMonthValue() == ZonedDateTime.now().getMonthValue());
            }
        });

        appointmentTimeFrameToggleGroup.selectToggle(appointmentsThisWeekRadioButton);

        /**
         * <p>
         * @Lambda
         * This Lambda is used in multiple places to simplify the focus change listener.
         * When a window goes in or out of focus, it is refreshed so it keeps up to date with ObservableList entries
         * While not a complex function to begin with, the Lambda again shortens this to a simple one line statement.
         * </p>
         */
        appointmentsTable.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> { appointmentsTable.refresh();});


    }

    /**
     * <p>
     * Loads a specified fmxl window
     * @param fxmlFile The name of the fxml file to load
     * @param windowName Sets the title of the window
     * </p>
     */
    void windowLoader(String fxmlFile, String windowName) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        //Open an new window(with exception catch)
        try {
            fxmlLoader.setLocation(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(windowName);
            stage.setScene(scene);
            stage.show();
        } catch (IOException windowError) {
            windowError.printStackTrace();
        }
    }

    /**
     * <p>
     * Returns the ID of the appointment selected on the tableview
     * </p>
     */
    static int getSelectedAppointmentId() {return selectedAppointmentId;}
}


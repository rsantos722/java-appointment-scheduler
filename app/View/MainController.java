package C195RafaelSantos.View;

import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * <p>
 * Launched after login page. Contains buttons to go to any window in the program, and also has a reminder if there is an appointment in the next 15 minutes.
 * </p>
 */
public class MainController {

    // ResourceBundle that was given to the FXMLLoader

    // URL location of the FXML file that was given to the FXMLLoader

    @FXML // fx:id="customerButton"
    private Button customerButton; // Value injected by FXMLLoader

    @FXML // fx:id="appointmentsButton"
    private Button appointmentsButton; // Value injected by FXMLLoader

    @FXML // fx:id="appointmentsReportButton"
    private Button appointmentsReportButton; // Value injected by FXMLLoader

    @FXML // fx:id="contactSchedulesButton"
    private Button contactSchedulesButton; // Value injected by FXMLLoader

    @FXML // fx:id="appointmentLocationButton"
    private Button appointmentLocationButton; // Value injected by FXMLLoader

    @FXML // fx:id="upcomingAppointmentsText"
    private Text upcomingAppointmentsText; // Value injected by FXMLLoader


    /**
     * <p>
     * Loads the Appointments controller
     * </p>
     */
    @FXML
    void appointmentsButtonAction() {

        windowLoader("Appointments.fxml","Appointments");

    }

    /**
     * <p>
     * Loads the Appointment Report controller
     * </p>
     */
    @FXML
    void appointmentsReportButtonAction() {
        windowLoader("AppointmentReport.fxml","Appointments by Type and Month");

    }

    /**
     * <p>
     * Loads the Schedule Report controller
     * </p>
     */
    @FXML
    void contactSchedulesButtonAction() {

        windowLoader("ScheduleReport.fxml", "Contact Schedules");
    }

    /**
     * <p>
     * Loads the customers controller
     * </p>
     */
    @FXML
    void customersButtonAction() {

        windowLoader("Customers.fxml","Customers");


    }

    /**
     * <p>
     * Loads the appointment locations controller
     * </p>
     */
    @FXML
    void appointmentLocationButtonAction() {

        windowLoader("LocationReport.fxml", "Appointments By Location");

    }


    /**
     * <p>
     * Initializes the main controller, and adds a focus listener to update the appointment reminder
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert customerButton != null : "fx:id=\"customerButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert appointmentsButton != null : "fx:id=\"appointmentsButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert appointmentsReportButton != null : "fx:id=\"appointmentsReportButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert contactSchedulesButton != null : "fx:id=\"contactSchedulesButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert upcomingAppointmentsText != null : "fx:id=\"upcomingAppointmentsText\" was not injected: check your FXML file 'Main.fxml'.";

        appointmentCheck();

        //Listener
        appointmentsButton.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> appointmentCheck());

        customerButton.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> appointmentCheck());

        appointmentsReportButton.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> appointmentCheck());

        contactSchedulesButton.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> appointmentCheck());

        appointmentLocationButton.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> appointmentCheck());


    }

    /**
     * <p>
     * Checks for an upcoming appointment, and displays it on the main page if there is an upcoming one
     * </p>
     */
    void appointmentCheck() {

        //Check for upcoming appointment
        FilteredList<Appointment> upcomingAppointments =
                new FilteredList<>(SchedulingData.getAllAppointments().filtered
                        ((appointment -> (appointment.getStartTime().isBefore(ZonedDateTime.now().plusMinutes(15))) && (appointment.getStartTime().isAfter(ZonedDateTime.now())))));
        String upcomingAppointmentString = "You have no upcoming appointments in the next 15 minutes.";
        //If it is not empty, read all appointments from list into string. Creates a multiline string to display on the main screen.
        if (!upcomingAppointments.isEmpty()) {
            upcomingAppointmentString = "";
            int amountOfAppointments = 0;
            for(Appointment a : upcomingAppointments) {
                upcomingAppointmentString += "-" + a.getTitle() + " (ID:" + a.getAppointmentId() + ") - " + a.getDateString() + " from " + a.getStartTimeString() + " to " + a.getEndTimeString() + "\n";
                amountOfAppointments++;
            }
            String fullAppointmentString = "You have " + amountOfAppointments + " upcoming appointments:\n" + upcomingAppointmentString;
            upcomingAppointmentsText.setText(fullAppointmentString);
        }
        else {
            upcomingAppointmentsText.setText(upcomingAppointmentString);
        }
    }

    /**
     * <p></p>
     * Page Loader
     * Loads JavaFX page based on passed parameters
     * fxmlFile specifies where the file is
     * title sets the title of the window
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
}




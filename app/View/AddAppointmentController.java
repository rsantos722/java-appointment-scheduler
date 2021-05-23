package C195RafaelSantos.View;

import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.Data.ContactDAO;
import C195RafaelSantos.Data.CustomerDAO;
import C195RafaelSantos.Data.UserDAO;
import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static java.lang.Integer.parseInt;

/**
 * <p>
 * Add Appointment Controller
 * Called when the "Add Appointment" button is pressed in the Appointments main screen.
 * Used to add a new appointment
 * @author Rafael Santos
 * </p>
 */
public class AddAppointmentController {

    private int appointmentId;

    @FXML // fx:id="titleBox"
    private TextField titleBox; // Value injected by FXMLLoader
    @FXML // fx:id="descriptionBox"
    private TextField descriptionBox; // Value injected by FXMLLoader
    @FXML // fx:id="locationBox"
    private TextField locationBox; // Value injected by FXMLLoader
    @FXML // fx:id="datePicker"
    private DatePicker datePicker; // Value injected by FXMLLoader
    @FXML // fx:id="contactMenu"
    private ComboBox<String> contactMenu; // Value injected by FXMLLoader
    @FXML // fx:id="typeMenu"
    private TextField typeMenu; // Value injected by FXMLLoader
    @FXML // fx:id="customerIdMenu"
    private ComboBox<String> customerIdMenu; // Value injected by FXMLLoader
    @FXML // fx:id="userIdMenu"
    private ComboBox<String> userIdMenu; // Value injected by FXMLLoader
    @FXML // fx:id="appointmentIdBox"
    private TextField appointmentIdBox; // Value injected by FXMLLoader
    @FXML // fx:id="saveButton"
    private Button saveButton; // Value injected by FXMLLoader
    @FXML // fx:id="cancelAction"
    private Button cancelAction; // Value injected by FXMLLoader
    @FXML // fx:id="startAMPM"
    private ComboBox<String> startAMPM; // Value injected by FXMLLoader
    @FXML // fx:id="endAMPM"
    private ComboBox<String> endAMPM; // Value injected by FXMLLoader
    @FXML // fx:id="startTimeBox"
    private TextField startTimeBox; // Value injected by FXMLLoader
    @FXML // fx:id="endTimeBox"
    private TextField endTimeBox; // Value injected by FXMLLoader

    @FXML
    void appointmentIdBoxAction() {}

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void cancelButtonAction() {
        //Close window
        Stage stage = (Stage) cancelAction.getScene().getWindow();
        stage.close();
    }

    @FXML
    void contactMenuAction() {}
    @FXML
    void customerIdMenuAction() {}
    @FXML
    void datePickerAction() {}
    @FXML
    void descriptionBoxAction() {}
    @FXML
    void locationBoxAction() {}

    /**
     * <p>
     * Called when save button is pressed.
     * First, checks if time the user entered is within office hours. The time is saved in the user's time zone and then compared to office hours in the EST time zone.
     * Next, the time is checked for any conflicts with another appointment of the same contact.
     * If no conflict is found, then the appointment is saved and the window is closed.
     * </p>
     */
    @FXML
    void saveButtonAction() throws SQLException {

        String startTimeBoxInput = startTimeBox.getText();
        String endTimeBoxInput = endTimeBox.getText();

        //Make sure time input is valid
        if(!startTimeBoxInput.matches("((0[1-9])|(1[0-2])):([0-5])([0-9])")) {
            //If user forgot a leading zero, add it and do not raise an issue
            if((startTimeBoxInput.length() == 4) && (startTimeBoxInput.matches("(([1-9]):([0-5])([0-9]))"))) {
                startTimeBox.setText('0' + startTimeBoxInput);     //Add leading 0 to string

            } else {
                errorMessage("There is an issue with your Start time input. Please follow the format HH:mm, with no AM or PM indicator.");
                return;
            }
        }
        if(!endTimeBoxInput.matches("((0[1-9])|(1[0-2])):([0-5])([0-9])")) {
            //If user forgot a leading zero, add it and do not raise an issue
            if((endTimeBoxInput.length() == 4) && (endTimeBoxInput.matches("(([1-9]):([0-5])([0-9]))"))) {
                endTimeBox.setText('0' + endTimeBoxInput);     //Add leading 0 to string

            } else {
                errorMessage("There is an issue with your Start time input. Please follow the format HH:mm, with no AM or PM indicator.");
                return;
            }
        }

        //Cannot schedule outside 8am-10pm EST (convert from their local time to EST)
        ZoneId localZone = ZoneId.systemDefault();
        ZoneId officeZone = ZoneId.of("America/New_York");

        //Office Hours (on the selected day) set to EST
        LocalDate selectedDate = datePicker.getValue();
        LocalTime officeHourStart = LocalTime.of(8,0);
        LocalTime officeHourEnd = LocalTime.of(22, 0);
        LocalDateTime localSelectedDateOfficeHourStart = LocalDateTime.of(selectedDate, officeHourStart);
        LocalDateTime localSelectedDateOfficeHourEnd = LocalDateTime.of(selectedDate, officeHourEnd);
        ZonedDateTime officeHoursStart = ZonedDateTime.of(localSelectedDateOfficeHourStart,officeZone);
        ZonedDateTime officeHoursEnd = ZonedDateTime.of(localSelectedDateOfficeHourEnd,officeZone);

        //Create ZoneDateTime of selected start
        LocalTime selectedStartTime = LocalTime.parse((startTimeBox.getText() + startAMPM.getValue()), DateTimeFormatter.ofPattern("hh:mma"));
        LocalDateTime localSelectedStartTimeFull = LocalDateTime.of(selectedDate,selectedStartTime);
        ZonedDateTime zoneStartTimeFull = ZonedDateTime.of(localSelectedStartTimeFull, localZone);
        //Create ZoneDateTime of selected end
        LocalTime selectedEndTime = LocalTime.parse((endTimeBox.getText() + endAMPM.getValue()), DateTimeFormatter.ofPattern("hh:mma"));
        LocalDateTime localSelectedEndTimeFull = LocalDateTime.of(selectedDate,selectedEndTime);
        ZonedDateTime zoneEndTimeFull = ZonedDateTime.of(localSelectedEndTimeFull,localZone);

        //Check if it is within office hours
        if ((zoneStartTimeFull.isBefore(officeHoursStart) || (zoneStartTimeFull.isAfter(officeHoursEnd)))) {
            errorMessage("The appointment you are attempting to save starts outside of office hours (8AM-10PM EST). Please choose a new time and try again.");
            return;
        }
        if (zoneEndTimeFull.isAfter(officeHoursEnd)) {
            errorMessage("The appointment you are attempting to save ends outside of office hours (8AM-10PM EST). Please choose a new time and try again.");
            return;
        }
        //Make sure times are not the same
        if (zoneStartTimeFull.equals(zoneEndTimeFull)) {
            errorMessage("The appointment start and end times are the same. Please choose a different start and end times and try again.");
            return;
        }
        //Make sure the end of the appointment is not earlier then the beginning
        if (zoneEndTimeFull.isBefore(zoneStartTimeFull)) {
            errorMessage("The selected end time is before the selected start time. Please change the appointment time and try again");
            return;
        }


        //Get Customer ID
        int customerId = parseInt(customerIdMenu.getValue());

        //Check for conflict
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        boolean conflictCheck = appointmentDAO.conflictCheck((
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zoneStartTimeFull.withZoneSameInstant(ZoneId.of("Etc/UTC")))),
                (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zoneEndTimeFull.withZoneSameInstant(ZoneId.of("Etc/UTC")))),
                customerId);
        System.out.println("Exiting AppointmentCheck");
        if (!conflictCheck) {
            Appointment newAppointment = new Appointment();
            newAppointment.setAppointmentId(appointmentId);
            newAppointment.setTitle(titleBox.getText());
            newAppointment.setDescription(descriptionBox.getText());
            newAppointment.setType(typeMenu.getText());
            newAppointment.setLocation(locationBox.getText());
            newAppointment.setStartTime(zoneStartTimeFull);
            newAppointment.setEndTime(zoneEndTimeFull);
            newAppointment.setCustomer(SchedulingData.findCustomer(customerId));
            newAppointment.setContact(SchedulingData.findContactByName(String.valueOf(contactMenu.getValue())));
            newAppointment.setUser(SchedulingData.findUser(parseInt(String.valueOf(userIdMenu.getValue()))));
            SchedulingData.addAppointment(newAppointment);
        }
        else {
            System.out.println("else statement");
            errorMessage("The appointment you are attempting to save has a scheduling conflict with another appointment from the same customer. Please recheck the customer's schedule and try again.");
            return;
        }

        //Close window
        Stage stage = (Stage) appointmentIdBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    void titleBoxAction() {}

    @FXML
    void typeMenuAction() {}

    @FXML
    void userIdMenuAction() {}

    /**
     * <p>
     * Initializes the Add Appointment window.
     * The Appointment ID is incremented and set in a disabled TextField
     * Then, the various ComboBoxes are set up, including getting all contacts, user Ids, and customer Ids.
     * Finally, a binding on the save button is created so the user can only save when a value has been entered in every box.
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert titleBox != null : "fx:id=\"titleBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert descriptionBox != null : "fx:id=\"descriptionBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert locationBox != null : "fx:id=\"locationBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert startTimeBox != null : "fx:id=\"startTimeBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert startAMPM != null : "fx:id=\"startAMPM\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert endTimeBox != null : "fx:id=\"endTimeBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert endAMPM != null : "fx:id=\"endAMPM\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert contactMenu != null : "fx:id=\"contactMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert typeMenu != null : "fx:id=\"typeMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert customerIdMenu != null : "fx:id=\"customerIdMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert userIdMenu != null : "fx:id=\"userIdMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert appointmentIdBox != null : "fx:id=\"appointmentIdBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert cancelAction != null : "fx:id=\"cancelAction\" was not injected: check your FXML file 'AddAppointment.fxml'.";

        //Find largest Appointment ID, increment by one, then set this as the new ID
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        try {
            int i = appointmentDAO.largestEntry() + 1;
            String idText = Integer.toString(i);
            appointmentIdBox.setText(idText);
            appointmentId = i;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Time Option Strings
        ObservableList<String> amPmOptions = FXCollections.observableArrayList("AM","PM");

        //Setup Time Box Options
        startAMPM.setItems(amPmOptions);
        endAMPM.setItems(amPmOptions);


        //Setup contactMenu ComboBox
        ContactDAO contactDAO = new ContactDAO();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try {
            contactNames = contactDAO.readAllContactNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contactMenu.setItems(contactNames.sorted());


        //Setup customerId Menu
        CustomerDAO customerDAO = new CustomerDAO();
        ObservableList<String> customerIdList = FXCollections.observableArrayList();
        try {
            customerIdList = customerDAO.readAllCustomerId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerIdMenu.setItems(customerIdList.sorted());

        //Setup userId Menu (SHow ID and name)
        UserDAO userDAO = new UserDAO();
        ObservableList<String> userIdList = FXCollections.observableArrayList();
        try {
            userIdList = userDAO.readAllUserId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userIdMenu.setItems(userIdList.sorted());


        //Only display save button when everything is entered
        //Bind save button to everything being filled
        saveButton.disableProperty().bind(
                Bindings.isEmpty(titleBox.textProperty())
                .or(Bindings.isEmpty(descriptionBox.textProperty())
                .or(Bindings.isEmpty(locationBox.textProperty())
                .or(Bindings.isNull(datePicker.valueProperty()))
                .or(Bindings.isNull(contactMenu.valueProperty()))
                .or(Bindings.isNull(typeMenu.textProperty()))
                .or(Bindings.isNull(customerIdMenu.valueProperty()))
                .or(Bindings.isNull(contactMenu.valueProperty()))
                .or(Bindings.isNull(userIdMenu.valueProperty())))
                .or(Bindings.isNull(startTimeBox.textProperty())))
                .or(Bindings.isNull(startAMPM.valueProperty()))
                .or(Bindings.isNull(endTimeBox.textProperty()))
                .or(Bindings.isNull(endAMPM.valueProperty())));
    }

    /**
     * <p>
     * Displays an error message
     * @param errorString The String of the error message to be displayed
     * </p>
     */
    void errorMessage(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorString);
        alert.showAndWait();
    }

}



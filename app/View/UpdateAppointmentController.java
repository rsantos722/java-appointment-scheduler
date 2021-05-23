package C195RafaelSantos.View;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.Data.ContactDAO;
import C195RafaelSantos.Data.CustomerDAO;
import C195RafaelSantos.Data.UserDAO;
import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import static java.lang.Integer.parseInt;

/**
 * <p>
 * Allows the user to update a customer from the Customer table
 * @author Rafael Santos
 * </p>
 */
public class UpdateAppointmentController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

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

    @FXML // fx:id="startTimeBox"
    private TextField startTimeBox; // Value injected by FXMLLoader

    @FXML // fx:id="endTimeBox"
    private TextField endTimeBox; // Value injected by FXMLLoader

    @FXML // fx:id="endAMPM"
    private ComboBox<String> endAMPM; // Value injected by FXMLLoader

    @FXML
    void appointmentIdBoxAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

        //Close window
        Stage stage = (Stage) cancelAction.getScene().getWindow();
        stage.close();

    }

    @FXML
    void contactMenuAction(ActionEvent event) {

    }

    @FXML
    void customerIdMenuAction(ActionEvent event) {

    }

    @FXML
    void datePickerAction(ActionEvent event) {

    }

    @FXML
    void descriptionBoxAction(ActionEvent event) {

    }

    @FXML
    void locationBoxAction(ActionEvent event) {

    }

    /**
     * <p>
     * Checks for a valid time input, then makes sure that there is no conflict with another appointment, before finally updating the appointment
     * </p>
     */
    @FXML
    void saveButtonAction(ActionEvent event) throws SQLException {

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
        //Get time from start and end box, and date
        //Get local time zone and time zone of company
        ZoneId localZone = ZoneId.systemDefault();
        System.out.println(localZone);
        ZoneId officeZone = ZoneId.of("America/New_York");
        ZoneId utcZone = ZoneId.of("UTC");

        //Office Hours (on the selected day) set to EST
        LocalDate selectedDate = datePicker.getValue();
        LocalTime officeHourStart = LocalTime.of(8,0);
        LocalTime officeHourEnd = LocalTime.of(22,00);
        LocalDateTime localSelectedDateOfficeHourStart = LocalDateTime.of(selectedDate, officeHourStart);
        LocalDateTime localSelectedDateOfficeHourEnd = LocalDateTime.of(selectedDate, officeHourEnd);
        ZonedDateTime officeHoursStart = ZonedDateTime.of(localSelectedDateOfficeHourStart,officeZone);
        ZonedDateTime officeHoursEnd = ZonedDateTime.of(localSelectedDateOfficeHourEnd,officeZone);

        //Start time
        LocalTime selectedStartTime = LocalTime.parse((startTimeBox.getText() + startAMPM.getValue()), DateTimeFormatter.ofPattern("hh:mma"));
        LocalDateTime localSelectedStartTimeFull = LocalDateTime.of(selectedDate,selectedStartTime);
        ZonedDateTime zoneStartTimeFull = ZonedDateTime.of(localSelectedStartTimeFull, localZone);
        //End Time
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
        boolean conflictCheck = appointmentDAO.updateConflictCheck((
                 DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zoneStartTimeFull.withZoneSameInstant(ZoneId.of("Etc/UTC")))),
                (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zoneEndTimeFull.withZoneSameInstant(ZoneId.of("Etc/UTC")))),
                customerId, parseInt(appointmentIdBox.getText()));
        if (!conflictCheck) {
            Appointment newAppointment = new Appointment();
            newAppointment.setAppointmentId(parseInt(appointmentIdBox.getText()));
            newAppointment.setTitle(titleBox.getText());
            newAppointment.setDescription(descriptionBox.getText());
            newAppointment.setType(typeMenu.getText());
            newAppointment.setLocation(locationBox.getText());
            newAppointment.setStartTime(zoneStartTimeFull);
            newAppointment.setEndTime(zoneEndTimeFull);
            newAppointment.setCustomer(SchedulingData.findCustomer(customerId));
            newAppointment.setContact(SchedulingData.findContactByName(String.valueOf(contactMenu.getValue())));
            System.out.println(newAppointment.getContact().getId());
            newAppointment.setUser(SchedulingData.findUser(parseInt(String.valueOf(userIdMenu.getValue()))));
            SchedulingData.updateAppointment(newAppointment);
        }
        else {
            System.out.println("else statement");
            errorMessage("The appointment you are attempting to save has a scheduling conflict with another appointment from the same customer. Please recheck the customer's schedule and try again.");
            return;
        }

        //TODO Javadocs

        //Close window
        Stage stage = (Stage) appointmentIdBox.getScene().getWindow();
        stage.close();

    }

    @FXML
    void titleBoxAction(ActionEvent event) {

    }

    @FXML
    void typeMenuAction(ActionEvent event) {

    }

    @FXML
    void userIdMenuAction(ActionEvent event) {

    }

    /**
     * <p>
     * Initializes the update window and propagates the selected appointment's values
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert titleBox != null : "fx:id=\"titleBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert descriptionBox != null : "fx:id=\"descriptionBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert locationBox != null : "fx:id=\"locationBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert contactMenu != null : "fx:id=\"contactMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert typeMenu != null : "fx:id=\"typeMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert customerIdMenu != null : "fx:id=\"customerIdMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert userIdMenu != null : "fx:id=\"userIdMenu\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert appointmentIdBox != null : "fx:id=\"appointmentIdBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert cancelAction != null : "fx:id=\"cancelAction\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert startTimeBox != null : "fx:id=\"startTimeBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert startAMPM != null : "fx:id=\"startAMPM\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert endTimeBox != null : "fx:id=\"endTimeBox\" was not injected: check your FXML file 'AddAppointment.fxml'.";
        assert endAMPM != null : "fx:id=\"endAMPM\" was not injected: check your FXML file 'AddAppointment.fxml'.";

        Appointment appointmentToUpdate = SchedulingData.findAppointment(AppointmentsController.getSelectedAppointmentId());

        //Time Option Strings
        ObservableList<String> timeOptions = FXCollections.observableArrayList("12:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00", "11:00");
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

        //Set Values to Update
        appointmentIdBox.setText(String.valueOf(appointmentToUpdate.getAppointmentId()));
        titleBox.setText(appointmentToUpdate.getTitle());
        descriptionBox.setText(appointmentToUpdate.getDescription());
        locationBox.setText(appointmentToUpdate.getLocation());
        datePicker.setValue(appointmentToUpdate.getStartTime().toLocalDate());
        typeMenu.setText(appointmentToUpdate.getType());
        customerIdMenu.setValue(String.valueOf(appointmentToUpdate.getCustomer().getCustomerId()));
        contactMenu.setValue(appointmentToUpdate.getContactName());
        userIdMenu.setValue(String.valueOf(appointmentToUpdate.getUser().getId()));
        //Convert times to usable format
        DateTimeFormatter timeBoxFormat = DateTimeFormatter.ofPattern("hh:mm");
        DateTimeFormatter amPmFormat = DateTimeFormatter.ofPattern("a");
        startTimeBox.setText(appointmentToUpdate.getStartTime().format(timeBoxFormat));
        endTimeBox.setText(appointmentToUpdate.getEndTime().format(timeBoxFormat));
        startAMPM.setValue(appointmentToUpdate.getStartTime().format(amPmFormat));
        endAMPM.setValue(appointmentToUpdate.getEndTime().format(amPmFormat));




        //Only display save button when everything is entered
        //Bind save button to everything being filled
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

    //Display an error message
    void errorMessage(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorString);
        alert.showAndWait();
    }

}

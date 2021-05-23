package C195RafaelSantos.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import C195RafaelSantos.Data.*;
import C195RafaelSantos.Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * <p>
 * Handles the login window for the program
 * @author Rafael Santos
 * </p>
 */
public class LoginController {

    private String userIncorrect;
    private String userEmpty;
    private String passwordIncorrect;
    private String passwordEmpty;
    private String inputEmpty;

    @FXML // fx:id="loginText"
    private Text loginText; // Value injected by FXMLLoader

    @FXML // fx:id="userIdText"
    private Text userIdText; // Value injected by FXMLLoader

    @FXML // fx:id="passwordBox"
    private PasswordField passwordBox; // Value injected by FXMLLoader

    @FXML // fx:id="passwordText"
    private Text passwordText; // Value injected by FXMLLoader

    @FXML // fx:id="userIdBox"
    private TextField userIdBox; // Value injected by FXMLLoader

    @FXML // fx:id="locationTitleText"
    private Text locationTitleText; // Value injected by FXMLLoader

    @FXML // fx:id="locationText"
    private Text locationText; // Value injected by FXMLLoader

    @FXML // fx:id="loginButton"
    private Button loginButton; // Value injected by FXMLLoader

    /**
     * <p>
     * Called when the customer attempts to login.
     * First, the username and password are checked to be correct. The login attempt is also logged to login_activity.txt
     * If login is successful, the main controller is called, and SchedulingData is initialized with DB data
     * This window is also available in French if the user's locale is french.
     * </p>
     */
    @FXML
    void loginButtonAction() throws SQLException {

        boolean loginSuccessful = true;

        String userBoxString = userIdBox.getText().toLowerCase(Locale.ROOT);
        String passwordBoxString = passwordBox.getText().toLowerCase(Locale.ROOT);

        if((userBoxString.isEmpty()) && (passwordBoxString.isEmpty())) {
            Alert noInputGiven = new Alert(Alert.AlertType.ERROR, inputEmpty);
            noInputGiven.showAndWait();
            loginSuccessful = false;
            return;
        }
        //Make sure login boxes are not empty
        if(userBoxString.isEmpty()) {
            Alert emptyUserBox = new Alert(Alert.AlertType.ERROR, userEmpty);
            emptyUserBox.showAndWait();
            loginSuccessful = false;
            return;
        }

        if(passwordBoxString.isEmpty()) {
            Alert emptyPasswordBox = new Alert(Alert.AlertType.ERROR, passwordEmpty);
            emptyPasswordBox.showAndWait();
            loginSuccessful = false;
            return;
        }

        if (!userBoxString.equals("test")) {
            Alert usernameError = new Alert(Alert.AlertType.ERROR, userIncorrect);
            usernameError.showAndWait();
            loginSuccessful = false;
            return;
        }

        //Check Password
        if (!passwordBoxString.equals("test")) {
            Alert passwordError = new Alert(Alert.AlertType.ERROR, passwordIncorrect);
            passwordError.showAndWait();
            loginSuccessful = false;
            return;
        }

        DateTimeFormatter dtfTimeStamp = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma");
        if (loginSuccessful) {
            try {
                writeLoginActivity(ZonedDateTime.now().format(dtfTimeStamp) + ":  Successful login attempt by user \"" + userBoxString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                writeLoginActivity(ZonedDateTime.now().format(dtfTimeStamp) + ":  Failed login attempt by user \"" + userBoxString);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        //Contacts
        ContactDAO contactDAO = new ContactDAO();
        ResultSet rs;
        rs = contactDAO.readAll();
        while(rs.next()) {
            Contact contact = new Contact();
            contact.setId(rs.getInt("Contact_ID"));
            contact.setName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
            SchedulingData.initialAddContact(contact);
        }

        //Users
        UserDAO userDAO = new UserDAO();
        rs = userDAO.readAll();
        while(rs.next()) {
            User user = new User();
            user.setId(rs.getInt("User_ID"));
            user.setName(rs.getString("User_Name"));
            SchedulingData.initialAddUser(user);
        }

        //Countries
        CountryDAO countryDAO = new CountryDAO();
        rs = countryDAO.readAll();
        while(rs.next()) {
            Country country = new Country();
            country.setCountryId(rs.getInt("Country_ID"));
            country.setCountryName(rs.getString("Country"));
            SchedulingData.initialAddCountry(country);
        }
        //Divisions
        DivisionDAO divisionDAO = new DivisionDAO();
        rs = divisionDAO.readAll();
        while(rs.next()) {
            Division division = new Division();
            division.setDivisionId(rs.getInt("Division_ID"));
            division.setDivisionName(rs.getString("Division"));
            division.setCountry(Integer.parseInt(rs.getString("Country_ID")));
            SchedulingData.initialAddDivision(division);
        }

        //Customers
        CustomerDAO customerDAO = new CustomerDAO();
        rs = customerDAO.readAll();
        while(rs.next()) {
            Customer c = new Customer();
            c.setCustomerId(rs.getInt("Customer_ID"));
            c.setName(rs.getString("Customer_Name"));
            c.setAddress(rs.getString("Address"));
            c.setPostalCode(rs.getString("Postal_Code"));
            c.setDivision(SchedulingData.findDivisionInt(rs.getInt("Division_ID")));
            c.setPhone(rs.getString("Phone"));

            SchedulingData.initialAddCustomer(c);


        }
        //Appointments
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        rs = appointmentDAO.readAll();
        while(rs.next()) {
            Appointment a = new Appointment();
            a.setAppointmentId(rs.getInt("Appointment_ID"));
            a.setTitle(rs.getString("Title"));
            a.setDescription(rs.getString("Description"));
            a.setLocation(rs.getString("Location"));
            a.setType(rs.getString("Type"));

            //Get Date. Saved in Object in local time zone, but stored in DB as UTC
            ZoneId userZone = ZoneId.systemDefault();
            ZonedDateTime zonedStart = rs.getTimestamp("Start").toLocalDateTime().atZone(userZone);
            ZonedDateTime zonedEnd = rs.getTimestamp("End").toLocalDateTime().atZone(userZone);

            //Create ZoneDateTime and save
            a.setStartTime(zonedStart);
            a.setEndTime(zonedEnd);

            a.setCustomer(SchedulingData.findCustomer(rs.getInt("Customer_ID")));
            a.setUser(SchedulingData.findUser(rs.getInt("User_ID")));
            a.setContact(SchedulingData.findContact(rs.getInt("Contact_ID")));

            SchedulingData.initialAddAppointment(a);
        }

        //Load Main Page
        try {
            windowLoader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Close Login Window
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();


    }

    @FXML
    void passwordBoxAction() {

    }

    @FXML
    void userIdBoxAction() {

    }

    /**
     * <p>
     * Initalizes the login window, and sets up the French locale
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert loginText != null : "fx:id=\"loginText\" was not injected: check your FXML file 'Login.fxml'.";
        assert userIdText != null : "fx:id=\"userIdText\" was not injected: check your FXML file 'Login.fxml'.";
        assert passwordBox != null : "fx:id=\"passwordBox\" was not injected: check your FXML file 'Login.fxml'.";
        assert passwordText != null : "fx:id=\"passwordText\" was not injected: check your FXML file 'Login.fxml'.";
        assert userIdBox != null : "fx:id=\"userIdBox\" was not injected: check your FXML file 'Login.fxml'.";
        assert locationTitleText != null : "fx:id=\"locationTitleText\" was not injected: check your FXML file 'Login.fxml'.";
        assert locationText != null : "fx:id=\"locationText\" was not injected: check your FXML file 'Login.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'Login.fxml'.";

        //Find user location
        ZoneId userZoneId = ZoneId.systemDefault();
        String userLocation = userZoneId.getId();
        locationText.setText(userLocation);

        //Load Locales
        ResourceBundle rb = ResourceBundle.getBundle("C195RafaelSantos/Locales", Locale.getDefault());
        loginText.setText(rb.getString("title"));
        loginButton.setText(rb.getString("title"));
        locationTitleText.setText(rb.getString("location"));
        userIdText.setText(rb.getString("userId"));
        passwordText.setText(rb.getString("password"));
        userIncorrect = rb.getString("userError");
        userEmpty = rb.getString("usernameEmpty");
        passwordIncorrect = rb.getString("passwordError");
        passwordEmpty = rb.getString("passwordEmpty");
        inputEmpty = rb.getString("inputEmpty");

    }

    /**
     * Page Loader
     * Loads JavaFX page based on passed parameters
     * fxmlFile specifies where the file is
     * title sets the title of the window
     */
     void windowLoader() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        //Open an new window(with exception catch)
        try {
            fxmlLoader.setLocation(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("C195 Main Screen");
            stage.setScene(scene);
            stage.show();
        } catch (IOException windowError) {
            windowError.printStackTrace();
        }
    }


    /**
     * <p>
     * Writes login activity to login_activity.txt
     * </p>
     */
    void writeLoginActivity(String loginActivityString) throws IOException {
        try {
            File loginActivityFile = new File("login_activity.txt");
            if (loginActivityFile.createNewFile()) {
                System.out.println("login_activity.txt created.");
            } else {
                System.out.println("login_activity.txt already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter loginFileWriter = new FileWriter("login_activity.txt",true);
            loginFileWriter.append(loginActivityString).append("\" \n");
            loginFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

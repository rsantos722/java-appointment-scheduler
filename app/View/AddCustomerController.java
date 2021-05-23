package C195RafaelSantos.View;

import C195RafaelSantos.Data.CountryDAO;
import C195RafaelSantos.Data.CustomerDAO;
import C195RafaelSantos.Data.DivisionDAO;
import C195RafaelSantos.Model.Country;
import C195RafaelSantos.Model.Customer;
import C195RafaelSantos.Model.Division;
import C195RafaelSantos.Model.SchedulingData;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * <p>
 * Opened when the customer clicks Add Customer in the Customer window
 * Allows the user to add a new customer
 * </p>
 */
public class AddCustomerController {

    private int customerId;

    @FXML // fx:id="nameBox"
    private TextField nameBox; // Value injected by FXMLLoader
    @FXML // fx:id="postalCodeBox"
    private TextField postalCodeBox; // Value injected by FXMLLoader
    @FXML // fx:id="phoneBox"
    private TextField phoneBox; // Value injected by FXMLLoader
    @FXML // fx:id="customerIdBox"
    private TextField customerIdBox; // Value injected by FXMLLoader
    @FXML // fx:id="addressBox"
    private TextField addressBox; // Value injected by FXMLLoader
    @FXML // fx:id="countryMenu"
    private ComboBox<String> countryMenu; // Value injected by FXMLLoader
    @FXML // fx:id="firstDivisionMenu"
    private ComboBox<String> firstDivisionMenu; // Value injected by FXMLLoader
    @FXML // fx:id="saveButton"
    private Button saveButton; // Value injected by FXMLLoader
    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML
    void addressBoxAction() {}

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void cancelButtonAction() {
        //Close window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * <p>
     * An input in the Country menu is required before entering a First Level Division.
     * When the user picks a country, the division menu is cleared, and then populated with divisions associated with the selected country
     * </p>
     */
    @FXML
    void countryMenuAction() {
        //Reset firstDivisionMenu and reset text
        firstDivisionMenu.getSelectionModel().clearSelection();

        //Create list for Divisions based on selected country
        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        String selectedCountryString = countryMenu.getValue();

        //Identify selected country- there has to be a better way to do this
        Country selectedCountry = SchedulingData.findCountry(selectedCountryString);
        int countryId = selectedCountry.getCountryId();
        DivisionDAO divisionDAO = new DivisionDAO();
        try {
            filteredDivisions = divisionDAO.getDivisionsFromCountryId(countryId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Set items and enable box if this is the first time selecting
        firstDivisionMenu.setItems(filteredDivisions);
        firstDivisionMenu.setDisable(false);

    }

    @FXML
    void customerIdBoxAction() {}
    @FXML
    void firstDivisionMenuAction() {}
    @FXML
    void nameBoxAction() {}
    @FXML
    void phoneBoxAction() {}
    @FXML
    void postalCodeBoxAction() {}

    /**
     * <p>
     * Saves the customer into Scheduling Data and the database, then closes the window
     * </p>
     */
    @FXML
    void saveButtonAction() {

        //Create new customer object
        Customer newCustomer = new Customer();
        newCustomer.setCustomerId(customerId);
        newCustomer.setName(nameBox.getText());
        newCustomer.setAddress(addressBox.getText());
        newCustomer.setPhone(phoneBox.getText());
        newCustomer.setPostalCode(postalCodeBox.getText());
        Division customerDivision;
        customerDivision = SchedulingData.findDivision(firstDivisionMenu.getValue());
        newCustomer.setDivision(customerDivision);

        //Add to customers
        try {
            SchedulingData.addCustomer(newCustomer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Close window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }


    /**
     * <p>
     * Initializes the window.
     * First, the Customer ID is incremented and set to the disabled TextField.
     * Then, the Country menu is populated with available countries
     * Finally, the save button is binded to every entry box needing a value to make sure the user enters a value for every box
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert nameBox != null : "fx:id=\"nameBox\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert postalCodeBox != null : "fx:id=\"postalCodeBox\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert phoneBox != null : "fx:id=\"phoneBox\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert customerIdBox != null : "fx:id=\"customerIdBox\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert addressBox != null : "fx:id=\"addressBox\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert countryMenu != null : "fx:id=\"countryMenu\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert firstDivisionMenu != null : "fx:id=\"firstDivisionMenu\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'AddCustomer.fxml'.";

        //Find largest Customer_ID, increment by one, then set this as Customer_ID
        CustomerDAO customerDAO = new CustomerDAO();
        try {
            int i = customerDAO.largestEntry() + 1;
            String idText = Integer.toString(i);
            customerIdBox.setText(idText);
            customerId = i;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        //Set Country ComboBox items
        CountryDAO countryDAO = new CountryDAO();
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            countryNames = countryDAO.readAllCountryNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        countryMenu.setItems(countryNames);

        //Bind save button to everything being filled
        saveButton.disableProperty().bind(
                Bindings.isEmpty(nameBox.textProperty())
                .or(Bindings.isEmpty(addressBox.textProperty())
                .or(Bindings.isEmpty(phoneBox.textProperty())
                .or(Bindings.isEmpty(postalCodeBox.textProperty()))
                .or(Bindings.isNull( firstDivisionMenu.valueProperty()))
                .or(Bindings.isNull(countryMenu.valueProperty())))));
    }
}

package C195RafaelSantos.View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import C195RafaelSantos.Data.CountryDAO;
import C195RafaelSantos.Data.DivisionDAO;
import C195RafaelSantos.Model.Country;
import C195RafaelSantos.Model.Customer;
import C195RafaelSantos.Model.Division;
import C195RafaelSantos.Model.SchedulingData;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * <p>
 * Allows the user to update a customer from the Customers controller
 * @author Rafael Santos
 * </p>
 */
public class UpdateCustomerController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="nameBox"
    private TextField nameBox; // Value injected by FXMLLoader

    @FXML // fx:id="postalCodeBox"
    private TextField postalCodeBox; // Value injected by FXMLLoader

    @FXML // fx:id="phoneBox"
    private TextField phoneBox; // Value injected by FXMLLoader

    @FXML // fx:id="customerIdBox"
    private TextField customerIdBox; // Value injected by FXMLLoader

    @FXML // fx:id="firstDivisionMenu"
    private ComboBox<String> firstDivisionMenu; // Value injected by FXMLLoader

    @FXML // fx:id="countryBox"
    private ComboBox<String> countryBox; // Value injected by FXMLLoader

    @FXML // fx:id="addressBox"
    private TextField addressBox; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private Button saveButton; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML
    void addressBoxAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

        //Close window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }

    /**
     * <p>
     * Filters first level division information based on selected country
     * </p>
     */
    @FXML
    void countryBoxAction(ActionEvent event) {

        //Reset firstDivisionMenu and reset text
        firstDivisionMenu.getSelectionModel().clearSelection();

        //Create list for Divisions based on selected country
        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        String selectedCountryString = countryBox.getValue();

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
    void customerIdBoxAction(ActionEvent event) {

    }

    @FXML
    void firstDivisionMenuAction(ActionEvent event) {

    }

    @FXML
    void nameBoxAction(ActionEvent event) {

    }

    @FXML
    void phoneBoxAction(ActionEvent event) {

    }

    @FXML
    void postalCodeBoxAction(ActionEvent event) {

    }

    /**
     * <p>
     * Updates the customer in SchedulingData and the DB
     * </p>
     */
    @FXML
    void saveButtonAction(ActionEvent event) {
        //Create new customer object
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(Integer.parseInt(customerIdBox.getText()));
        updatedCustomer.setName(nameBox.getText());
        updatedCustomer.setAddress(addressBox.getText());
        updatedCustomer.setPhone(phoneBox.getText());
        updatedCustomer.setPostalCode(postalCodeBox.getText());
        Division customerDivision = new Division();
        customerDivision = SchedulingData.findDivision(firstDivisionMenu.getValue());
        System.out.println(customerDivision);
        updatedCustomer.setDivision(customerDivision);


        //Update customer entry
        try {
            SchedulingData.updateCustomer(updatedCustomer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Close window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }

    /**
     * <p>
     * Initializes the window, including propagating the window with the selected customer's values
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert nameBox != null : "fx:id=\"nameBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert postalCodeBox != null : "fx:id=\"postalCodeBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert phoneBox != null : "fx:id=\"phoneBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert customerIdBox != null : "fx:id=\"customerIdBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert firstDivisionMenu != null : "fx:id=\"firstDivisionMenu\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert countryBox != null : "fx:id=\"countryBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert addressBox != null : "fx:id=\"addressBox\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'UpdateCustomer.fxml'.";

        //Create customer for box info
        int customerId = CustomersController.getSelectedCustomerId();
        Customer customerToUpdate = SchedulingData.findCustomer(customerId);

        //Set Country ComboBox items
        CountryDAO countryDAO = new CountryDAO();
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            countryNames = countryDAO.readAllCountryNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        countryBox.setItems(countryNames);




        //Set box values
        customerIdBox.setText(String.valueOf(customerToUpdate.getCustomerId()));
        nameBox.setText(customerToUpdate.getName());
        addressBox.setText(customerToUpdate.getAddress());
        phoneBox.setText(customerToUpdate.getPhone());
        postalCodeBox.setText(customerToUpdate.getPostalCode());
        System.out.println(customerToUpdate.getDivision().getCountry().getCountryName());
        countryBox.getSelectionModel().select(customerToUpdate.getDivision().getCountry().getCountryName());


        //Since division is initialized, free it up
        //Create list for Divisions based on selected country
        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        String selectedCountryString = countryBox.getValue();

        //Identify selected country
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

        firstDivisionMenu.getSelectionModel().select(customerToUpdate.getDivision().getDivisionName());


        //Bind save button to everything being filled
        saveButton.disableProperty().bind(
                Bindings.isEmpty(nameBox.textProperty())
                .or(Bindings.isEmpty(addressBox.textProperty())
                .or(Bindings.isEmpty(phoneBox.textProperty())
                .or(Bindings.isEmpty(postalCodeBox.textProperty()))
                .or(Bindings.isNull( firstDivisionMenu.valueProperty()))
                .or(Bindings.isNull(countryBox.valueProperty())))));

    }
}

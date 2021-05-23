package C195RafaelSantos.View;

import C195RafaelSantos.Model.Customer;
import C195RafaelSantos.Model.SchedulingData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <p>
 * Controller for the main Customers window
 * @author Rafael Santos
 * </p>
 */
public class CustomersController {

    static int selectedCustomerId;

    @FXML // fx:id="customerTable"
    private TableView<Customer> customerTable; // Value injected by FXMLLoader

    @FXML // fx:id="customerIdColumn"
    private TableColumn<Customer, Integer> customerIdColumn; // Value injected by FXMLLoader

    @FXML // fx:id="customerNameColumn"
    private TableColumn<Customer, String> customerNameColumn; // Value injected by FXMLLoader

    @FXML // fx:id="addressColumn"
    private TableColumn<Customer, String> addressColumn; // Value injected by FXMLLoader

    @FXML // fx:id="postalCodeColumn"
    private TableColumn<Customer, String> postalCodeColumn; // Value injected by FXMLLoader

    @FXML // fx:id="phoneColumn"
    private TableColumn<Customer, String> phoneColumn; // Value injected by FXMLLoader

    @FXML // fx:id="addCustomerButton"
    private Button addCustomerButton; // Value injected by FXMLLoader

    @FXML // fx:id="updateCustomer"
    private Button updateCustomer; // Value injected by FXMLLoader

    @FXML // fx:id="deleteCustomer"
    private Button deleteCustomer; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    /**
     * <p>
     * Loads the Add Customer window
     * </p>
     */
    @FXML
    void addCustomerButtonAction() {

        windowLoader("AddCustomer.fxml", "Add Customer");

    }

    @FXML
    void addressColumnCancelAction() {

    }

    /**
     * <p>
     * Allows user to enter a new address into the respective column on the table
     * </p>
     */
    @FXML
    void addressColumnCommitAction(TableColumn.CellEditEvent<Customer, String> event) throws SQLException {
        //Get customer and ID
        int customerId = event.getRowValue().getCustomerId();
        Customer customerToUpdate = SchedulingData.findCustomer(customerId);
        customerToUpdate.setAddress(event.getNewValue());
        //Update customer
        SchedulingData.updateCustomer(customerToUpdate);
        //Update table
        updateCustomers();


    }

    @FXML
    void customerNameColumnCancelAction() {

    }

    /**
     * <p>
     * Allows the Customer column to be edited
     * </p>
     */
    @FXML
    void customerNameColumnCommitAction(TableColumn.CellEditEvent<Customer, String> event) throws SQLException {
        //Get customer and ID
        int customerId = event.getRowValue().getCustomerId();
        Customer customerToUpdate = SchedulingData.findCustomer(customerId);
        customerToUpdate.setName(event.getNewValue());
        //Update customer
        SchedulingData.updateCustomer(customerToUpdate);
        //Update table
        updateCustomers();
    }

    @FXML
    void customerTableSortAction() {

    }

    /**
     * <p>
     * Confirms with the user if they would like to delete the customer, with an extra notification that all associated appointments will be deleted.
     * If the user confirms, the customer and associated appointments are deleted
     * </p>
     */
    @FXML
    void deleteCustomerButtonAction() throws SQLException {

        //Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this customer?\n" +
                " Note: All appointments associated with the customer will be deleted as well.", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        //If user would like to continue with deletion
        if (alert.getResult() == ButtonType.YES) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            SchedulingData.deleteCustomer(selectedCustomer);
        }
    }

    /**
     * <p>
     * Gets the current window and closes it
     * </p>
     */
    @FXML
    void exitButtonAction() {
        //Close window
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void phoneColumnCancelAction() {

    }

    /**
     * <p>
     * Allows the phone number to be edited from the table
     * </p>
     */
    @FXML
    void phoneColumnCommitAction(TableColumn.CellEditEvent<Customer, String> event) throws SQLException {
        //Get customer and ID
        int customerId = event.getRowValue().getCustomerId();
        Customer customerToUpdate = SchedulingData.findCustomer(customerId);
        customerToUpdate.setPhone(event.getNewValue());
        //Update customer
        SchedulingData.updateCustomer(customerToUpdate);
        //Update table
        updateCustomers();

    }

    @FXML
    void postalCodeColumnCancelAction() {

    }

    /**
     * <p>
     * Allows the postal code to be edited from the table
     * </p>
     */
    @FXML
    void postalCodeColumnCommitAction(TableColumn.CellEditEvent<Customer, String> event) throws SQLException {
        //Get customer and ID
        int customerId = event.getRowValue().getCustomerId();
        Customer customerToUpdate = SchedulingData.findCustomer(customerId);
        customerToUpdate.setPostalCode(event.getNewValue());
        //Update customer
        SchedulingData.updateCustomer(customerToUpdate);
        //Update table
        updateCustomers();

    }

    /**
     * <p>
     * Opens the Update Customer window if a customer is selected
     * </p>
     */
    @FXML
    void updateCustomerButtonAction() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null) {
            Alert noInputGiven = new Alert(Alert.AlertType.ERROR, "No customer was selected to update.\n Please select a customer in the table to update and try again.");
            noInputGiven.showAndWait();
            return;
        }

        selectedCustomerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        windowLoader("UpdateCustomer.fxml","Update Customer");
    }

    /**
     * <p>
     * Populates the window with customer information.
     * </p>
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert customerTable != null : "fx:id=\"customerTable\" was not injected: check your FXML file 'Customers.fxml'.";
        assert customerIdColumn != null : "fx:id=\"customerIdColumn\" was not injected: check your FXML file 'Customers.fxml'.";
        assert customerNameColumn != null : "fx:id=\"customerNameColumn\" was not injected: check your FXML file 'Customers.fxml'.";
        assert addressColumn != null : "fx:id=\"addressColumn\" was not injected: check your FXML file 'Customers.fxml'.";
        assert postalCodeColumn != null : "fx:id=\"postalCodeColumn\" was not injected: check your FXML file 'Customers.fxml'.";
        assert phoneColumn != null : "fx:id=\"phoneColumn\" was not injected: check your FXML file 'Customers.fxml'.";
        assert addCustomerButton != null : "fx:id=\"addCustomerButton\" was not injected: check your FXML file 'Customers.fxml'.";
        assert updateCustomer != null : "fx:id=\"updateCustomer\" was not injected: check your FXML file 'Customers.fxml'.";
        assert deleteCustomer != null : "fx:id=\"deleteCustomer\" was not injected: check your FXML file 'Customers.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'Customers.fxml'.";


        //Setup Customer Table
        customerTable.setEditable(true);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        postalCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        customerTable.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> updateCustomers());

        //Update table
        updateCustomers();



    }

    private void updateCustomers() {
        customerTable.setItems(SchedulingData.getAllCustomers());
    }

    /**
     * <p>
     * Loads a window from the given file
     * @param windowName What to set the title of the window to
     * @param fxmlFile The name of the fxml file to load
     * </p>
     */
    private void windowLoader(String fxmlFile, String windowName) {
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
     * Returns the ID of the customer that is selected on the tableview
     * </p>
     */
    static int getSelectedCustomerId() {
        return selectedCustomerId;
    }


}
package C195RafaelSantos.Data;

import C195RafaelSantos.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * <p>
 * Handles DB operations related to the Customer class
 *
 * @author Rafael Santos
 * @see C195RafaelSantos.Model.Customer
 * </p>
 */
public class CustomerDAO implements DAO<Customer>{

    /**
     * <p>
     * Creates a new Customer in the customers table, based on a provided Customer object
     * @param c Customer to add to the Database
     * </p>
     */
    @Override
    public void create(Customer c) throws SQLException {
        String statement =
               "INSERT INTO customers(Customer_ID,Customer_Name,Address,Postal_Code,Phone,Division_ID) VALUES (" +
               c.getCustomerId() + ",'" +
               c.getName() + "','" +
               c.getAddress() + "','" +
               c.getPostalCode() + "','" +
               c.getPhone() + "'," +
               c.getDivision().getDivisionId() + ");";

        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Returns a ResultSet with every entry in the Customer table
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM customers";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Updates a specified customer in the customers table
     * @param c The customer (as an object) to update
     * </p>
     */
    @Override
    public void update(Customer c) throws SQLException {
        String statement =
                "UPDATE customers SET " +
                "Customer_Name ='" + c.getName() +
                "', Address ='" + c.getAddress() +
                "', Phone ='" + c.getPhone() +
                "', Division_ID=" + c.getDivision().getDivisionId() +
                " WHERE Customer_ID = " + c.getCustomerId();

        System.out.println(statement);
        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Removes a specified customer from the database
     * @param c The customer to remove
     * </p>
     */
    @Override
    public void delete(Customer c) throws SQLException {
        String statement = "DELETE FROM customers WHERE Customer_ID =" + c.getCustomerId() + ";";
        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Returns an Integer with the largest Customer_ID. Used to increment the ID when a new Customer is created
     * </p>
     */
    public int largestEntry() throws SQLException {
        String statement = "SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 0,1";
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        rs.next();
        return rs.getInt("Customer_ID");
    }

    /**
     * <p>
     * Returns an ObservableList will every Customer_ID in the database
     * </p>
     */
    public ObservableList<String> readAllCustomerId() throws SQLException {
        ObservableList<String> customerInfo = FXCollections.observableArrayList();
        String statement = "SELECT Customer_ID FROM customers";
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        while(rs.next()) {
            customerInfo.add((rs.getString("Customer_ID")));
        }
        return customerInfo;
    }

}

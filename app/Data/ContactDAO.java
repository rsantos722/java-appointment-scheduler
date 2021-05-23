package C195RafaelSantos.Data;

import C195RafaelSantos.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Handles DB operations related to the Contact class
 *
 * @author Rafael Santos
 * @see C195RafaelSantos.Model.Contact
 * </p>
 */
public class ContactDAO implements DAO<Contact> {



    Connection conn = DatabaseConnection.getConnection();


    /**
     * <p>
     * Returns a ResultSet with every entry in the Customer table
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM contacts";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Creates an ObservableList with every Contact_Name from the Database
     * </p>
     */
    public ObservableList<String> readAllContactNames() throws SQLException {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        String statement = "SELECT Contact_Name FROM contacts";
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        while(rs.next()) {contactNames.add(rs.getString("Contact_Name"));}
        return contactNames;
    }

    //Not used
    @Override
    public void create(Contact contact) throws SQLException {}
    @Override
    public void update(Contact contact) throws SQLException {}
    @Override
    public void delete(Contact contact) throws SQLException {}


}

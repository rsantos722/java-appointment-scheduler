package C195RafaelSantos.Data;

import C195RafaelSantos.Model.Country;
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
 * @see C195RafaelSantos.Model.Country
 * </p>
 */
public class CountryDAO implements DAO<Country>{

    //Initialize Connection
    Connection conn = DatabaseConnection.getConnection();


    /**
     * <p>
     * Returns a ResultSet with every entry in the Country table
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM countries";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Creates an ObservableList with every Country name (Country column) from the Database
     * </p>
     */
    public ObservableList<String> readAllCountryNames() throws SQLException {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        String statement = "SELECT Country FROM countries";
        ResultSet rs;
        rs = conn.prepareStatement(statement).executeQuery();
        while(rs.next()) {countryNames.add(rs.getString("Country"));}
        return countryNames;

    }

    //Not used
    @Override
    public void create(Country country) throws SQLException { }
    @Override
    public void update(Country country) throws SQLException { }
    @Override
    public void delete(Country country) throws SQLException { }
}

package C195RafaelSantos.Data;

import C195RafaelSantos.Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Handles DB operations related to the Division class (first_level_divisions in the DB)
 *
 * @author Rafael Santos
 * @see C195RafaelSantos.Model.Division
 * </p>
 */
public class DivisionDAO implements DAO<Division> {


    /**
     * <p>
     * Returns a ResultSet with the complete first_level_divisions table from the DB
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM first_level_divisions";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Returns an ObservableList with first_level_divisions associated with a given Country
     * @param countryId The country ID to search for associated divisions
     * </p>
     */
    public ObservableList<String> getDivisionsFromCountryId(int countryId) throws SQLException {
        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        String statement = "SELECT Division from first_level_divisions WHERE COUNTRY_ID = "+ countryId;
        ResultSet rs;
        rs = conn.prepareStatement(statement).executeQuery();
        while(rs.next()) {
            filteredDivisions.add(rs.getString("Division"));
        }
        return filteredDivisions;
    }


    //Not Used
    @Override
    public void create(Division division) throws SQLException {}
    @Override
    public void update(Division division) throws SQLException {}
    @Override
    public void delete(Division division) throws SQLException {}
}

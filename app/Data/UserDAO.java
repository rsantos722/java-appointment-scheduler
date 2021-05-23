package C195RafaelSantos.Data;

import C195RafaelSantos.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Handles DB operations related to the User class
 *
 * @author Rafael Santos
 * @see C195RafaelSantos.Model.User
 * </p>
 */
public class UserDAO implements DAO<User>{

    //Initialize Connection
    Connection conn = DatabaseConnection.getConnection();

    /**
     * <p>
     * Returns a ResultSet with every entry in the users table
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM users";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Returns an ObservableList with every User_ID
     * </p>
     */
    public ObservableList<String> readAllUserId() throws SQLException {
        ObservableList<String> userIdList = FXCollections.observableArrayList();
        String statement = "SELECT User_ID FROM users";
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        while(rs.next()) {
            userIdList.add((rs.getString("User_ID")));
        }
        return userIdList;
    }

    //Not used
    @Override
    public void create(User user) throws SQLException {}
    //Wont be used but keep for reference
    @Override
    public void update(User user) throws SQLException {}
    @Override
    public void delete(User user) throws SQLException {}


}

package C195RafaelSantos.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * DAO Interface used by Object DAOs
 *
 * @author Rafael Santos
 * </p>
 */
public interface DAO<Obj> {

    //Initialize Connection
    Connection conn = DatabaseConnection.getConnection();

    /**
     * <p>
     * Create an Object in the respective database
     * @param obj The Object to update
     * </p>
     */
    void create(Obj obj) throws SQLException;

    /**
     * <p>
     * Gets the entire specified table into a ResultSet
     * </p>
     */
    ResultSet readAll() throws SQLException;

    /**
     * <p>
     * Updates the respective entry of a specified Object in the DB
     * @param obj The object to update
     * </p>
     */
    void update(Obj obj) throws SQLException;

    /**
     * <p>
     * Deletes a respective object in the DB
     * @param obj The object to delete
     * </p>
     */
    void delete(Obj obj) throws SQLException;

}

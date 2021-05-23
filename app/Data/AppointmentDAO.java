package C195RafaelSantos.Data;

import C195RafaelSantos.Model.Appointment;
import C195RafaelSantos.Model.SchedulingData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**<p>
 * @author Rafael Santos <br>
 * For WGU C195 <br>
 *
 * DAO and implementation for Appointment Class
 * For Database operations pertaning to Model.Appointment
 * @see C195RafaelSantos.Model.Appointment
 * @see DAO
 *</p>
 */
public class AppointmentDAO implements DAO<Appointment>{


    /**
     * <p>
     * Variable that holds basic SQL connection command
     * </p>
     */
    Connection conn = DatabaseConnection.getConnection();

    /**
     * <p>
     * Creates an appointment in the 'appointments' table of the DB.
     * A statement is appended with appointment info from parameter Appointment a, and then the command is executed
     * @param a The appointment that will be added to the DB
     * @throws SQLException
     * </p>
     */
    @Override
    public void create(Appointment a) throws SQLException {

        String statement =
                "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (" +
                a.getAppointmentId() + ",'" +
                a.getTitle() + "','" +
                a.getDescription() + "','" +
                a.getLocation() + "','" +
                a.getType() + "','" +
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(a.getStartTime().withZoneSameInstant(ZoneId.of("Etc/UTC"))) + "','" +
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(a.getEndTime().withZoneSameInstant(ZoneId.of("Etc/UTC"))) + "'," +
                a.getCustomer().getCustomerId() + "," +
                a.getUser().getId() + "," +
                a.getContact().getId() + ");";

        //System.out.println(statement);
        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Gets every appointment in the DB, and returns the ResultSet of the operation
     * </p>
     */
    public ResultSet readAll() throws SQLException {
        String statement = "SELECT * FROM appointments";
        return conn.prepareStatement(statement).executeQuery();
    }



    /**
     * <p>
     * Updates a specified appointment in the database
     * @param a The appointment (as an Appointment object) to update from the database. The ID of the appointment is used to match the object to the database object.
     * </p>
     */
    @Override
    public void update(Appointment a) throws SQLException {
        int id = a.getAppointmentId();
        String statement =
                "UPDATE appointments SET " +
                "Title='" + a.getTitle() +
                "', Description ='" + a.getDescription() +
                "', Location ='" + a.getLocation() +
                "', Type='" + a.getType() +
                "', Start='" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(a.getStartTime().withZoneSameInstant(ZoneId.of("Etc/UTC"))) +
                "', End= '" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(a.getEndTime().withZoneSameInstant(ZoneId.of("Etc/UTC"))) +
                "', Customer_ID =" + a.getCustomer().getCustomerId() +
                ", User_ID=" + a.getUser().getId() +
                ", Contact_ID=" + a.getContact().getId() +
                " WHERE Appointment_ID =" + id + ";";
        System.out.println(statement);
        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Deletes a specified appointment from the Database
     * @param a The Appointment (as an Object) to delete
     * </p>
     */
    @Override
    public void delete(Appointment a) throws SQLException {
        int id = a.getAppointmentId();
        String statement = "DELETE FROM appointments WHERE Appointment_ID =" + id + ";";
        System.out.println(statement);
        conn.prepareStatement(statement).executeUpdate();
    }

    /**
     * <p>
     * Find the highest Appointment_ID currently in the Database. Used for iterating the Appointment_ID
     * Returns an int with the current highest ID
     * @see C195RafaelSantos.View.AddAppointmentController
     * </p>
     */
    public int largestEntry() throws SQLException {
        String statement = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID DESC LIMIT 0,1";
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        rs.next();
        return rs.getInt("Appointment_ID");
    }


    /**
     * <p>
     * Checks the database for a time conflict with an existing appointment when the program is attempting to save a new one
     * Returns a boolean true if there is a conflict, and otherwise returns false
     * @param newStart A String, formatted for SQL, with the start time of the new appointment
     * @param newEnd String formatted for SQL with the end time of the new appointment
     * @param customerId The Customer_ID for the customer to check for conflicts within
     * </p>
     */
    public boolean conflictCheck(String newStart, String newEnd, int customerId) throws SQLException {
        //Very long SQL statement to check for any conflicts
        String statement = "SELECT Start, End FROM appointments WHERE Customer_ID = "+customerId+" AND (((Start < '"+newStart+"') AND ((End > '"+newStart+"') AND (End < '"+newEnd+"'))) /*Left Overlap*/ OR ((Start > '"+newStart+"') AND (End < '"+newEnd+"')) /*Inner Overlap*/ OR ((Start < '"+newStart+"') AND (End > '"+newEnd+"')) /*Outer Overlap*/ OR ((Start > '"+newStart+"') AND (Start < '"+newEnd+"'))/*Right Overlap*/ OR (Start = '"+newStart+"') /*Same Appointment*/ OR (End = '"+newEnd+"')) /*Same Appointment*/ ";


        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        //If there is nothing in rs, then nothing was returned from SQL statement and no conflicts exist
        //System.out.println("No conflicts");
        return rs.next();
        //System.out.println("About to return true");
    }

    /**
     * <p>
     * Checks the database for a conflict with an existing appointment with the appointment the user is trying to update.
     * Differs from conflictCheck in that the current appointment being updated is not checked for a conflict with itself
     * Returns a boolean true if there is a conflict, and otherwise returns false
     * @param customerId The Customer_ID of the Customer to check for possible time conflicts
     * @param newStart The updated appointment start time
     * @param newEnd The updated appointment end time
     * @param appointmentId The ID of the appointment that is being updated. This appointment ID is ignored when checking the database for a conflict
     * </p>
     */
    public boolean updateConflictCheck(String newStart, String newEnd, int customerId, int appointmentId) throws SQLException {

        //Very long SQL statement to check for any conflicts
        String statement =
                "SELECT Start, End FROM appointments WHERE Customer_ID = "+customerId+" AND Appointment_ID != "+appointmentId+" AND (((Start < '"+newStart+"') AND ((End > '"+newStart+"') AND (End < '"+newEnd+"'))) /*Left Overlap*/ OR ((Start > '"+newStart+"') AND (End < '"+newEnd+"')) /*Inner Overlap*/ OR ((Start < '"+newStart+"') AND (End > '"+newEnd+"')) /*Outer Overlap*/ OR ((Start > '"+newStart+"') AND (Start < '"+newEnd+"'))/*Right Overlap*/ OR (Start = '"+newStart+"') /*Same Appointment*/ OR (End = '"+newEnd+"')) /*Same Appointment*/ ";

        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        //If there is nothing in rs, then nothing was returned from SQL statement and no conflicts exist
        return rs.next();
    }

    /**
     * <p>
     * Returns a ResultSet from Appointments, grouped by the Type and the amount of said type, Used for the Appointment Type Report
     * @see C195RafaelSantos.View.AppointmentReportController
     * </p>
     */
    public ResultSet getAppointmentsByType() throws SQLException {
        String statement = "SELECT DISTINCT Type as Type, COUNT(Type) as AMOUNT FROM appointments GROUP BY Type";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Returns a ResultSet from Appointments, grouped by the Month that the appointment takes place in, and the amount in the month.
     * Used for the Appointments By Month Report
     * @see C195RafaelSantos.View.AppointmentReportController
     * </p>
     */
    public ResultSet getAppointmentsByMonth() throws SQLException {
        String statement = "SELECT MONTH(Start) AS Month, COUNT(Title) as Amount FROM appointments GROUP BY Month";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Returns a ResultSet from Appointments, grouped by the Location and the amount of appointments with the same location
     * Used for the Appointment Location Report
     * @see C195RafaelSantos.View.LocationReportController
     * </p>
     */
    public ResultSet getAmountByLocation() throws SQLException {
        String statement = "SELECT Location, COUNT(Location) AS Amount FROM appointments GROUP BY Location";
        return conn.prepareStatement(statement).executeQuery();
    }

    /**
     * <p>
     * Returns an ObservableList with all appointments with a specified Contact_ID
     * @param contactId The Contact_ID to search for appointments from
     * </p>
     */
    public ObservableList<Appointment> getAppointmentsByContact(int contactId) throws SQLException {
        ObservableList<Appointment> matchedAppointments = FXCollections.observableArrayList();
        String statement = "SELECT * FROM appointments WHERE Contact_ID = " + contactId;
        ResultSet rs = conn.prepareStatement(statement).executeQuery();
        while (rs.next()) {
            Appointment a = new Appointment();
            a.setAppointmentId(rs.getInt("Appointment_ID"));
            a.setTitle(rs.getString("Title"));
            a.setType(rs.getString("Type"));
            a.setDescription(rs.getString("Description"));
            a.setStartTime(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault()));
            a.setEndTime(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
            a.setCustomer(SchedulingData.findCustomer(rs.getInt("Customer_ID")));
            //SchedulingData.findCustomer(rs.getInt("Customer_ID"))
            //rs.getTimestamp("Start").toLocalDateTime().atZone(userZone)
            matchedAppointments.add(a);
        }
        return matchedAppointments;

    }

}

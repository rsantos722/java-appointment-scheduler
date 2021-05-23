package C195RafaelSantos.ReportModel;

/**
 * <p>
 * Object to hold data for the amount of appointments by type report
 * @author Rafael Santos
 * @see C195RafaelSantos.View.AppointmentReportController
 * </p>
 */
public class AmountByType {


    private String type;
    private int amount;


    /**
     * <p>
     * Getter for Type string
     * </p>
     */
    public String getType() { return type; }

    /**
     * <p>
     * Setter for type string
     * </p>
     */
    public void setType(String type) { this.type = type; }

    /**
     * <p>
     * Getter for amount int
     * </p>
     */
    public int getAmount() { return amount; }

    /**
     * <p>
     * Setter for amount int
     * </p>
     */
    public void setAmount(int amount) { this.amount = amount; }


}

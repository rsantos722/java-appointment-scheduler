package C195RafaelSantos.ReportModel;

/**
 * <p>
 * Object to hold data for the Location Report
 * @author Rafael Santos
 * @see C195RafaelSantos.View.LocationReportController
 * </p>
 */
public class AmountByLocation {

    /**
     * <p>
     * Returns the location name
     * </p>
     */
    public String getLocation() {
        return location;
    }

    /**
     * <p>
     * Setter for Location
     * </p>
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * <p>
     * Getter for Amount
     * </p>
     */
    public int getAmount() {
        return amount;
    }

    /**
     * <p>
     * Setter for amount
     * </p>
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    String location;
    int amount;


}

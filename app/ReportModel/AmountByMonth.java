package C195RafaelSantos.ReportModel;

/**
 * <p>
 * Object to hold data for the amount of appointments by month report
 * @author Rafael Santos
 * @see C195RafaelSantos.View.AppointmentReportController
 * </p>
 */
public class AmountByMonth {


    //When month number is used as index, this string will return the month
    String[] monthString = new String[]{ "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private String month;
    private int amount;

    /**
     * <p>
     * Getter for Month string
     * </p>
     */
    public String getMonth() { return month; }

    /**
     * <p>
     * Setter for month string
     * </p>
     */
    public void setMonth(int monthNumber) { this.month = monthString[monthNumber]; }

    /**
     * <p>
     * Getter for month string
     * </p>
     */
    public int getAmount() { return amount; }

    /**
     * <p>
     * Setter for month string
     * </p>
     */
    public void setAmount(int amount) { this.amount = amount; }

}


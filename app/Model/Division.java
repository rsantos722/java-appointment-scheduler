package C195RafaelSantos.Model;

/**
 * <p>
 * Division Class. Keeps track of Division information
 * @author Rafael Santos
 * </p>
 */
public class Division {
    private int divisionId;
    private String divisionName;
    private Country country;

    public Division() {}

    /**
     * <p>
     * Getter for divisionId
     * </p>
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * <p>
     * Setter for divisionId
     * </p>
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * <p>
     * Getter for divisionName
     * </p>
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * <p>
     * Setter for divisionName
     * </p>
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * <p>
     * Getter for country
     * </p>
     */
    public Country getCountry() {
        return country;
    }

    /**
     * <p>
     * Setter for country
     * </p>
     */
    public void setCountry(int countryId) { this.country = SchedulingData.findCountryInt(countryId); }
}

package C195RafaelSantos.Model;

/**
 * <p>
 * Country Class. Keeps track of Country information
 * @author Rafael Santos
 * </p>
 */
public class Country {
    private int countryId;
    private String countryName;

    public Country() {}

    /**
     * <p>
     * Getter for Country ID
     * </p>
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * <p>
     * Setter for Country ID
     * </p>
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * <p>
     * Getter for Country name
     * </p>
     */

    public String getCountryName() {
        return countryName;
    }
    /**
     * <p>
     * Setter for country name
     * </p>
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}

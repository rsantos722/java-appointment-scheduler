package C195RafaelSantos.Model;


/**
 * <p>
 * Customer Class. Keeps track of Customer, as well the associated first level division
 * @author Rafael Santos
 * </p>
 */
public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Division division;

    public Customer() {}

    /**
     * <p>
     * Getter for customer id
     * </p>
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * <p>
     * Setter for customer id
     * </p>
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * <p>
     * Getter for customer name
     * </p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Setter for customer name
     * </p>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Getter for customer address
     * </p>
     */
    public String getAddress() {
        return address;
    }

    /**
     * <p>
     * Setter for customer address
     * </p>
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * <p>
     * Getter for customer postal code
     * </p>
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * <p>
     * Setter for postal code
     * </p>
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * <p>
     * Getter for phone number
     * </p>
     */
    public String getPhone() {
        return phone;
    }

    /**
     * <p>
     * Setter for phone number
     * </p>
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * <p>
     * Getter for first level division
     * </p>
     */
    public Division getDivision() {
        return division;
    }

    /**
     * <p>
     * Setter for first level division
     * </p>
     */
    public void setDivision(Division division) {
        this.division = division;
    }
}

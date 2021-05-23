package C195RafaelSantos.Model;

/**
 * <p>
 * Contact Class. Keeps track of contact information
 * @author Rafael Santos
 * </p>
 */
public class Contact {
    private int id;
    private String name;


    public Contact() {}

    /**
     * <p>
     * Setter for contact ID
     * </p>
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>
     * Setter for contact name
     * </p>
     */
    public void setName(String name) {this.name = name;}

    /**
     * <p>
     * Setter for contact email address
     * </p>
     */
    public void setEmail(String email) {
    }

    /**
     * <p>
     * Getter for contact ID
     * </p>
     */
    public int getId() {
        return id;
    }

    /**
     * <p>
     * Getter for Contact Name
     * </p>
     */
    public String getName() {
        return name;
    }


}

package C195RafaelSantos.Model;


/**
 * <p>
 * User Class. Keeps track of User information
 * @author Rafael Santos
 * </p>
 */
public class User {
    private int id;
    private String name;

    public User() {}

    /**
     * <p>
     * Getter for user id
     * </p>
     */
    public int getId() {
        return id;
    }

    /**
     * <p>
     * Setter for user id
     * </p>
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>
     * Getter for user name
     * </p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Setter for user name
     * </p>
     */
    public void setName(String name) {
        this.name = name;
    }
}

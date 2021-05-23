package C195RafaelSantos.Model;

import C195RafaelSantos.Data.AppointmentDAO;
import C195RafaelSantos.Data.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

/**
 * <p>
 * SchedulingData class that stores an ObservableList for each Object in use.
 * Used by the View controller to populate tables and save data
 * @author Rafael Santos
 * </p>
 */
public class SchedulingData {

    private static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static final ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static final ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * <p>
     * Adds a provided Appointment object to the allAppointments ObservableList, and then updates the DB with the new appointment.
     * @param appointment The Appointment to add
     * </p>
     */
    public static void addAppointment(Appointment appointment) throws SQLException {
        allAppointments.add(appointment);
        AppointmentDAO dao = new AppointmentDAO();
        dao.create(appointment);
    }

    /**
     * <p>
     * Adds a provided Customer object to the allCustomers ObservableList, and then updates the DB with the new customer.
     * @param customer The Appointment to add
     * </p>
     */
    public static void addCustomer(Customer customer) throws SQLException {
        allCustomers.add(customer);
        CustomerDAO dao = new CustomerDAO();
        dao.create(customer);
    }

    /**
     * <p>
     * Updates a specified appointment in the Appointment ObservableList and in the DB
     * @param appointment The appointment to update
     * </p>
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        
        int originalAppointmentId = appointment.getAppointmentId();
        Appointment appointmentToUpdate = findAppointment(originalAppointmentId);
        allAppointments.set(allAppointments.indexOf(appointmentToUpdate), appointment);
        AppointmentDAO dao = new AppointmentDAO();
        dao.update(appointment);

    }

    /**
     * <p>
     * Updates a specified customer in SchedulingData and in the DB
     * @param customer The customer to update
     * </p>
     */
    public static void updateCustomer(Customer customer) throws SQLException {

        //Identify customer to update
        int customerId = customer.getCustomerId();
        Customer customerToUpdate = findCustomer(customerId);

        //Update customer with new data
        allCustomers.set(allCustomers.indexOf(customerToUpdate),customer);

        //Update DB
        CustomerDAO dao = new CustomerDAO();
        dao.update(customer);
    }

    /**
     * <p>
     * Deletes a specified appointment in this object and the DB
     * @param appointment The appointment to delete
     * </p>
     */
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        AppointmentDAO dao = new AppointmentDAO();
        dao.delete(appointment);
        allAppointments.remove(appointment);

    }

    /**
     * <p>
     * Deletes a specified appointment in this object. Due to FK constraints, this also deletes any appointments associated with the customer, and checks for this beforehand
     * @param customer The customer to delete
     * </p>
     */
    public static void deleteCustomer(Customer customer) throws SQLException {

        boolean deleteAppointmentCheck = false;
        //If there is an appointment associated with the customer, delete appointments
        ObservableList<Appointment> appointmentsToRemove = FXCollections.observableArrayList();
        for(Appointment a : allAppointments) {
            if(a.getCustomer() == customer) {
                deleteAppointmentCheck = true;
                System.out.println("Found appointment: " + a);
                appointmentsToRemove.add(a);
            }
        }

        if (deleteAppointmentCheck) {
            for (Appointment a : appointmentsToRemove) {
                System.out.println("Starting deletion loop");
                System.out.println("a ==" + a + " and appointmentsToRemove = " + appointmentsToRemove);
                if(appointmentsToRemove.contains(a)) {
                    System.out.println("Removing appointment: "+a);
                    deleteAppointment(a);
                }
            }
        }

        //Remove from Customer DB
        CustomerDAO dao = new CustomerDAO();
        dao.delete(customer);

        //Remove from object
        allCustomers.remove(customer);
    }

    /**
     * <p>
     * Returns a ObservableList of every appointment
     * </p>
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * <p>
     * Returns an ObservableList of every customer
     * </p>
     */
    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}

    /**
     * <p>
     * Returns the appointment with the specified appointment id
     * </p>
     */
    public static Appointment findAppointment(int appointmentId) {
        Appointment matchedAppointment = new Appointment();
        for (Appointment a : allAppointments) {
            if(a.getAppointmentId() == appointmentId) {
                matchedAppointment = a;
            }
        }
        return matchedAppointment;
    }

    /**
     * <p>
     * Returns teh contact with the specified contact id
     * </p>
     */
    public static Contact findContact(int contactId) {
        Contact matchedContact = new Contact();
        for (Contact c : allContacts) {
            if(c.getId() == contactId) {
                matchedContact = c;
            }
        }
        return matchedContact;
    }

    /**
     * <p>
     * Returns a country with the given Country name string
     * </p>
     */
    public static Country findCountry(String countryName) {
        Country matchedCountry = new Country();
        for (Country c : allCountries) {
            if(c.getCountryName().equals(countryName)) {
                matchedCountry = c;
            }
        }
        return matchedCountry;
    }

    /**
     * <p>
     * Returns a country by a given country id
     * </p>
     */
    public static Country findCountryInt(int countryId) {
        Country matchedCountry = new Country();
        for (Country c : allCountries) {
            if(c.getCountryId() == countryId) {
                matchedCountry = c;
            }
        }
        return matchedCountry;
    }

    /**
     * <p>
     * Finds a customer with the given customer id
     * </p>
     */
    public static Customer findCustomer(int customerId) {
        Customer matchedCustomer = new Customer();
        for (Customer c : allCustomers) {
            if(c.getCustomerId() == customerId) {
                matchedCustomer = c;
            }
        }
        return matchedCustomer;
    }

    /**
     * <p>
     * Finds a contact by the given Contact name string
     * </p>
     */
    public static Contact findContactByName(String contactName) {
        Contact matchedContact = new Contact();
        for (Contact c : allContacts) {
            if(c.getName().equals(contactName)) {
                matchedContact = c;
            }
        }
        return matchedContact;
    }

    /**
     * <p>
     * Returns the first level division with the given division id
     * </p>
     */
    public static Division findDivisionInt(int divisionId) {
        Division matchedDivision = new Division();
        for (Division d : allDivisions) {
            if(d.getDivisionId() == divisionId) {
                matchedDivision = d;
            }
        }
        return matchedDivision;
    }

    /**
     * <p>
     * Returns a first level division using a given division name string
     * </p>
     */
    public static Division findDivision(String divisionName) {
        Division matchedDivision = new Division();
        for (Division d : allDivisions) {
            if(d.getDivisionName().equals(divisionName)) {
                matchedDivision = d;
            }
        }
        return matchedDivision;
    }

    /**
     * <p>
     * Finds a user using a given user ID
     * </p>
     */
    public static User findUser(int userId) {
        User matchedUser = new User();
        for (User u : allUsers) {
            if(u.getId() == userId) {
                matchedUser = u;
            }
        }
        return matchedUser;
    }


    //Initialize lists on application startup (Take from DB)
    public static void initialAddAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }
    public static void initialAddContact(Contact contact) {
        allContacts.add(contact);
    }
    public static void initialAddCountry(Country country) {
        allCountries.add(country);
    }
    public static void initialAddCustomer(Customer customer) {allCustomers.add(customer);}
    public static void initialAddDivision(Division division) {allDivisions.add(division);}
    public static void initialAddUser(User user) {allUsers.add(user);}

    /**
     * <p>
     * Returns a contact with a matching contact name String
     * </p>
     */
    public static int getContactByName(String contactName) {
        Contact matchedContact = new Contact();
        for(Contact c : allContacts) {
            if (contactName.equals(c.getName())) {
                matchedContact = c;
            }
        }
        return matchedContact.getId();
    }

}

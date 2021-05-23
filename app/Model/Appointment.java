package C195RafaelSantos.Model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * Handles data from the Appointment table of DB, including getters and setters
 * </p>
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private Customer customer;
    private User user;
    private Contact contact;
    private String contactName;
    private String startTimeString;
    private String endTimeString;
    private String dateString;


    public Appointment() {}

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        this.startTimeString = startTime.format(startEndTimeFormat);
        this.dateString = startTime.format(dateFormat);
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        this.endTimeString = endTime.format(startEndTimeFormat);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getCustomerId() {return customer.getCustomerId();}
    public String getContactName() {return contact.getName();}

    DateTimeFormatter startEndTimeFormat = DateTimeFormatter.ofPattern("hh:mma");
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public String getStartTimeString() {
        //ZonedDateTime timeAtLocalTimeZone = startTime.withZoneSameInstant(ZoneId.systemDefault());
        return startTime.format(startEndTimeFormat);
    }
    public String getEndTimeString() {
        //ZonedDateTime timeAtLocalZone = endTime.withZoneSameInstant(ZoneId.systemDefault());
        return endTime.format(startEndTimeFormat);
    }
    public String getDateString() {return startTime.format(dateFormat);}

}

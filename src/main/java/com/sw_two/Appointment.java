package com.sw_two;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
/**
This class represents a Appointment in a scheduling system.
 It stores information about the appointment ie type, contact, title, Description, location, start and time .etc
@author Ohzecodes
@version 1.0 
 */


public class Appointment {
    private  Contact contact;
    private String TYPE;
    private int appointmentid;
    private String title;
    private String description;
    private String location;
    private int customerid;
    private int userid;
    private ZonedDateTime localStartDateTime;
    private ZonedDateTime localEndDateTime;

    /**
     This class represents an appointment in a scheduling system.
     @param appointmentid The unique identifier for this appointment.
     @param title The title of the appointment.
     @param description A brief description of the appointment.
     @param location The location of the appointment.
     @param TYPE The type of appointment (e.g. "meeting", "call", etc.)
     @param localStartDateTime The start time of the appointment in the local timezone.
     @param localEndDateTime The end time of the appointment in the local timezone.
     @param customerid The unique identifier for the customer associated with this appointment.
     @param userid The unique identifier for the user associated with this appointment.
     @param contact The contact information for the customer or user associated with this appointment.
     */
    public Appointment(int appointmentid, String title, String description, String location,String TYPE,
                       ZonedDateTime localStartDateTime, ZonedDateTime localEndDateTime,
                       int customerid, int userid, Contact contact) {
        this.TYPE=TYPE;
        this.appointmentid = appointmentid;
        this.title = title;
        this.description = description;
        this.location = location;
        this.customerid = customerid;
        this.userid = userid;
        this.localStartDateTime=localStartDateTime;
        this.localEndDateTime=localEndDateTime;
        this.contact=contact;
    }

    /**
     Returns a string representation of this Appointment object.
     @return A string containing the appointment ID, title, description, location, customer ID, user ID, start date and time, and end date and time.
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentid=" + appointmentid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", customerid=" + customerid +
                ", userid=" + userid +
                ", localStartDateTime=" + localStartDateTime +
                ", localEndDateTime=" + localEndDateTime +
                '}';
    }

    /**
     Returns the unique identifier for this appointment.
     @return The appointment ID.
     */
    public int getAppointmentid() {
        return appointmentid;
    }
    /**
     Returns the title of this appointment.
     @return The appointment title.
     */
    public String getTitle() {
        return title;
    }
    /**
     Sets the title of this appointment.
     @param title The new appointment title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     Returns the description of this appointment.
     @return The appointment description.
     */
    public String getDescription() {
        return description;
    }
    /**
     Sets the description of this appointment.
     @param description The new appointment description.
     */

    public void setDescription(String description) {
        this.description = description;
    }
    /**
     Returns the location of this appointment.
     @return The appointment location.
     */
    public String getLocation() {
        return location;
    }
    /**

     Sets the location of this appointment.
     @param location The new appointment location.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**

     Returns the unique identifier for the customer associated with this appointment.
     @return The customer ID.
     */
    public int getCustomerid() {
        return customerid;
    }
    /**
     Sets the unique identifier for the customer associated with this appointment.
     @param customerid The new customer ID.
     */
    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    /**
     Returns the unique identifier for the user associated with this appointment.
     @return The user ID.
     */
    public int getUserid() {
        return userid;
    }

    /**
     Sets the unique identifier for the user associated with this appointment.
     @param userid The new user ID.
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }
    //    ------------------------- Start

    /**
     Returns the start time of this appointment in the local timezone.
     @return The start time as a ZonedDateTime object.
     */
    public ZonedDateTime getLocalStartDateTime() {
        return localStartDateTime;
    }

    /**
     * Sets the start time of this appointment in the local timezone.
     * @param localStartDateTime The new start time as a Zoned

     */
    public void setLocalStartDateTime(ZonedDateTime localStartDateTime) {
        this.localStartDateTime =localStartDateTime;
    }

    /**
     Returns the date of the start time of this appointment.
     @return The start date as a LocalDate object.
     */
    public LocalDate getStartDate(){
        return localStartDateTime.toLocalDate();
    }
    /**
     Returns the time of the start time of this appointment.
     @return The start time as a LocalTime object.
     */
    public LocalTime getStartTime(){
        return localStartDateTime.toLocalTime();
    }


    //    -------------------------- End

    /**
     Returns the end time of this appointment in the local timezone.
     @return The end time as a ZonedDateTime object.
     */
    public ZonedDateTime getLocalEndDateTime() {
        return localEndDateTime;
    }
    /**
     Sets the end time of this appointment in the local timezone.
     @param localEndDateTime The new end time as a ZonedDateTime object.
     */
    public void setLocalEndDateTime(ZonedDateTime localEndDateTime) {
        this.localEndDateTime = localEndDateTime;
    }

    /**
        Returns the date of the end time of this appointment.
        @return The end date as a LocalDate object.
    */
        public LocalDate getEndDate(){
        return localEndDateTime.toLocalDate();
    }
    /**

        Returns the time of the end time of this appointment.
        @return The end time as a LocalDate object.
    */
        public LocalTime getEndTime(){
            return localEndDateTime.toLocalTime();
        }

        /**

        Returns the time of the end time of this appointment.
        @return The end time as a LocalTime object.
        */
        public String getTYPE() {
            return TYPE;
        }

        /**
        Sets the type of this appointment.
        @param TYPE The new appointment type.
        */
        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }


        /**
        Returns the  Contact of the appointment .
        @return The Contact object.
        */
        private Contact getContact() {
            return this.contact;
        }
        /**
        Returns the unique identifier for the contact associated with this appointment.
        @return The contact ID.
        */
        public int getContactId(){
            return this.getContact().getContactId();
        }
        /**
        Returns the Name for the contact associated with this appointment.
        @return The contact Name.
        */
        public String getCONTACTNAME(){
            return this.getContact().getContactName();
        }
}

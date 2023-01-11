package com.sw_two;

/**
This class Is used to define a contact that an appointment has
@author Ohzecodes
@version 1.0 
 */

public class Contact {
    private  int ContactId;

    @Override
    public String toString() {
        return "Contact{" +
                "ContactId=" + ContactId +
                ", ContactName='" + ContactName + '\'' +
                '}';
    }

    private String ContactName;

    /**
     * this class represent a contect for the appointment
     * @param contactId id of the contact
     * @param contactName name of the contact
     * @param contactEmail email of the contact
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.ContactId = contactId;
        this.ContactName =contactName;

    }

    /**
     * returns the id of the contact
     * @return the id of the contact
     */
    public int getContactId() {
        return ContactId;
    }

    /**
     * sets the contact id
     * @param contactid the new contact id
     */
    public void setContactId(int contactid) {
        ContactId = contactid;
    }

    /**
     * return the name of the contact
     * @return name of the contact
     */
    public String getContactName() {
        return ContactName;
    }

    /**
     * set the name of contact
     * @param contactName the new name of the contact
     */
    public void setContactName(String contactName) {
        ContactName = contactName;
    }
}

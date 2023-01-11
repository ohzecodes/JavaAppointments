package com.sw_two;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.ZonedDateTime;
/**
This class represents a customer in a scheduling system. It stores information about the customer's
name, address, phone number, location, and other details.
@author Ohzecodes
@version 1.0
/ */
public class Customer {

    private int CUSTOMER_ID;
    private String CUSTOMER_NAME,CREATED_BY,LAST_UPDATED_BY;
    private String CUSTOMER_ADDRESS;
    private String PHONE;
    private Division STATE;
    private ZonedDateTime CREATED_ON;
    private ZonedDateTime LAST_UPDATE_ON;
    private Country Country;
    private String Postal;
    /**

    This class represents a customer in a scheduling system.
    @param CUSTOMER_ID The unique identifier for this customer.
    @param CUSTOMER_NAME The name of the customer.
    @param CUSTOMER_ADDRESS The address of the customer.
    @param PHONE The phone number of the customer.
    @param STATE The division (e.g. state or province) of the customer's address.
    @param country The country of the customer's address.
    @param Postal The postal code of the customer's address.
    @param CREATED_ON The date and time that this customer was created.
    @param LAST_UPDATE_ON The date and time of the last update to this customer's information.
    @param LAST_UPDATED_BY The user who last updated this customer's information.
    */
    public Customer(int CUSTOMER_ID, String CUSTOMER_NAME, String CUSTOMER_ADDRESS, String PHONE, Division STATE, Country country,
                    String Postal, ZonedDateTime CREATED_ON, ZonedDateTime LAST_UPDATE_ON, String LAST_UPDATED_BY, String CREATED_BY ) {
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.CUSTOMER_ADDRESS = CUSTOMER_ADDRESS;
        this.PHONE = PHONE;
        this.STATE = STATE;
        this.CREATED_ON=CREATED_ON;
        this.LAST_UPDATE_ON=LAST_UPDATE_ON;
        this.Country=country;
        this.Postal=Postal;
        this.LAST_UPDATED_BY=LAST_UPDATED_BY;
        this.CREATED_BY=CREATED_BY;
    }

    /**
     * the string representation of the object
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "Customer{" +
                "CUSTOMER_ID=" + CUSTOMER_ID +
                ", CUSTOMER_NAME='" + CUSTOMER_NAME + '\'' +
                ", CUSTOMER_ADDRESS='" + CUSTOMER_ADDRESS + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", STATE='" + STATE + '\'' +
                ", CREATED_ON=" + CREATED_ON +
                ", LAST_UPDATE_ON=" + LAST_UPDATE_ON +
                ", Country='" + Country + '\'' +
                ", Postal='" + Postal + '\'' +
                '}';
    }

    /**
     * STATE or Division of the customer
     * @return STATE or Division of the customer
     */
    public Division getSTATE() {
        return STATE;
    }

    /**
     *
     * @return the country where the customer lives
     */
    public Country getCountry() {
        return Country;
    }

    /**
     * sets the country of the customer
     * @param country
     */
    public void setCountry(Country country) {
        this.Country = country;
    }

    /**
     *
     * @return the id of customer
     */
    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    /**
     *
     * @return the name of costumer
     */
    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    /**
     * sets the name of customer
     * @param CUSTOMER_NAME  new c name
     */
    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }
    /**
     *
     * @return the address of CUSTOMER
     */
    public String getCUSTOMER_ADDRESS() {
        return CUSTOMER_ADDRESS;
    }

    /**
     *
     * @return the PHONE NUMBER of CUSTOMER
     */
    public String getPHONE() {
        return PHONE;
    }

    /**
     * @return  CUSTOMER's postal  code
     */
    public String getPOSTAL() {
        return this.Postal;
    }

    /**
     *
     * @return name of user who created the customer
     */
    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public String getLAST_UPDATED_BY() {
        return LAST_UPDATED_BY;
    }
    public ZonedDateTime getLAST_UPDATE_ON() {
        return LAST_UPDATE_ON;
    }
    public ZonedDateTime getCREATED_ON() {
        return CREATED_ON;
    }
}

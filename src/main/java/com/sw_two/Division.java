package com.sw_two;
/**
This class defines a division or state that a country may have
@author Ohzecodes
@version 1.0  */
public class Division {
    private int id;
    private String name;
    private int countryId;

    /**
     * this class defined the division of a country
     *
     * @param id        the division id
     * @param name      the division name
     * @param countryId the Country id
     */
    public Division(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    /**
     * @return the name of division
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * @return the division id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return devision name
     */

    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * set a new of country
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the country id
     */
    public int getCountryId() {
        return countryId;
    }

}

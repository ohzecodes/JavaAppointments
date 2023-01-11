package com.sw_two;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
This class Defines a country object that has divisions and a Customer may live in
@author Ohzecodes
@version 1.0  */
public class Country {
    private int id;
    private String name;
    private ObservableList<Division> divisions;

    /**
     * this creates a new country object
     * @param id id of the country
     * @param name name of the country
     */
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of the country
     * @return id of the country
     */
    public int getId() {
        return this.id;
    }

    /**
     * returns name of the country
     * @return name  of the country
     */
    public String getName() {
        return this.name;
    }

    /**
     * division (State) of the country as list
     * @return division (State) of the country
     */
    public ObservableList<Division> getDivisions() {
        return this.divisions;
    }

    /**
     * set the country id
     * @param id the new country id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets the new name of the country
     * @param name the new name of the country
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the divisions of a country
     * @param divisions an arraylist of the divisions of a country
     */
    public void setDivisions(ObservableList<Division> divisions) {
        this.divisions = divisions;
    }

    /**
     * add a new division to the country
     * @param division the new division
      */

    public void addDivision(Division division) {
        this.divisions.add(division);
    }

    /**
     *
     * @return the countries' name
     */
    @Override
    public String toString() {
       return this.name;
    }

}

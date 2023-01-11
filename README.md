# Appointment Management System 

## Purpose 
The purpose of this project is to develop a GUI-based scheduling desktop application 
for a global consulting organization with offices in multiple locations and languages. 
The application pulls data from a MySQL database provided by the consulting organization 
and meets specific business requirements outlined by the organization.


## Languages

If your system is on any other language the software will be displayed in english
If you change you your language, you will  need to restart the program.
Current Language support:
1. English (Default)
2. Canadian French: (If your system is using any other variant of the language, the code will detect it and set itself to this).

### Language feature is tested on
I have only tested the program on these languages, but I am sure other variants are supported

1. French(Canada)
2. French (Morroco) -> this will be convert to French (Canada)
3. French (Other) -> this will be convert to French (Canada)
4. English (United States)

If your system is on any other language the software will be displayed in english 

## Environment Used
* IntelliJ IDEA 2021.1.3 (Community Edition)
* java SE "17.0.1" 2021-10-19 LTS
* JavaFX-SDK-17.0.1
* mysql-connector-java-8.0.25
 

## Usage
[//]: # (directions for how to run the program)
* open this project using IntelliJ and run this file /src/main/java/com/sw_two/ScheduleApp.java

## Additional Report
[//]: # (a description of the additional report of your choice you ran in part A3f)
My additional Report finds the total number of customer appointments organized by customer name. 

## lamda functions
1. A code in the `find()` method  in  `src/main/java/com/sw_two/ReportOfAppointmentByContact.java` uses generate the number of each appointment made by each distinct and stores it to count variable 
2.  The code in the `initialize()` method in `src/main/java/com/sw_two/ReportByTypeCtrl.java`  uses multiple lambda functions to set actions on each of the radio buttons for months. These  actions are also set using lambda functions, which unset the count and pass the chosen month to the find() function.

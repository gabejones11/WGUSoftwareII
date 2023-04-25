package dev.gabriel.schedulingapplication.Utility;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.dbhelper.DBAppointments;
import javafx.scene.control.Alert;

import java.time.*;
public class Utility {

    private Utility() {}

    //generic header and title
    public static final String TITLE = "Error";
    public static final String HEADER = "Invalid entry";

    //error messages for appointment validation
    public static final String TITLE_ERROR = "Please enter a valid appointment title";
    private static final String TYPE_ERROR = "Please enter a valid appointment type";
    private static final String DESCRIPTION_ERROR = "Please enter a valid appointment description";
    private static final String LOCATION_ERROR = "Please enter a valid appointment location";
    private static final String START_END_DATE_TIME_ERROR = "Please enter a valid start and end date/time for the appointment (Business Hours: All week 8:00AM-10:00PM EST)";
    private static final String CUSTOMER_ID_ERROR = "Please enter a valid customer ID for the appointment";
    private static final String USER_ID_ERROR = "Please enter a valid user ID for the appointment";
    private static final String CONTACT_ERROR = "Please enter a valid contact for the appointment";
    private static final String APPOINTMENT_COLLISION_ERROR_ID = "There is a conflict with this appointment and appointment with ID: ";
    private static final String APPOINTMENT_COLLISION_ERROR_START = " that is scheduled to start at: ";
    private static final String APPOINTMENT_COLLISION_ERROR_END = " and is scheduled to end at: ";
    private static final String APPOINTMENT_COLLISION_ERROR_FINAL = " please make an adjustment to this appointment or appointment with ID: ";

    //error messages for customer validation
    private static final String NAME_ERROR = "Please enter a valid customer name";
    private static final String ADDRESS_ERROR = "Please enter a valid customer address";
    private static final String POSTAL_CODE_ERROR = "Please enter a valid postal code";
    private static final String PHONE_NUMBER_ERROR = "Please enter a valid phone number";
    private static final String COUNTRY_ERROR = "Please enter a valid customer country";
    private static final String STATE_PROV_ERROR = "Please enter a valid customer state/province";

    /**
     * This method generates an error message and takes three argument, title, header, and content where the content
     * is the specific of the type of error that has occured
     * @param title
     * @param header
     * @param content
     */
    //error message generator
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method validates the title by making sure it is not empty
     * @param title
     * @return
     */
    //appointments validation
    public static boolean validateAppointmentTitle(String title) {
        if (title.trim().isEmpty()) {
            showError(TITLE, HEADER, TITLE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the type by making sure it is not empty
     * @param type
     * @return
     */
    public static boolean validateAppointmentType(String type) {
        if (type.trim().isEmpty()) {
            showError(TITLE, HEADER, TYPE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the description by making sure it is not empty
     * @param description
     * @return
     */
    public static boolean validateAppointmentDescription(String description) {
        if (description.trim().isEmpty()) {
            showError(TITLE, HEADER, DESCRIPTION_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the location by making sure it is not empty
     * @param location
     * @return
     */
    public static boolean validateAppointmentLocation(String location) {
        if (location.trim().isEmpty()) {
            showError(TITLE, HEADER, LOCATION_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the appointment start and end date and time
     * First we make sure that the entered date and time is not null before doing our calculation or else we will cause our program to
     * crash. After that we create a variable named easternTime that will represent the zoneID of our business hours.
     * Next we create a business hours start, and end variable which will hold the local time of 8AM and 10PM respectively, we also have
     * to give a date to this parameter for validation and since the requirements do not specify any date restrictions (besides the logical check that
     * the appointment start date is not before the appointment end date) we pass the appointment start date and end date as parameters here.
     * Next we do a logical statement check of whether the start of the appointment is before business hours, the end is after business hours,
     * and whether the end time is before the start time
     * @param startTime
     * @param endTime
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean validateAppointmentStartEndDateTime(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        //check this first because we don't want to do calculations if the fields are null
        if (startTime == null || endTime == null || startDate == null || endDate == null) {
            showError(TITLE, HEADER, START_END_DATE_TIME_ERROR);
            return false;
        }

        //define our business time zone
        ZoneId easternTime = ZoneId.of("America/New_York");

        //making business hours start and business hours end objects that have an eastern time zone
        //we pass the start date and end date as parameters here because we know the business is open every day,
        //so we don't need to worry about date exclusion
        //maybe add the localdate.now() as the start date parameter? based on requirements it doesn't say if we can make an appointment
        //that has a date set in the past, but it seems logical to check for that
        ZonedDateTime businessHoursStart = ZonedDateTime.of(startDate, LocalTime.of(8, 0), easternTime);
        ZonedDateTime businessHoursEnd = ZonedDateTime.of(endDate, LocalTime.of(22, 0), easternTime);

        //converting our date and time to eastern time
        ZonedDateTime startDateTime = ZonedDateTime.of(startDate, startTime, ZoneId.systemDefault()).withZoneSameInstant(easternTime);
        ZonedDateTime endDateTime = ZonedDateTime.of(endDate, endTime, ZoneId.systemDefault()).withZoneSameInstant(easternTime);

        if (startDateTime.isBefore(businessHoursStart) || endDateTime.isAfter(businessHoursEnd) || endDateTime.isBefore(startDateTime)) {
            showError(TITLE, HEADER, START_END_DATE_TIME_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the customer ID by making sure it is not empty
     * @param customerID
     * @return
     */
    public static boolean validateAppointmentCustomerID(Integer customerID) {
        if (customerID == null) {
            showError(TITLE, HEADER, CUSTOMER_ID_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the User ID by making sure it is not empty
     * @param userID
     * @return
     */
    public static boolean validateAppointmentUserID(Integer userID) {
        if (userID == null) {
            showError(TITLE, HEADER, USER_ID_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method validates the contact ID by making sure it is not empty
     * @param contactID
     * @return
     */
    public static boolean validateAppointmentContactID(Integer contactID) {
        if (contactID == null) {
            showError(TITLE, HEADER, CONTACT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method checks whether a customer has overlapping appointments we start by looping through all of our appointments
     * and checking if the appointment has the same customer ID but not the same appointment ID to make sure that we don't get this error
     * when modifying an appointment.  If we find an appointment(s) we then grab the start and end date and time and check
     * if the appointment we are comparing this one too has a start time within the same time frame as the original, if it does we display an error
     * @param newStart
     * @param newEnd
     * @param customerID
     * @param appointmentID
     * @return
     */
    //we should probably check for overlapping appointments for the contacts as well?
    public static boolean validateAppointmentCollisions(LocalDateTime newStart, LocalDateTime newEnd, int customerID, int appointmentID) {
        for (Appointments appointment : DBAppointments.getAllAppointments()) {
            if (appointment.getCustomerID() == customerID && appointment.getAppointmentID() != appointmentID) {
                LocalDateTime start = appointment.getStart();
                LocalDateTime end = appointment.getEnd();
                if ((newStart.isAfter(start) && newStart.isBefore(end)) || (newEnd.isAfter(start) && newEnd.isBefore(end)) || (newStart.isEqual(start) || newEnd.isEqual(end))) {
                    showError(TITLE, HEADER, APPOINTMENT_COLLISION_ERROR_ID + appointment.getAppointmentID() + APPOINTMENT_COLLISION_ERROR_START + start +
                            APPOINTMENT_COLLISION_ERROR_END + end + APPOINTMENT_COLLISION_ERROR_FINAL + appointment.getAppointmentID());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method handles validation of the customer name and makes sure it is not empty
     * @param customerName
     * @return
     */
    //customer validation
    public static boolean validateCustomerName(String customerName) {
        if (customerName.trim().isEmpty()) {
            showError(TITLE, HEADER, NAME_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method handles validation of the customer address and makes sure it is not empty
     * @param customerAddress
     * @return
     */
    public static boolean validateCustomerAddress(String customerAddress) {
        if (customerAddress.trim().isEmpty()) {
            showError(TITLE, HEADER, ADDRESS_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method handles validation of the customer postal code and makes sure it is not empty
     * @param postalCode
     * @return
     */
    public static boolean validateCustomerPostalCode(String postalCode) {
        if (postalCode.trim().isEmpty()) {
            showError(TITLE, HEADER, POSTAL_CODE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method handles validation of the customer phone number and makes sure it is not empty
     * @param phoneNumber
     * @return
     */
    public static boolean validateCustomerPhoneNumber(String phoneNumber) {
        if (phoneNumber.trim().isEmpty()) {
            showError(TITLE, HEADER, PHONE_NUMBER_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method handles validation of the customer country and makes sure it is not empty
     * @param country
     * @return
     */
    public static boolean validateCustomerCountry(String country) {
        if (country == null) {
            showError(TITLE, HEADER, COUNTRY_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method handles validation of the customer state/providence and makes sure it is not empty
     * @param stateProv
     * @return
     */
    public static boolean validateCustomerStateProvidence(String stateProv) {
        if (stateProv == null) {
            showError(TITLE, HEADER, STATE_PROV_ERROR);
            return false;
        }
        return true;
    }
}

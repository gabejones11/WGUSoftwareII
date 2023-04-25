package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.ReportModels.AppointmentsByTypeMonth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;

public class DBAppointments {

    /**
     * This method grabs an observable list of all of our appointments in the database
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {
        //appointment list
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            //sql query
            String sql = "SELECT * FROM appointments";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointmentList;
    }

    /**
     * This method adds an appointment to the database and returns an int (generated key) that we can use to make an appointment object for the unique ID
     * I don't know if it is needed or not to make an Appointment object, but I did it anyway
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @return
     */
    public static int addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                                     int customerID, int userID, int contactID) {

        try {
            //query
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
            preparedStatement.setInt(7, customerID);
            preparedStatement.setInt(8, userID);
            preparedStatement.setInt(9, contactID);

            //execute query
            preparedStatement.executeUpdate();

            //retrieve the generated ID Value from the DB
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated ID value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method handles deleting an appointment from the database taking an appointment ID as an argument
     * @param appointmentID
     */
    public static void deleteAppointment(int appointmentID) {
       try{
           //query
           String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
           //prepared statement
           PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
           preparedStatement.setInt(1, appointmentID);
           //execute query
           preparedStatement.executeUpdate();
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    /**
     * This method handles updating an appointment taking an appointment object as an argument
     * @param appointment
     */
    public static void updateAppointment(Appointments appointment) {
        try {
            //query
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? "
                    + "WHERE Appointment_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setString(1, appointment.getTitle());
            preparedStatement.setString(2, appointment.getDescription());
            preparedStatement.setString(3, appointment.getLocation());
            preparedStatement.setString(4, appointment.getType());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            preparedStatement.setInt(7, appointment.getCustomerID());
            preparedStatement.setInt(8, appointment.getUserID());
            preparedStatement.setInt(9, appointment.getContactID());
            preparedStatement.setInt(10, appointment.getAppointmentID());
            //execute query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method returns a list of appointments that are filtered for the current month
     * @return
     */
    public static ObservableList<Appointments> appointmentsListByMonth() {
        //appointments list
        ObservableList<Appointments> appointmentsByMonthList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM Appointments WHERE MONTH(START) = MONTH(CURRENT_DATE())";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointmentsByMonthList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointmentsByMonthList;
    }

    /**
     * This method returns a list of appointments that are filtered for the current week
     * @return
     */
    public static ObservableList<Appointments> appointmentsListByWeek() {
        //appointments list
        ObservableList<Appointments> appointmentsByWeekList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM Appointments WHERE WEEK(START) = WEEK(CURRENT_DATE())";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointmentsByWeekList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointmentsByWeekList;
    }

    /**
     * This method checks if there is an appointment for a specific customer ID to make sure that no customers are deleted before
     * their appointments have been deleted first. It takes a customer ID as an argument.
     * @param customerID
     * @return
     */
    public static boolean checkForCustomerAppointment(int customerID) {
        try {
            //query
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method returns a list of appointments that correspond with a specific contact ID. It takes a contact ID as an argument.
     * @param contactID
     * @return
     */
    public static ObservableList<Appointments> getAllAppointmentsByContactID(int contactID) {
        //appointments by contact list
        ObservableList<Appointments> appointmentsByContactList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, contactID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while(resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointmentsByContactList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointmentsByContactList;
    }

    /**
     * This method returns a list of appointments that conform the AppointmentByTypeMonth class that I made specifically for the report screen
     * This method selects the start month, type, and makes a count value, from the appointments table. Then it groups the results by the month
     * and type so that we can have a list of AppointmentByTypeMonth Objects
     * @return
     */
    public static ObservableList<AppointmentsByTypeMonth> getAppointmentsByTypeMonth() {
        //appointments by type and month list
        ObservableList<AppointmentsByTypeMonth> appointmentsByTypeMonthList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT MONTH(Start) AS Month, Type, COUNT(*) AS Count " +
                    "FROM appointments " +
                    "GROUP BY MONTH(Start), Type";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int monthNumber = resultSet.getInt("Month");
                String monthName = Month.of(monthNumber).toString();
                String type = resultSet.getString("Type");
                int count = resultSet.getInt("Count");

                AppointmentsByTypeMonth appointmentsByTypeMonth = new AppointmentsByTypeMonth(monthName, type, count);
                appointmentsByTypeMonthList.add(appointmentsByTypeMonth);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointmentsByTypeMonthList;
    }
}

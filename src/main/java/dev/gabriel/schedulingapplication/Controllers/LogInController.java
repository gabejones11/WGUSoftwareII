package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.Models.Users;
import dev.gabriel.schedulingapplication.dbhelper.DBAppointments;
import dev.gabriel.schedulingapplication.dbhelper.DBUsers;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static dev.gabriel.schedulingapplication.Main.switchScreen;

public class LogInController implements Initializable {
    @FXML
    private Label loginLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label timeZoneDisplayLabel;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Button exitButton;
    ResourceBundle resourceBundle;

    /**
     * This function handles user login when the login button is pressed. The first thing we do is grab the user input from the text fields.
     * After this we create a list of users from the database that includes the usernames and passwords for each user.
     * Next we make a LocalDateTime object called time which stores the time of this current moment for the print writer to write the
     * time stamp to the login_activity.txt. After this we make our FileWrite and PrintWriter objects.
     * Next we loop through our list of users and check to make sure the username and password matches with one of the users if it does,
     * we write the successful login attempt to the login_activity.txt file, go the appointments screen and display our custom alert
     * that checks whether there is an appointment within the next 15 minutes.
     * If it doesn't match any users, we write the unsuccessful login attempt to the login_activity.txt file then
     * we display an alert that the username and password is incorrect and display it with the correct language based on the user Locale.
     *
     * @throws IOException
     */
    public void handleLoginButton() throws IOException {
        //get username and password inputted by user
        String username = usernameField.getText();
        String password = passwordField.getText();

        //get our user list
        ObservableList<Users> userList = DBUsers.getAllUsers();

        //grab the time to display login attempt time
        LocalDateTime time = LocalDateTime.now();
        //make our print writer/file writer object to write to the login_activity.txt file
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        //loop through the list of users and check if any match
        for (Users user: userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                //write to the file
                printWriter.println("Successful login attempt was done at " + time + " username:" + username + " password:" + password);
                //close print writer
                printWriter.close();

                //proceed to the next screen user has valid credentials
                switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", loginButton);

                //loop through the list of appointments and see if any are within fifteen minutes of now
                boolean hasUpcomingAppointment = false;
                for (Appointments appointment : DBAppointments.getAllAppointments()) {
                    LocalDateTime appointmentStartTime = appointment.getStart();
                    if (appointmentStartTime.isAfter(LocalDateTime.now()) && appointmentStartTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
                        hasUpcomingAppointment = true;
                        //display an alert that the user has an upcoming appointment
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Upcoming Appointment");
                        alert.setHeaderText("You have an appointment within the next 15 minutes");
                        alert.setContentText("Appointment ID: " + appointment.getAppointmentID() + " Start time: " + appointment.getStart() + " End time: " + appointment.getEnd());
                        alert.showAndWait();
                        break;
                    }
                }
                if (!hasUpcomingAppointment) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No upcoming appointments");
                    alert.setHeaderText("You have no upcoming appointments scheduled within the next 15 minutes");
                    alert.showAndWait();
                }

                return;
            }

            //write to the file
            printWriter.println("Unsuccessful login attempt was done at " + time + " username:" + username + " password:" + password);
            //close print writer
            printWriter.close();
        }
        //display an error here no valid credentials found
        resourceBundle = ResourceBundle.getBundle("languageBundle/Nat", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("Login_Error").replace('_', ' '));
        alert.setHeaderText(resourceBundle.getString("Invalid_username_or_password").replace('_', ' '));
        alert.setContentText(resourceBundle.getString("Please_enter_a_valid_username_and_password.").replace('_', ' '));
        alert.showAndWait();
    }

    /**
     * This function handles exiting the program when the user hits the exit button
     * First, we display an alert which is translated to the correct language based on the users Locale for confirmation.
     * If the user hit's the OK button we exit the application if they hit cancel we dismiss the alert and stay
     */
    public void handleExitButton() {
        //confirm that the user wants to leave with an alert
        resourceBundle = ResourceBundle.getBundle("languageBundle/Nat", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("Confirm_Exit").replace('_', ' '));
        alert.setHeaderText(resourceBundle.getString("Are_you_sure_you_want_to_exit_the_program?").replace('_', ' '));
        alert.setContentText(resourceBundle.getString("Click_OK_to_exit,_or_Cancel_to_stay").replace('_', ' '));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * This function handles loading our resource bundle so that we can set user Locale and Timezone
     * It translates all the static labels based on where the user is and what their language is set to on their OS
     */
    public void loadResourceBundle() {
        resourceBundle = ResourceBundle.getBundle("languageBundle/Nat", Locale.getDefault());

        loginLabel.setText(resourceBundle.getString("Login"));
        usernameField.setPromptText(resourceBundle.getString("Username"));
        passwordField.setPromptText(resourceBundle.getString("Password"));
        loginButton.setText(resourceBundle.getString("Login"));
        exitButton.setText(resourceBundle.getString("Exit"));
        timeZoneLabel.setText(resourceBundle.getString("TimeZone").replace('_', ' '));
        timeZoneDisplayLabel.setText(String.valueOf(ZoneId.systemDefault()));
    }

    /**
     * Initialization of our screen, calls the load resource bundle function
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadResourceBundle();
    }
}

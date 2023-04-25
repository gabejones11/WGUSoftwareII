# WGUSoftwareII

#### Gabriel Jones, Version 1.0, 4/24/2023 ###
#### JDK: Java 17.0.5 
#### JavaFX version: 17.0.2 
#### MySQL Driver: mysql-connector-java-8.0.25.jar 

## Scheduling Application  
This is an application that uses Java to accomplish tasks such as scheduling, updating, and deleting appointments, creating, updating, and deleting customers, and viewing reports. All of this information is stored in a MySQL database that contains information on appointments, customers, country data, division data, etc. This was created as part of the Performance Assessment for Software II in the WGU curriculum.

## How to run the program
After the user hits the "Run" button in the IDE, they should be taken to the login screen. The user must enter the correct login credentials and will then be taken to the appointments screen. From here, the user can logout, navigate to the customers screen, navigate to the reports screen, add or modify an appointment, or use the radio buttons to filter the appointments table. The process is the same for the customers page, except for the radio buttons as they are not present on the customers page. On the reports page, to display results for the first table, the user must make a selection in the "Contacts" dropdown box. After making a selection, the user will see appointments that correspond to that contact populating the table.

## Description of additional report
For my additional report, I created a table that displays the number of customers by country. I thought this would be useful for users as they can now easily see which country has the most customers. This information can be used in various ways, such as staffing more French-speaking workers if most of the customer base is from French-speaking areas, allocating additional advertising budget to certain countries, and so on.

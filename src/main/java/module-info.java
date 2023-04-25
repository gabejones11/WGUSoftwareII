module dev.gabriel.schedulingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens dev.gabriel.schedulingapplication to javafx.fxml;
    exports dev.gabriel.schedulingapplication;
    opens dev.gabriel.schedulingapplication.Controllers to javafx.fxml;
    exports dev.gabriel.schedulingapplication.Controllers;
    opens dev.gabriel.schedulingapplication.Models to javafx.base;
    opens dev.gabriel.schedulingapplication.ReportModels to javafx.base;
}
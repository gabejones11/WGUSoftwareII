package dev.gabriel.schedulingapplication.ReportModels;

public class AppointmentsByTypeMonth {

    private String month;
    private String type;
    private int total;

    public AppointmentsByTypeMonth(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    //getters
    public String getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public int getTotal() {
        return total;
    }
}

package dev.gabriel.schedulingapplication.ReportModels;

public class CustomerCountry {

    private String country;
    private int customerCount;

    public CustomerCountry(String country, int customerCount) {
        this.country = country;
        this.customerCount = customerCount;
    }

    //getters
    public String getCountry() {
        return country;
    }

    public int getCustomerCount() {
        return customerCount;
    }
}

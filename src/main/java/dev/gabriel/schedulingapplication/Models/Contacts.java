package dev.gabriel.schedulingapplication.Models;

import dev.gabriel.schedulingapplication.dbhelper.DBContacts;

public class Contacts {
    private int contactID;
    private String contactName;
    private String email;

    public Contacts (int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    //getters
    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return email;
    }

    public String getContactNameByID() {
        return DBContacts.getContactName(contactID);
    }

}

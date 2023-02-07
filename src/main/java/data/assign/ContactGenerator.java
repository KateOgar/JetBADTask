package data.assign;

import api.model.assign.Contact;

import static data.Configuration.getConfig;

public class ContactGenerator {

    public static final String USER_WITH_ASSIGNED_WSLICENSE_EMAIL = getConfig("user.with.assigned.license.email");
    public static final String USER_WITH_ASSIGNED_WSLICENSE_FIRSTNAME = getConfig("user.with.assigned.license.firstname");
    public static final String USER_WITH_ASSIGNED_WSLICENSE_LASTNAME = getConfig("user.with.assigned.license.lastname");
    public static final String USER_INVALID_EMAIL = getConfig("user.invalid.email");
    public static final String USER_TO_ASSIGN_FIRSTNAME = getConfig("user.to.assign.firstname");
    public static final String USER_TO_ASSIGN_LASTNAME = getConfig("user.to.assign.lastname");
    public static final String USER_TO_ASSIGN_EMAIL = getConfig("user.to.assign.email");


    public static Contact defaultContact() {
        Contact contact = new Contact.ContactBuilder()
                .withFirstName(USER_TO_ASSIGN_FIRSTNAME)
                .withLastName(USER_TO_ASSIGN_LASTNAME)
                .withEmail(USER_TO_ASSIGN_EMAIL)
                .build();
        return contact;
    }

    public static Contact userWithAssignedWSLicense() {
        Contact contact = new Contact.ContactBuilder()
                .withFirstName(USER_WITH_ASSIGNED_WSLICENSE_FIRSTNAME)
                .withLastName(USER_WITH_ASSIGNED_WSLICENSE_LASTNAME)
                .withEmail(USER_WITH_ASSIGNED_WSLICENSE_EMAIL)
                .build();
        return contact;
    }

    public static Contact contactWithFirstName(String firstName) {
        Contact contact = new Contact.ContactBuilder()
                .withFirstName(firstName)
                .withLastName(USER_TO_ASSIGN_LASTNAME)
                .withEmail(USER_TO_ASSIGN_EMAIL)
                .build();
        return contact;
    }

    public static Contact contactWithLastName(String lastName) {
        Contact contact = new Contact.ContactBuilder()
                .withFirstName(USER_TO_ASSIGN_FIRSTNAME)
                .withLastName(lastName)
                .withEmail(USER_TO_ASSIGN_EMAIL)
                .build();
        return contact;
    }

    public static Contact contactWithEmail(String email) {
        Contact contact = new Contact.ContactBuilder()
                .withFirstName(USER_TO_ASSIGN_FIRSTNAME)
                .withLastName(USER_TO_ASSIGN_LASTNAME)
                .withEmail(email)
                .build();
        return contact;
    }
}

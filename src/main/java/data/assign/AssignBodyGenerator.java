package data.assign;

import api.model.assign.Contact;
import api.model.assign.License;
import api.model.assign.LicenseAssign;

import static data.assign.ContactGenerator.*;
import static data.assign.LicenseGenerator.*;

public class AssignBodyGenerator {

    public static LicenseAssign.LicenseAssignObjBuilder defaultLicense(String licenseId) {
        return new LicenseAssign.LicenseAssignObjBuilder()
                .withLicenseId(licenseId)
                .withSendEmail(false)
                .withIncludeOfflineActivationCode(false);
    }

    private static LicenseAssign.LicenseAssignObjBuilder defaultLicense() {
        return defaultLicense(NOT_ASSIGNED_DEFAULT_WSLICENSE);
    }

    public static LicenseAssign withAllValidBody(String licenseId) {
        return defaultLicense(licenseId)
                .withLicense(LicenseGenerator.defaultLicense())
                .withContact(defaultContact())
                .build();
    }

    public static LicenseAssign defaultWithLicense(License license) {
        return defaultLicense()
                .withLicense(license)
                .withContact(defaultContact())
                .build();
    }

    public static LicenseAssign defaultWithContact(Contact contact) {
        return defaultLicense()
                .withLicense(LicenseGenerator.defaultLicense())
                .withContact(contact)
                .build();
    }

    public static LicenseAssign defaultWithSendMailAndCode(String licenseId, Boolean sendEmail, Boolean includeCode) {
        return new LicenseAssign.LicenseAssignObjBuilder()
                .withLicenseId(licenseId)
                .withLicense(LicenseGenerator.defaultLicense())
                .withContact(defaultContact())
                .withSendEmail(sendEmail)
                .withIncludeOfflineActivationCode(includeCode)
                .build();
    }

    public static LicenseAssign withoutLicenseIDWithLicence(License license) {
        return new LicenseAssign.LicenseAssignObjBuilder()
                .withLicense(license)
                .withContact(defaultContact())
                .withSendEmail(false)
                .withIncludeOfflineActivationCode(false)
                .build();
    }

    public static LicenseAssign withoutLicenseAndLicenseIDBody() {
        return new LicenseAssign.LicenseAssignObjBuilder()
                .withSendEmail(false)
                .withIncludeOfflineActivationCode(false)
                .withContact(defaultContact())
                .build();
    }

    public static LicenseAssign withoutContactBody() {
        return defaultLicense()
                .withLicense(LicenseGenerator.defaultLicense())
                .build();
    }
}

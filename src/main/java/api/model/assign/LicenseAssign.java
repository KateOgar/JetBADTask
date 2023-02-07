package api.model.assign;

public class LicenseAssign {
    private String licenseId;
    private License license;
    private Contact contact;
    private Boolean sendEmail;
    private Boolean includeOfflineActivationCode;

    public String getLicenseId() {
        return licenseId;
    }

    public License getLicense() {
        return license;
    }

    public Contact getContact() {
        return contact;
    }

    public Boolean isSendEmail() {
        return sendEmail;
    }

    public Boolean isIncludeOfflineActivationCode() {
        return includeOfflineActivationCode;
    }

    private LicenseAssign(LicenseAssignObjBuilder builder) {
        this.licenseId = builder.licenseId;
        this.license = builder.license;
        this.contact = builder.contact;
        this.sendEmail = builder.sendEmail;
        this.includeOfflineActivationCode = builder.includeOfflineActivationCode;
    }

    public static class LicenseAssignObjBuilder {
        private String licenseId;
        private License license;
        private Contact contact;
        private Boolean sendEmail;
        private Boolean includeOfflineActivationCode;

        public LicenseAssignObjBuilder withLicenseId(String licenseId) {
            this.licenseId = licenseId;
            return this;
        }

        public LicenseAssignObjBuilder withLicense(License license) {
            this.license = license;
            return this;
        }

        public LicenseAssignObjBuilder withContact(Contact contact) {
            this.contact = contact;
            return this;
        }


        public LicenseAssignObjBuilder withSendEmail(Boolean sendEmail) {
            this.sendEmail = sendEmail;
            return this;
        }

        public LicenseAssignObjBuilder withIncludeOfflineActivationCode(Boolean includeOfflineActivationCode) {
            this.includeOfflineActivationCode = includeOfflineActivationCode;
            return this;
        }

        public LicenseAssign build() {
            return new LicenseAssign(this);
        }

    }




}

package api.model.assign;

public class Contact {
    private String firstName;
    private String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }


    private Contact(ContactBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
    }

    public static class ContactBuilder {
        private String firstName;
        private String lastName;
        private String email;


        public ContactBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ContactBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ContactBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ContactBuilder withNOFirstName() {
            this.firstName = null;
            return this;
        }

        public ContactBuilder withNOLastName() {
            this.lastName = null;
            return this;
        }

        public ContactBuilder withNOEmail() {
            this.email = null;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}

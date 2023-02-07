package data;

public enum Roles {
    COMPANY_ADMIN("company_admin"),
    TEAM_ADMIN("team_admin"),
    TEAM_VIEWER("team_viewer");

    private final String propertyName;

    Roles(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}

package tests.assign;

import api.model.common.Error;
import data.assign.LicenseGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static data.Roles.TEAM_ADMIN;
import static data.assign.AssignBodyGenerator.*;
import static data.assign.AssignErrorGenerator.assignError;
import static data.assign.ContactGenerator.*;
import static data.assign.LicenseGenerator.*;
import static data.assign.LicenseGenerator.LICENSE_FROM_ADMIN_TEAM;
import static data.move.LicensesToMoveGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static data.Roles.COMPANY_ADMIN;
import static util.RequestUtils.*;

public class LicenseAssignPositiveTest {

    @Test
    @Feature("Positive: /customer/licenses/assign")
    @Description("Company admin role. Assign license")
    public void testAssignLicenseFromCompanyAdmin() {
        givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithEmail(USER_TO_ASSIGN_EMAIL)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(200);

        String assignedUser = getUserForLicense(NOT_ASSIGNED_DEFAULT_WSLICENSE);
        assertThat(USER_TO_ASSIGN_EMAIL, equalTo(assignedUser));
    }

    @Test
    @Feature("Positive: /customer/licenses/assign")
    @Description("Admin team role. Assign licence from team")
    public void testAssignLicenseFromTeamAdmin() {
        givenUser(TEAM_ADMIN)
                .body(defaultLicense(LICENSE_FROM_ADMIN_TEAM)
                        .withLicense(licenseWithTeam(TEAM_WITH_ADMIN_ROLE))
                        .withContact(contactWithEmail(USER_TO_ASSIGN_EMAIL))
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(200);

        String assignedUser = getUserForLicense(LICENSE_FROM_ADMIN_TEAM);
        assertThat(USER_TO_ASSIGN_EMAIL, equalTo(assignedUser));
    }

    @Test
    @Feature("Positive: /customer/licenses/assign")
    @Description("One user can use several same product licenses")
    public void testAssignToUserWithSameProductLicense() {
        givenUser(COMPANY_ADMIN)
                .body(defaultLicense(NOT_ASSIGNED_WSLICENSE)
                        .withLicense(LicenseGenerator.defaultLicense())
                        .withContact(userWithAssignedWSLicense())
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(200);

        String assignedUser = getUserForLicense(NOT_ASSIGNED_WSLICENSE);
        assertThat(USER_WITH_ASSIGNED_WSLICENSE_EMAIL, equalTo(assignedUser));
    }

    @Test
    @Feature("Positive: /customer/licenses/assign")
    @Description("Assign any available license without licenseId")
    public void testAssignLicenseWithoutLicenseId() {
        givenUser(COMPANY_ADMIN)
                .body(withoutLicenseIDWithLicence(licenseWithProductCode(NOT_DEFAULT_PRODUCTCODE)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(200);

        String assignedUser = getUserForLicense(NOT_DEFAULT_PC_LOCENSE);
        assertThat(USER_TO_ASSIGN_EMAIL, equalTo(assignedUser));
    }

    @Test
    @Ignore
    @Issue("9")
    @Feature("Positive: /customer/licenses/assign")
    @Description("Assign with different licenseID ProductCode")
    public void testDifferentLicenseIdAndProductCode() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithLicense(licenseWithProductCode(NOT_DEFAULT_PRODUCTCODE)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError("unknown error code", "unknown error message")));
    }
}

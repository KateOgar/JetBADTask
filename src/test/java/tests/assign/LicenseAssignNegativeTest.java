package tests.assign;

import api.model.common.Error;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Test;

import static data.Roles.*;
import static data.User.getXApiKey;
import static data.User.getxCustomerCode;
import static data.assign.AssignBodyGenerator.*;
import static data.assign.AssignBodyGenerator.withAllValidBody;
import static data.assign.ContactGenerator.*;
import static data.assign.AssignErrorGenerator.*;
import static data.assign.LicenseGenerator.*;
import static data.move.LicensesToMoveGenerator.LICENSE_FROM_VIEWER_TEAM_TOMOVE;
import static data.move.LicensesToMoveGenerator.TEAM_WITH_VIEWER_ROLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static util.RequestUtils.*;

public class LicenseAssignNegativeTest {

    /**
     * Check requests without required headers and auth errors
     */

    @Test
    @Issue("1")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign without X-Customer-Code")
    public void testWithoutXCustomerCodeHeader() {
        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Api-Key", getXApiKey(COMPANY_ADMIN))
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    @Test
    @Issue("1")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign without X-Api-Key")
    public void testWithoutXApiKey() {
        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", getxCustomerCode())
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_TOKEN_HEADER, API_HEADER_REQUIRED)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Move with not existed X-Customer-Code header")
    public void testWithNotExistedXCustomerCodeHeader() {
        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", "1234567890")
                .header("X-Api-Key", getXApiKey(COMPANY_ADMIN))
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_TOKEN, INVALID_TOKEN_DESCR)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Move with not existed Customer Code header")
    public void testWithNotExistedXApiTokenHeader() {
        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", getxCustomerCode())
                .header("X-Api-Key", "ABC1234567890")
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_TOKEN, INVALID_TOKEN_DESCR)));
    }


    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Team admin role. Assign license from other team")
    public void testAssignFromTeamAdminLicenseFromOtherTeam() {
        Error error = givenUser(TEAM_ADMIN)
                .body(defaultLicense(LICENSE_FROM_VIEWER_TEAM_TOMOVE)
                        .withLicense(licenseWithTeam(TEAM_WITH_VIEWER_ROLE))
                        .withContact(contactWithEmail(USER_TO_ASSIGN_EMAIL))
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(403)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(TEAM_MISMATCH, String.format(MISMATCH_DESCRIPTION, TEAM_FOR_ADMIN_ROLE_ID))));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Viewer role. Can't assign license from team")
    public void testAssignFromTeamViewerTeamLicense() {
        Error error = givenUser(TEAM_VIEWER)
                .body(defaultLicense(LICENSE_FROM_VIEWER_TEAM)
                        .withLicense(licenseWithTeam(TEAM_WITH_VIEWER_ROLE))
                        .withContact(contactWithEmail(USER_TO_ASSIGN_EMAIL))
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(403)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INSUFFICIENT_PERMISSIONS,
                String.format(MISSING_EDIT_PERMISSION, getxCustomerCode(), TEAM_FOR_VIEWER_ROLE_ID))));
    }

    /**
     * Check requests with NULL fields
     */

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without body")
    public void testAssignWithNullBody() {
        Error error = givenUser(COMPANY_ADMIN)
                .contentType("application/json")
                .accept("application/json")
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without LicenseID and License")
    public void testWithoutLicenseIDAndLicense() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withoutLicenseAndLicenseIDBody())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSED_LICENSES)));
    }

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without Contact")
    public void testWithNullContact() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withoutContactBody())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSED_FIELD)));
    }

    @Test
    @Issue("7")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without LicenseTeam")
    public void testWithNullLicenseTeam() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultLicense(SHOULDNOT_BE_ASSIGNED_LICENSE1)
                        .withLicense(licenseWithTeam(null))
                        .withContact(defaultContact())
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without ProductCode")
    public void testWithNullProductCode() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithLicense(licenseWithProductCode(null)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without FirstName")
    public void testWithNullFirstName() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithFirstName(null)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without LastName")
    public void testWithNullLastName() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithLastName(null)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Issue("2")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without Email")
    public void testWithNullEmail() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithEmail(null)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Issue("10")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without SendEmail")
    public void testWithNullSendEmail() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultLicense(SHOULDNOT_BE_ASSIGNED_LICENSE2)
                        .withLicense(licenseWithTeam(NOT_EXISTED_TEAM_ID))
                        .withContact(defaultContact())
                        .withSendEmail(null)
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    @Test
    @Issue("10")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign request without IncludeOfflineActivationCode")
    public void testWithNullIncludeActivationCode() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithSendMailAndCode(SHOULDNOT_BE_ASSIGNED_LICENSE3, false, null))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    /**
     * Check requests with wrong license data
     */
    @Test
    @Issue("3")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign valid not existed license")
    public void testAssignNotFromPoolLicenseID() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(NOT_EXISTED_LICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(LICENSE_NOT_FOUND, NOT_EXISTED_LICENSE)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign already assigned license")
    public void testLicenseIDAlreadyAssined() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(ALREADY_ASSIGNED_WSLICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(LICENSE_ASSIGNED, ALLOCATED)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign invalid format license ")
    public void testNotValidLicenseIDFormat() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(NOT_VALID_FORMAT_LICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(LICENSE_NOT_FOUND, NOT_VALID_FORMAT_LICENSE)));
    }

    @Test
    @Issue("7")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with not existed License TeamId from company admin ")
    public void testAssignNotExistedLicenseTeam() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultLicense(SHOULDNOT_BE_ASSIGNED_LICENSE4)
                        .withLicense(licenseWithTeam(NOT_EXISTED_TEAM_ID))
                        .withContact(defaultContact())
                        .build())
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("No LicenseId and no licenses from specified ProductCode")
    public void testNoLicenseIdProductCodeNotInPool() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withoutLicenseIDWithLicence(licenseWithProductCode(NOT_DEFAULT_PRODUCTCODE)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(NO_AVAILABLE_LICENSE_TO_ASSIGN, String.format(NO_AVAILABLE_FOR_CODE, TEAM_FOR_COMPADMIN_ROLE_ID, NOT_DEFAULT_PRODUCTCODE))));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with not existed ProductCode")
    public void testWithNotExistedProductCode() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withoutLicenseIDWithLicence(licenseWithProductCode(NOT_EXISTED_PRODUCTCODE)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(PRODUCT_NOT_FOUND, NOT_EXISTED_PRODUCTCODE)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign outdated license")
    public void testAssignOutdatedLicense() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(OUTDATED_LICENSE))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN, EXPIRED_WITHOUT_FALLBACK)));
    }

    /**
     * Check requests with wrong contact data
     */

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with empty Firstname")
    public void testEmptyFirstName() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithFirstName("")))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_CONTACT_NAME, FIELD_CANT_BE_EMPTY)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with empty LastName")
    public void testEmptyLastName() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithLastName("")))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_CONTACT_NAME, FIELD_CANT_BE_EMPTY)));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with empty Email")
    public void testEmptyEmail() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithEmail("")))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_CONTACT_EMAIL, "")));
    }

    @Test
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with invalid email format")
    public void testInvalidEmailFormat() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithContact(contactWithEmail(USER_INVALID_EMAIL)))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(INVALID_CONTACT_EMAIL, USER_INVALID_EMAIL)));
    }

    /**
     * Check requests with wrong includeOfflineActivationCode data
     */

    @Test
    @Issue("11")
    @Feature("Negative: /customer/licenses/assign")
    @Description("Assign with sendEmail false and activationCode true")
    public void testSendEmailFalseOfflineActivationCodeTrue() {
        Error error = givenUser(COMPANY_ADMIN)
                .body(defaultWithSendMailAndCode(SHOULDNOT_BE_ASSIGNED_LICENSE5, false, true))
                .when()
                .post(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(assignError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    /**
     * Check request with not supported methods
     */
    @Test
    @Issue("4")
    @Feature("Negative: /customer/licenses/assign")
    @Description("DELETE instead of POST for assign")
    public void deleteInsteadOfPost() {
        givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .delete(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Feature("Negative: /customer/licenses/assign")
    @Description("PUT instead of POST for assign")
    public void putInsteadOfPost() {
        givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .put(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Feature("Negative: /customer/licenses/assign")
    @Description("GET instead of POST for assign")
    public void getInsteadOfPost() {
        givenUser(COMPANY_ADMIN)
                .body(withAllValidBody(NOT_ASSIGNED_DEFAULT_WSLICENSE))
                .when()
                .get(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Issue("8")
    @Feature("Negative: /customer/licenses/assign")
    @Description("OPTIONS instead of POST for assign")
    public void optionsInsteadOfPost() {
        givenUser(COMPANY_ADMIN)
                .body(defaultLicense(SHOULDNOT_BE_ASSIGNED_LICENSE6)
                        .withLicense(licenseWithTeam(NOT_EXISTED_TEAM_ID))
                        .withContact(defaultContact())
                        .build())
                .when()
                .options(CUSTOMER_LICENSES_ASSIGN_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }
}

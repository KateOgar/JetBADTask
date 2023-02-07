package tests.move;

import api.model.common.Error;
import data.User;
import extension.ChangeLicenseTeamExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static data.Roles.*;
import static data.User.getXApiKey;
import static data.assign.AssignErrorGenerator.*;
import static data.assign.LicenseGenerator.*;
import static data.assign.LicenseGenerator.LICENSE_FROM_ADMIN_TEAM;
import static data.move.ChangeTeamBodyGenerator.changeLicensesTeam;
import static data.move.ChangeTeamErrorGenerator.*;
import static data.move.LicensesToMoveGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static util.RequestUtils.*;

@ExtendWith(ChangeLicenseTeamExtension.class)
public class ChangeLicTeamNegativeTest {

    /**
     * Check requests without required headers
     */

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move without X-Customer-Code header")
    public void testWithoutXCustomerCodeHeader() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Api-Key", getXApiKey(COMPANY_ADMIN))
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(MISSING_CUSTOMER_HEADER, CUSTOMER_HEADER_REQUIRED)));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move without X-Api-Key header")
    public void testWithoutXApiKey() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", User.getxCustomerCode())
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(401)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(MISSING_TOKEN_HEADER, API_HEADER_REQUIRED)));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move with not existed X-Customer-Code header")
    public void moveLicenseNotExistedCustomerCode() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", "123445678")
                .header("X-Api-Key", getXApiKey(COMPANY_ADMIN))
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(401)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(INVALID_TOKEN, INVALID_TOKEN_DESCR)));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move with not existed X-Api-Key header")
    public void moveLicenseNotExistedApiToken() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", User.getxCustomerCode())
                .header("X-Api-Key", "ABC1234567890")
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(401)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(INVALID_TOKEN, INVALID_TOKEN_DESCR)));
    }


    /**
     * Check requests without required body
     */

    @Test
    @Issue("2")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move without body")
    public void testWithoutBody() {
        Error error = restAssuredGiven().log().all()
                .contentType("application/json")
                .header("X-Customer-Code", User.getxCustomerCode())
                .header("X-Api-Key", getXApiKey(COMPANY_ADMIN))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Team admin role. Move license from team")
    public void moveLicenseFromAdminRoleTeam() {
        List<String> licenses = licenseToMove(LICENSE_FROM_ADMIN_TEAM_TOMOVE);

        Error error = postChangeLicensesTeam(TEAM_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(403)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(TOKEN_TYPE_MISMATCH, CHANGE_NOT_POSSIBLE)));

    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Team Viewer role. Move license from team")
    public void moveLicenseFromViewerRoleTeam() {
        List<String> licenses = licenseToMove(LICENSE_FROM_VIEWER_TEAM_TOMOVE);

        Error error = postChangeLicensesTeam(TEAM_VIEWER, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(403)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(TOKEN_TYPE_MISMATCH, CHANGE_NOT_POSSIBLE)));
    }

    /**
     * Check requests with incorrect teamId data
     */

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move license to not existed team")
    public void moveLicenseToNotExistedTeamID() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = postChangeLicensesTeam(COMPANY_ADMIN, NOT_EXISTED_TEAM_ID, licenses)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(TEAM_NOT_FOUND, String.valueOf(NOT_EXISTED_TEAM_ID))));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move license to null team")
    public void moveLicenseInNullTeam() {
        List<String> licenses = licensesFromOneTeamToMove();

        Error error = postChangeLicensesTeam(COMPANY_ADMIN, null, licenses)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(TEAM_NOT_FOUND, "0")));
        // tbd is "null" better than 0 in description?
    }

    /**
     * Check requests with incorrect license data
     */

    @Test
    @Issue("5")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move not existed license")
    public void moveNotExistedLicense() {
        List<String> licenses = licenseToMove(NOT_EXISTED_LICENSE);

        Error error = postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(404)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(LICENSE_NOT_FOUND, NOT_EXISTED_LICENSE)));
    }

    @Test
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move already existed in team license")
    public void moveAlreadyExistedInTeamLicense() {
        List<String> licenses = licenseToMove(LICENSE1_FROM_TEAM1_TOMOVE);

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM1_WITH_LICENSES_TOMOVE, licenses)
                .then()
                .statusCode(200);
    }

    @Test
    @Issue("2")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move NULL license array")
    public void moveNullLicenseArray() {
        Error error = postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, null)
                .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError(MISSING_FIELD, MISSING_FIELD)));
    }

    @Test
    @Issue("6")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("Move NULL license")
    public void moveNullLicense() {
        List<String> licenses = licenseToMove(null);

        Error error = postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(400)
                .extract()
                .as(Error.class);
        assertThat(error, equalTo(changeTeamError("some error code", "some error message")));
        // tbd and figure out error messages
    }

    /**
     * Check request with not supported methods
     */

    @Test
    @Issue("4")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("DELETE instead of POST for move")
    public void deleteInsteadOfPost() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE, LICENSE2_FROM_TEAM1_TOMOVE);

        givenUser(COMPANY_ADMIN)
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .delete(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("PUT instead of POST for move")
    public void putInsteadOfPost() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE, LICENSE2_FROM_TEAM1_TOMOVE);

        givenUser(COMPANY_ADMIN)
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .put(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("GET instead of POST for move")
    public void getInsteadOfPost() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE, LICENSE2_FROM_TEAM1_TOMOVE);

        givenUser(COMPANY_ADMIN)
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .get(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }

    @Test
    @Issue("4")
    @Feature("Negative /customer/changeLicensesTeam")
    @Description("OPTIONS instead of POST for move")
    public void optionsInsteadOfPost() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE, LICENSE2_FROM_TEAM1_TOMOVE);

        givenUser(COMPANY_ADMIN)
                .body(changeLicensesTeam(TEAM_TOMOVE_LICENSES, licenses))
                .when()
                .options(CHANGE_LICENSES_TEAM_URI)
                .then()
                .statusCode(405)
                .contentType("application/json")
                .extract()
                .as(Error.class);
    }
}

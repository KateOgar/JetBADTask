package tests.move;

import extension.ChangeLicenseTeamExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static data.Roles.*;
import static data.assign.LicenseGenerator.OUTDATED_LICENSE;
import static data.move.LicensesToMoveGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static util.RequestUtils.*;


@ExtendWith(ChangeLicenseTeamExtension.class)
public class ChangeLicTeamPositiveTest {

    @Test
    @Feature("Positive /customer/changeLicensesTeam")
    @Description("Company admin role. Change team for 2 different licenses")
    public void changeDiffLicensesTeamWithCompanyAdminOK() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE,
                LICENSE2_FROM_TEAM1_TOMOVE);

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(200);

        List<String> licenseIds = getLicensesFromTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES);
        licenses.forEach(id -> assertThat(licenseIds.contains(id), is(true)));
    }

    @Test
    @Feature("Positive /customer/changeLicensesTeam")
    @Description("Licenses from different teams")
    public void licensesFromDifferentTeamsToMove() {
        List<String> licenses = licensesListToMove(LICENSE1_FROM_TEAM1_TOMOVE,
                LICENSE1_FROM_TEAM2_TOMOVE);

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(200);

        List<String> licenseIds = getLicensesFromTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES);
        licenses.forEach(id -> assertThat(licenseIds.contains(id), is(true)));
    }

    @Test
    @Feature("Positive /customer/changeLicensesTeam")
    @Description("Move outdated license")
    public void moveOutdatedLicense() {
        List<String> licenses = licenseToMove(OUTDATED_LICENSE);

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(200);

        List<String> licenseIds = getLicensesFromTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES);
        licenses.forEach(id -> assertThat(licenseIds.contains(id), is(true)));

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM1_WITH_LICENSES_TOMOVE, Arrays.asList(OUTDATED_LICENSE))
                .then()
                .statusCode(200);
    }

    @Test
    @Feature("Positive /customer/changeLicensesTeam")
    @Description("Move already assigned license")
    public void moveAssignedLicense() {
        List<String> licenses = licenseToMove(LICENSE_ASSIGNED_FROM_TEAM1);

        postChangeLicensesTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES, licenses)
                .then()
                .statusCode(200);

        List<String> licenseIds = getLicensesFromTeam(COMPANY_ADMIN, TEAM_TOMOVE_LICENSES);
        licenses.forEach(id -> assertThat(licenseIds.contains(id), is(true)));
    }
}

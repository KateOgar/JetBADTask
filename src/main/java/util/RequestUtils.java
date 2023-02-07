package util;

import com.github.viclovsky.swagger.coverage.CoverageOutputWriter;
import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import data.Roles;
import data.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;
import java.util.List;

import static data.Configuration.getConfig;
import static data.Roles.COMPANY_ADMIN;
import static data.move.ChangeTeamBodyGenerator.changeLicensesTeam;
import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.http.ContentType.JSON;

public class RequestUtils {
    public static final String CUSTOMER_LICENSES_ASSIGN_URI = getConfig("licences.assign.uri");
    public static final String CHANGE_LICENSES_TEAM_URI = getConfig("change.licence.team.uri");


    private static final CoverageOutputWriter coverageWriter = new FileSystemOutputWriter(Paths.get("target/swagger-coverage-output"));

    public static RequestSpecification givenUser(Roles userRole) {
        return User.withRole(userRole).log().all()
                .contentType("application/json")
                .accept("application/json");
    }

    public static RequestSpecification restAssuredGiven() {
        return RestAssured.given().baseUri("https://account.jetbrains.com")
                .basePath("/api/v1").filters(
                        new AllureRestAssured(),
                        new SwaggerCoverageRestAssured(coverageWriter))
                .config(config().logConfig(logConfig()
                        .blacklistHeader("X-Customer-Code")
                        .blacklistHeader("X-Api-Key")));
    }

    public static Response postChangeLicensesTeam(Roles role, Integer teamToMove, List<String> licenses) {
        return givenUser(role)
                .body(changeLicensesTeam(teamToMove, licenses))
                .when()
                .post(CHANGE_LICENSES_TEAM_URI);
    }

    public static String getUserForLicense(String lisenseId) {
        return givenUser(COMPANY_ADMIN)
                .when()
                .get(String.format("/customer/licenses/%s", lisenseId))
                .then()
                .contentType(JSON)
                .extract()
                .path("assignee.email");
    }
}

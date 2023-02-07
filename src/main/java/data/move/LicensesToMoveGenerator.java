package data.move;

import data.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static data.Configuration.getConfig;
import static data.Configuration.getConfigNumber;
import static io.restassured.http.ContentType.JSON;
import static util.RequestUtils.givenUser;

public class LicensesToMoveGenerator {

    public static final String LICENSE1_FROM_TEAM1_TOMOVE = getConfig("licence1.from.team1");
    public static final String LICENSE2_FROM_TEAM1_TOMOVE = getConfig("licence2.from.team1");
    public static final String LICENSE3_FROM_TEAM1_TOMOVE = getConfig("license3.from.team1");
    public static final String LICENSE_OUTDATED_FROM_TEAM1 = getConfig("license.outdated.from.team1");
    public static final String LICENSE_ASSIGNED_FROM_TEAM1 = getConfig("license.assigned.from.team1");
    public static final String LICENSE1_FROM_TEAM2_TOMOVE = getConfig("licence1.from.team2");
    public static final String LICENSE2_FROM_TEAM2_TOMOVE = getConfig("licence2.from.team2");
    public static final String LICENSE_FROM_VIEWER_TEAM_TOMOVE = getConfig("license.from.viewer.team.tomove");
    public static final String LICENSE_FROM_ADMIN_TEAM_TOMOVE = getConfig("license.from.admin.team.tomove");


    public static final Integer TEAM1_WITH_LICENSES_TOMOVE = getConfigNumber("team1.with.licenses");
    public static final Integer TEAM2_WITH_LICENSES_TOMOVE = getConfigNumber("team2.with.licenses");
    public static final Integer TEAM_TOMOVE_LICENSES = getConfigNumber("team.tomove.licenses");
    public static final Integer TEAM_WITH_ADMIN_ROLE = getConfigNumber("team.for.admin.role");
    public static final Integer TEAM_WITH_VIEWER_ROLE = getConfigNumber("team.for.viewer.role");



    public static List<String> licensesFromOneTeamToMove(){
        List<String> licensesIds = new ArrayList<>();
        licensesIds.add(LICENSE1_FROM_TEAM1_TOMOVE);
        licensesIds.add(LICENSE2_FROM_TEAM1_TOMOVE);
        return licensesIds;
    }

    public static List<String> licensesListToMove(String ...licenses){
        return new ArrayList<>(Arrays.asList(licenses));
    }

    public static List<String> licenseToMove(String license){
        List<String> licensesIds = new ArrayList<>();
        licensesIds.add(license);
        return licensesIds;
    }

    public static List<String> getLicensesFromTeam(Roles role, Integer teamId) {
        return givenUser(role)
                .when()
                .get(String.format("/customer/teams/%d/licenses", teamId))
                .then()
                .contentType(JSON)
                .extract().jsonPath()
                .getList("licenseId", String.class);
    }
}

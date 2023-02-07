package data.move;

import api.model.move.ChangeLicensesTeam;

import java.util.List;

public class ChangeTeamBodyGenerator {
    public static ChangeLicensesTeam changeLicensesTeam(Integer targetTeamId, List<String> licenseIds) {
        return new ChangeLicensesTeam.ChangeLicensesTeamBuilder()
                .withTargetTeamId(targetTeamId)
                .withLicenseIds(licenseIds)
                .build();
    }
}

package extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Collections;

import static data.Roles.COMPANY_ADMIN;
import static data.assign.LicenseGenerator.OUTDATED_LICENSE;
import static data.move.LicensesToMoveGenerator.*;
import static util.RequestUtils.postChangeLicensesTeam;

public class ChangeLicenseTeamExtension implements AfterEachCallback {

    public ChangeLicenseTeamExtension() {
    }


    @Override
    public void afterEach(ExtensionContext context) {
        try{
            postChangeLicensesTeam(COMPANY_ADMIN, TEAM1_WITH_LICENSES_TOMOVE,
                    Arrays.asList(LICENSE1_FROM_TEAM1_TOMOVE,
                            LICENSE2_FROM_TEAM1_TOMOVE,
                            OUTDATED_LICENSE,
                            LICENSE_ASSIGNED_FROM_TEAM1));
            postChangeLicensesTeam(COMPANY_ADMIN, TEAM2_WITH_LICENSES_TOMOVE,
                    Collections.singletonList(LICENSE1_FROM_TEAM2_TOMOVE));
        } catch (Exception e) {
            System.out.printf("Can't make after each setup fully %s%n", e.getMessage());
        }
    }
}
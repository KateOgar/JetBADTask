package data.assign;

import api.model.assign.License;

import static data.Configuration.getConfig;
import static data.Configuration.getConfigNumber;

public class LicenseGenerator {

    public static final String NOT_ASSIGNED_DEFAULT_WSLICENSE = getConfig("license.not.assigned");
    public static final String NOT_ASSIGNED_WSLICENSE = getConfig("license1.from.company.team");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE1 = getConfig("license1.shouldnot.assigned1");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE2 = getConfig("license1.shouldnot.assigned2");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE3 = getConfig("license1.shouldnot.assigned3");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE4 = getConfig("license1.shouldnot.assigned4");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE5 = getConfig("license1.shouldnot.assigned5");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE6 = getConfig("license1.shouldnot.assigned6");
    public static final String SHOULDNOT_BE_ASSIGNED_LICENSE7 = getConfig("license1.shouldnot.assigned7");

    public static final String LICENSE_FROM_ADMIN_TEAM = getConfig("license.from.admin.team");
    public static final String LICENSE_FROM_VIEWER_TEAM = getConfig("license.from.viewer.team");
    public static final String NOT_EXISTED_LICENSE = getConfig("licences.notexisted.valid");
    public static final String ALREADY_ASSIGNED_WSLICENSE = getConfig("license.already.assigned");
    public static final String OUTDATED_LICENSE = getConfig("license.outdated");
    public static final String NOT_VALID_FORMAT_LICENSE = getConfig("license.not.valid.format");
    public static final String NOT_DEFAULT_PC_LOCENSE = getConfig("license.notdefault.productcode");

    public static final Integer TEAM_FOR_COMPADMIN_ROLE_ID = getConfigNumber("team.for.compadmin.role");
    public static final Integer TEAM_FOR_ADMIN_ROLE_ID = getConfigNumber("team.for.admin.role");
    public static final Integer TEAM_FOR_VIEWER_ROLE_ID = getConfigNumber("team.for.viewer.role");
    public static final Integer NOT_EXISTED_TEAM_ID = getConfigNumber("valid.not.existed.team");

    public static final String VALID_EXISTED_PRODUCTCODE = getConfig("valid.existed.pdoductcode");
    public static final String NOT_EXISTED_PRODUCTCODE = getConfig("not.existed.productcode");
    public static final String NOT_DEFAULT_PRODUCTCODE = getConfig("not.default.productcode");


    public static License defaultLicense() {
        License license = new License.LicenseBuilder()
                .withTeam(TEAM_FOR_COMPADMIN_ROLE_ID)
                .withProductCode(VALID_EXISTED_PRODUCTCODE)
                .build();
        return license;
    }

    public static License licenseWithTeam(Integer teamId) {
        License license = new License.LicenseBuilder()
                .withTeam(teamId)
                .withProductCode(VALID_EXISTED_PRODUCTCODE)
                .build();
        return license;
    }

    public static License licenseWithProductCode(String prod_code) {
        License license = new License.LicenseBuilder()
                .withTeam(TEAM_FOR_COMPADMIN_ROLE_ID)
                .withProductCode(prod_code)
                .build();
        return license;
    }
}

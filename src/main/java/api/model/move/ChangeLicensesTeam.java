package api.model.move;

import java.util.List;

public class ChangeLicensesTeam {
    private Integer targetTeamId;
    private List<String> licenseIds;

    public Integer getTargetTeamId() {
        return targetTeamId;
    }

    public List<String> getLicenseIds() {
        return licenseIds;
    }


    private ChangeLicensesTeam(ChangeLicensesTeamBuilder builder) {
        this.targetTeamId = builder.targetTeamId;
        this.licenseIds = builder.licenseIds;
    }

    public static class ChangeLicensesTeamBuilder {
        private Integer targetTeamId;
        private List<String> licenseIds;

        public ChangeLicensesTeamBuilder withTargetTeamId(Integer targetTeamId) {
            this.targetTeamId = targetTeamId;
            return this;
        }

        public ChangeLicensesTeamBuilder withLicenseIds(List<String> licenseIds) {
            this.licenseIds = licenseIds;
            return this;
        }

        public ChangeLicensesTeam build() {
            return new ChangeLicensesTeam(this);
        }
    }

}

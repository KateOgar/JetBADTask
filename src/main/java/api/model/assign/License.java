package api.model.assign;


public class License {
    private Integer team;
    private String productCode;

    public Integer getTeam() {
        return team;
    }

    public String getProductCode() {
        return productCode;
    }

    private License(LicenseBuilder builder) {
        this.team = builder.team;
        this.productCode = builder.productCode;
    }

    public static class LicenseBuilder {
        private Integer team;
        private String productCode;

        public LicenseBuilder withTeam(Integer team) {
            this.team = team;
            return this;
        }

        public LicenseBuilder withProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public License build() {
            return new License(this);
        }
    }
}

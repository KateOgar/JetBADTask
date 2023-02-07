package data;

import io.restassured.specification.RequestSpecification;

import static data.Configuration.getConfig;
import static util.RequestUtils.restAssuredGiven;

public class User {

    public static String getXApiKey(Roles role) {
        return getConfig(role.getPropertyName() + ".api_key");
    }

    public static String getxCustomerCode() {
        return getConfig("customer_code");
    }

    public static RequestSpecification withRole(Roles role) {
        return restAssuredGiven()
                .header("X-Customer-Code", getxCustomerCode())
                .header("X-Api-Key", getXApiKey(role));
    }
}

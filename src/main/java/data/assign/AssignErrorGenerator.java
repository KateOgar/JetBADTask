package data.assign;

import api.model.common.Error;

public class AssignErrorGenerator {

    // Codes
    public static final String TEAM_MISMATCH = "TEAM_MISMATCH";
    public static final String MISSING_TOKEN_HEADER = "MISSING_TOKEN_HEADER";
    public static final String MISSING_CUSTOMER_HEADER = "MISSING_CUSTOMER_HEADER";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String MISSING_FIELD = "MISSING_FIELD";
    public static final String LICENSE_ASSIGNED = "LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN";
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String NO_AVAILABLE_LICENSE_TO_ASSIGN = "NO_AVAILABLE_LICENSE_TO_ASSIGN";
    public static final String LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN = "LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN";
    public static final String INVALID_CONTACT_EMAIL = "INVALID_CONTACT_EMAIL";
    public static final String INSUFFICIENT_PERMISSIONS = "INSUFFICIENT_PERMISSIONS";
    public static final String INVALID_CONTACT_NAME = "INVALID_CONTACT_NAME";


    // Descriptions
    public static final String LICENSE_NOT_FOUND = "LICENSE_NOT_FOUND";
    public static final String API_HEADER_REQUIRED = "X-Api-Key header is required";
    public static final String CUSTOMER_HEADER_REQUIRED = "X-Customer-Code header is required";
    public static final String MISMATCH_DESCRIPTION = "Token was generated for team with id %d";
    public static final String INVALID_TOKEN_DESCR = "The token provided is invalid";
    public static final String MISSED_LICENSES = "Either licenseId or license must be provided";
    public static final String MISSED_FIELD = "Field must be provided";
    public static final String ALLOCATED = "ALLOCATED";
    public static final String NO_AVAILABLE_FOR_CODE = "No available license found to assign in the team %d with product %s";
    public static final String EXPIRED_WITHOUT_FALLBACK = "EXPIRED_WITHOUT_FALLBACK";
    public static final String MISSING_EDIT_PERMISSION = "Missing Edit permission on customer %s or on team with id %d";
    public static final String FIELD_CANT_BE_EMPTY = "This field can't be empty.";


    public static Error assignError(String code, String description) {
        Error error = new Error();
        error.setCode(code);
        error.setDescription(description);
        return error;
    }
}

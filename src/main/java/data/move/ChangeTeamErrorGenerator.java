package data.move;

import api.model.common.Error;

public class ChangeTeamErrorGenerator {

    // Codes
    public static final String TOKEN_TYPE_MISMATCH = "TOKEN_TYPE_MISMATCH";
    public static final String TEAM_NOT_FOUND = "TEAM_NOT_FOUND";


    //Descriptions
    public static final String CHANGE_NOT_POSSIBLE = "Changing team is not possible with a token that was generated for a specific team";

    public static Error changeTeamError(String code, String description) {
        Error error = new Error();
        error.setCode(code);
        error.setDescription(description);
        return error;
    }
}

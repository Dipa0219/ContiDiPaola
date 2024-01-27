package it.polimi.SE2.CK.utils.enumeration;

/**
 * Enumeration class that represent the state of acceptance to join a team.
 */
public enum TeamStudentState {
    ACCEPT("Accept"),
    NOTACCEPT("Not Accept");

    /**
     * Enumeration value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param value the enumeration value.
     */
    TeamStudentState(String value) {
        this.value = value;
    }

    /**
     * Gets the TeamStudentState value.
     *
     * @return the tournament state value.
     */
    public String getValue() {
        return value;
    }
}

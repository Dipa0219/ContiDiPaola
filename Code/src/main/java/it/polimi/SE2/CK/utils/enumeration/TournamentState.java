package it.polimi.SE2.CK.utils.enumeration;

/**
 * Enumeration class that represent the tournament state.
 */
public enum TournamentState {
    NOTSTARTED("Not Started"),
    ONGOING("Ongoing"),
    CLOSED("Closed");

    /**
     * Enumeration value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param value the enumeration value.
     */
    TournamentState(String value) {
        this.value = value;
    }

    /**
     * Gets the TournamentState value.
     *
     * @return the tournament state value.
     */
    public String getValue() {
        return value;
    }
}

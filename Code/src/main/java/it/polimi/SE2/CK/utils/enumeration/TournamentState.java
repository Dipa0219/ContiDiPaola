package it.polimi.SE2.CK.utils.enumeration;

/**
 * Enumeration class that represent the tournament state.
 */
public enum TournamentState {
    NOTSTARTED(0),
    ONGOING(1),
    ENDED(2);

    /**
     * Enumeration value.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param value the enumeration value.
     */
    TournamentState(int value) {
        this.value = value;
    }

    /**
     * Gets the TournamentState value.
     *
     * @return the tournament state value.
     */
    public int getValue() {
        return value;
    }
}

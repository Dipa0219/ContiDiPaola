package it.polimi.SE2.CK.utils.enumeration;

public enum UserRole {
    EDUCATOR(0),
    STUDENT(1);

    /**
     * Enumeration value.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param value the enumeration value.
     */
    UserRole(int value) {
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

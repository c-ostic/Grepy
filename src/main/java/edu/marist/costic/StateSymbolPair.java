package edu.marist.costic;

/**
 * Represents a state and a symbol, for use in the delta functions of FSAs.
 */
public class StateSymbolPair {

    public final char EPSILON = ' ';

    private final int state;
    private final char symbol;

    /**
     * Constructs a StateSymbolPair object.
     * @param state the FSA state.
     * @param symbol a symbol in the FSA's alphabet.
     */
    public StateSymbolPair(int state, char symbol) {
        this.state = state;
        this.symbol = symbol;
    }

    public StateSymbolPair(int state) {
        this.state = state;
        this.symbol = EPSILON;
    }

    /**
     * Gets the state.
     * @return the state.
     */
    public int getState() {
        return state;
    }

    /**
     * Gets the symbol.
     * @return the symbol.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Compares two StateSymbolPairs based upon state and symbol.
     * @param other the other StateSymbolPair.
     * @return true if the parts of each StateSymbolPair are equal.
     */
    public boolean equals(StateSymbolPair other) {
        return state == other.getState() && symbol == other.getSymbol();
    }

    /**
     * Compares this object to a state and a symbol.
     * @param otherState the other state to compare to.
     * @param otherSymbol the other symbol to compare to.
     * @return true if the state and symbol match those of this object.
     */
    public boolean equals(int otherState, char otherSymbol) {
        return state == otherState && symbol == otherSymbol;
    }
}

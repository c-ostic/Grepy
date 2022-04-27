package edu.marist.costic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an NFA.
 */
public class NFA {
    // the delta function maps a state to the list of states it is connected to on a particular symbol
    private Map<StateSymbolPair, List<Integer>> deltaFunction;

    private Set<Character> alphabet;
    private int states;
    private int startState;
    private int endState;

    private String regex;
    private int currentChar;

    /**
     * Constructs an NFA object given a regex string and an alphabet to work with.
     * @param regex the regex string.
     * @param alphabet the alphabet to use.
     */
    public NFA(String regex, Set<Character> alphabet) {
        this.alphabet = alphabet;
        this.regex = regex;
        currentChar = 0;
        states = 0;

        deltaFunction = new HashMap<StateSymbolPair, List<Integer>>();

        try {
            parseRegex();
        } catch (InvalidRegexException e) {
            Utils.error("Error parsing regex: " + e.getMessage());
        }
    }

    /**
     * Starts the recursive descent through parsing the regex.
     * @throws InvalidRegexException
     */
    private void parseRegex() throws InvalidRegexException {
        int[] startEndstates = parseUnionGroup();
        startState = startEndstates[0];
        endState = startEndstates[1];
    }

    /**
     * Parses a group of the regex where the lowest precedence operator is union, '+'.
     * @return the beginning and end states given by Thompson's algorithm, inside of a int array.
     * @throws InvalidRegexException
     */
    private int[] parseUnionGroup() throws InvalidRegexException {
        int[] leftSide = parseConcatGroup();

        if (currentChar < regex.length() && regex.charAt(currentChar) != ')') {
            if (regex.charAt(currentChar) == '+') {
                // the next character is a '+' so move past it
                currentChar++;
                int[] rightSide = parseUnionGroup();

                // get the next 2 available state counts and make them the beginning and the end of the union
                int unionStart = states;
                states++;
                int unionEnd = states;
                states++;

                // add the necessary relations to the delta function
                addToDelta(new StateSymbolPair(unionStart), leftSide[0]);
                addToDelta(new StateSymbolPair(unionStart), rightSide[0]);
                addToDelta(new StateSymbolPair(leftSide[1]), unionEnd);
                addToDelta(new StateSymbolPair(rightSide[1]), unionEnd);

                return new int[] {unionStart, unionEnd};
            } else {
                throw new InvalidRegexException("Invalid characters beyond regex");
            }
        } else {
            return leftSide;
        }
    }

    /**
     * Parses a group of the regex where the lowest precedence operator is concatenation.
     * @return the beginning and end states given by Thompson's algorithm, inside of a int array.
     * @throws InvalidRegexException
     */
    private int[] parseConcatGroup() throws InvalidRegexException {
        int[] leftSide = parseKleeneGroup();
        if (currentChar < regex.length() && regex.charAt(currentChar) != ')' && regex.charAt(currentChar) != '+') {
            // there is more to concat
            int[] rightSide = parseConcatGroup();

            // add the necessary relation to the delta function
            addToDelta(new StateSymbolPair(leftSide[1]), rightSide[0]);

            return new int[] {leftSide[0], rightSide[1]};
        } else {
            return leftSide;
        }
    }

    /**
     * Parses a group of the regex where the lowest precedence operator is Kleene star, '*'.
     * @return the beginning and end states given by Thompson's algorithm, inside of a int array.
     * @throws InvalidRegexException
     */
    private int[] parseKleeneGroup() throws InvalidRegexException {
        int[] symbolGroup = parseSymbolGroup();
        if (currentChar < regex.length() && regex.charAt(currentChar) == '*') {
            // the next character is a '*' so move past it
            currentChar++;

            // add the necessary relations to the delta function (epsilons between the existing start and end states)
            addToDelta(new StateSymbolPair(symbolGroup[0]), symbolGroup[1]);
            addToDelta(new StateSymbolPair(symbolGroup[1]), symbolGroup[0]);
        }
        return symbolGroup;
    }

    /**
     * Parses a group of the regex made up of either a symbol or another regex string inside of parenthesis.
     * @return the beginning and end states given by Thompson's algorithm, inside of a int array.
     * @throws InvalidRegexException
     */
    private int[] parseSymbolGroup() throws InvalidRegexException {
        if (currentChar >= regex.length()) {
            throw new InvalidRegexException("Reached end of regex expecting more characters");
        }
        if (regex.charAt(currentChar) == '(') {
            // the next character is a '(' so move past it
            currentChar++;

            // call the top level regex expression
            int[] innerRegex = parseUnionGroup();

            if (regex.charAt(currentChar) == ')') {
                currentChar++;
            } else {
                throw new InvalidRegexException("Missing right parenthesis");
            }

            return innerRegex;
        } else if (alphabet.contains(regex.charAt(currentChar))) {
            // get the next two available states for the start and end of this one symbol expression
            int start = states;
            states++;
            int end = states;
            states++;

            // add the necessary relation to the delta function
            addToDelta(new StateSymbolPair(start, regex.charAt(currentChar)), end);

            // advance the character counter
            currentChar++;

            return new int[] {start, end};
        } else {
            throw new InvalidRegexException("Character not in recognized alphabet");
        }
    }

    /**
     * Adds a state and symbol pair with its destination to the deltaFunction.
     * @param pair the starting state and the symbol paired with it.
     * @param result the destination state from this move.
     */
    private void addToDelta(StateSymbolPair pair, int result) {
        // get the current list associated with this pair
        List<Integer> currentList = deltaFunction.get(pair);

        // if the list doesn't exist, then it is a new pair and it needs to be added
        if (currentList == null) {
            currentList = new ArrayList<Integer>();
            deltaFunction.put(pair, currentList);
        }

        // add the result to the list
        currentList.add(result);
    }

    /**
     * Converts the NFA to dot format.
     * @return the dot format as a string.
     */
    public String convertToDot() {
        String dotFormat = "digraph nfa {\n";

        // add the double circle to the end state
        dotFormat += "\t" + endState + " [shape=doublecircle];\n";

        // add the start state with a fake empty state to simulate the first arrow
        dotFormat += "\tstart [label=\"\",shape=none];\n";
        dotFormat += "\tstart -> " + startState + ";\n\n";

        // add each of the node pairs to the string
        for (StateSymbolPair pair : deltaFunction.keySet()) {
            List<Integer> destinations = deltaFunction.get(pair);
            for (int dest : destinations) {
                if (pair.getSymbol() == StateSymbolPair.EPSILON) {
                    dotFormat += "\t" + pair.getState() + " -> " + dest + " [label=epsilon];\n";
                } else {
                    dotFormat += "\t" + pair.getState() + " -> " + dest + " [label=" + pair.getSymbol() + "];\n";
                }
            }
        }

        dotFormat += "}";

        return dotFormat;
    }
}

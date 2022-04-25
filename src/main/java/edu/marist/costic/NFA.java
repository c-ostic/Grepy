package edu.marist.costic;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

class InvalidRegexException extends Exception
{
    public InvalidRegexException(String message)
    {
        super(message);
    }
}

public class NFA {
    // the delta function maps a state to the list of states it is connected to on a particular symbol
    private Dictionary<StateSymbolPair, List<Integer>> deltaFunction;

    private Set<Character> alphabet;
    private int states;

    private String regex;
    private int currentChar;

    public NFA(String regex, Set<Character> alphabet) {
        this.alphabet = alphabet;
        this.regex = regex;
        currentChar = 0;
        states = 0;

        deltaFunction = new Hashtable<StateSymbolPair,List<Integer>>();

        try {
            parseRegex();
        } catch (InvalidRegexException e) {
            Utils.error("Error parsing regex: " + e.getMessage());
        }
    }

    private void parseRegex() throws InvalidRegexException {
        parseUnionGroup();
    }

    private int[] parseUnionGroup() throws InvalidRegexException {
        int[] leftSide = parseConcatGroup();

        if(currentChar < regex.length() && regex.charAt(currentChar) != ')') {
            if(regex.charAt(currentChar) != '+') {
                throw new InvalidRegexException("Invalid characters beyond regex");
            } else {
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
            }
        } else {
            return leftSide;
        }
    }

    private int[] parseConcatGroup() {
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

    private int[] parseKleeneGroup() {
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

    private int[] parseSymbolGroup() {
        return null;
    }

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

    public String convertToDot(){
        return "";
    }
}
